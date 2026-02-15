package com.lowkkid.github.customersupport.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.lowkkid.github.customersupport.model.enums.Category;
import com.lowkkid.github.customersupport.model.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategorizationService {

    private final Client geminiClient;

    @Value("${gemini.model}")
    private String modelName;

    public CategoryAndPriority categorizeAndPrioritize(String messageBody) {
        String prompt = buildCombinedPrompt(messageBody);

        try {
            GenerateContentResponse response = geminiClient.models.generateContent(
                    modelName,
                    prompt,
                    null
            );

            String responseText = response.text();
            log.info("Gemini response: {}", responseText);

            return parseCombinedResponse(responseText, messageBody);

        } catch (Exception e) {
            log.error("Error calling Gemini API: {}", e.getMessage(), e);
            Category category = fallbackCategorization(messageBody);
            Priority priority = fallbackPrioritization(messageBody, category);
            return new CategoryAndPriority(category, priority);
        }
    }

    private String buildCombinedPrompt(String message) {
        return String.format("""
            You are a customer support triage specialist.
            
            Analyze this message and provide:
            1. Category (BUG, BILLING, FEATURE_REQUEST, or GENERAL)
            2. Priority (HIGH, MEDIUM, or LOW)
            
            Categories:
            - BUG: Technical issues, crashes, errors, broken features
            - BILLING: Invoices, payments, charges, refunds, subscriptions
            - FEATURE_REQUEST: New features, improvements, suggestions
            - GENERAL: Questions, general inquiries, other
            
            Priority Levels:
            - HIGH: Urgent, critical, blocking, production issues, contains "urgent"/"asap"/"critical"
            - MEDIUM: Important but not blocking, has workarounds
            - LOW: Feature requests, general questions, non-urgent
            
            Customer Message:
            "%s"
            
            Respond in this EXACT format (two lines):
            CATEGORY: [category]
            PRIORITY: [priority]
            
            Example:
            CATEGORY: BUG
            PRIORITY: HIGH
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

    @Data
    @AllArgsConstructor
    public static class CategoryAndPriority {
        private Category category;
        private Priority priority;
    }
}