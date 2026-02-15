package com.lowkkid.github.customersupport.service.impl;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.lowkkid.github.customersupport.domain.entity.enums.Category;
import com.lowkkid.github.customersupport.domain.entity.enums.Priority;
import com.lowkkid.github.customersupport.service.CategorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategorizationServiceImpl implements CategorizationService {

    private final Client geminiClient;

    @Value("${gemini.model}")
    private String modelName;

    @Override
    public CategoryAndPriority categorizeAndPrioritize(String messageBody) {
        String prompt = buildCombinedPrompt(messageBody);

        try {
            GenerateContentResponse response = CompletableFuture
                    .supplyAsync(() -> geminiClient.models.generateContent(modelName, prompt, null))
                    .get(60, TimeUnit.SECONDS);

            String responseText = response.text();
            log.info("Gemini response: {}", responseText);

            return parseCombinedResponse(responseText, messageBody);

        } catch (TimeoutException e) {
            log.warn("Gemini API timed out after 60s, using fallback");
            Category category = fallbackCategorization(messageBody);
            Priority priority = fallbackPrioritization(messageBody, category);
            return new CategoryAndPriority(category, priority);
        } catch (Exception e) {
            log.error("Error calling Gemini API: {}", e.getMessage());
            Category category = fallbackCategorization(messageBody);
            Priority priority = fallbackPrioritization(messageBody, category);
            return new CategoryAndPriority(category, priority);
        }
    }

    private String buildCombinedPrompt(String message) {
        return String.format("""
            Classify this support message. Reply ONLY two lines:
            CATEGORY: BUG|BILLING|FEATURE_REQUEST|GENERAL
            PRIORITY: HIGH|MEDIUM|LOW

            Message: "%s"
            """, message);
    }

    private CategoryAndPriority parseCombinedResponse(String response, String originalMessage) {
        try {
            String[] lines = response.split("\n");
            Category category = Category.GENERAL;
            Priority priority = Priority.MEDIUM;

            for (String line : lines) {
                String upper = line.toUpperCase().trim();

                if (upper.startsWith("CATEGORY:")) {
                    String categoryStr = upper.replace("CATEGORY:", "").trim();
                    category = parseCategory(categoryStr);
                }

                if (upper.startsWith("PRIORITY:")) {
                    String priorityStr = upper.replace("PRIORITY:", "").trim();
                    priority = parsePriority(priorityStr);
                }
            }

            return new CategoryAndPriority(category, priority);

        } catch (Exception e) {
            log.error("Error parsing response: {}", e.getMessage());
            Category category = fallbackCategorization(originalMessage);
            Priority priority = fallbackPrioritization(originalMessage, category);
            return new CategoryAndPriority(category, priority);
        }
    }

    private Category parseCategory(String text) {
        String cleaned = text.replaceAll("[^A-Z_]", "");

        try {
            return Category.valueOf(cleaned);
        } catch (IllegalArgumentException e) {
            if (cleaned.contains("BUG")) return Category.BUG;
            if (cleaned.contains("BILLING")) return Category.BILLING;
            if (cleaned.contains("FEATURE")) return Category.FEATURE_REQUEST;
            return Category.GENERAL;
        }
    }

    private Priority parsePriority(String text) {
        if (text.contains("HIGH")) return Priority.HIGH;
        if (text.contains("MEDIUM")) return Priority.MEDIUM;
        if (text.contains("LOW")) return Priority.LOW;
        return Priority.MEDIUM;
    }

    private Category fallbackCategorization(String message) {
        String lower = message.toLowerCase();

        if (lower.matches(".*(crash|error|bug|broken|not working|fail|down|outage).*")) {
            return Category.BUG;
        }
        if (lower.matches(".*(invoice|charge|payment|bill|refund|subscription).*")) {
            return Category.BILLING;
        }
        if (lower.matches(".*(feature|add|support|would like|request|improve).*")) {
            return Category.FEATURE_REQUEST;
        }

        return Category.GENERAL;
    }

    private Priority fallbackPrioritization(String message, Category category) {
        String lower = message.toLowerCase();

        if (lower.matches(".*(urgent|asap|critical|immediately|emergency|production).*")) {
            return Priority.HIGH;
        }

        if (category == Category.BUG &&
                lower.matches(".*(crash|data loss|security|cannot access).*")) {
            return Priority.HIGH;
        }

        if (category == Category.BILLING &&
                lower.matches(".*(charged.*twice|unauthorized).*")) {
            return Priority.HIGH;
        }

        if (category == Category.BUG || category == Category.BILLING) {
            return Priority.MEDIUM;
        }

        return Priority.LOW;
    }
}
