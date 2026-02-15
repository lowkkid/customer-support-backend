package com.lowkkid.github.customersupport.service;

import com.lowkkid.github.customersupport.dto.CustomerMessage;
import com.lowkkid.github.customersupport.dto.SupportMessageDto;
import com.lowkkid.github.customersupport.mapper.SupportMessageMapper;
import com.lowkkid.github.customersupport.model.SupportMessage;
import com.lowkkid.github.customersupport.model.enums.Category;
import com.lowkkid.github.customersupport.model.enums.Priority;
import com.lowkkid.github.customersupport.repository.SupportMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportMessageService {

    private final SupportMessageRepository repository;
    private final SupportMessageMapper mapper;
    private final CategorizationService categorizationService;

    public SupportMessageDto createMessage(CustomerMessage customerMessage) {
        CategorizationService.CategoryAndPriority result =
                categorizationService.categorizeAndPrioritize(customerMessage.messageBody());

        SupportMessage message = new SupportMessage();
        message.setCustomerName(customerMessage.customerName());
        message.setMessageBody(customerMessage.messageBody());
        message.setCategory(result.getCategory());
        message.setPriority(result.getPriority());
        message.setResolved(false);
        message.setCreatedAt(LocalDateTime.now());

        return mapper.toDto(repository.save(message));
    }

    public List<SupportMessageDto> getAllMessages() {
        return mapper.toDtoList(repository.findAll());
    }

    public SupportMessageDto getMessageById(Long id) {
        SupportMessage message = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        return mapper.toDto(message);
    }

    public List<SupportMessageDto> getMessagesByCategory(Category category) {
        return mapper.toDtoList(repository.findByCategory(category));
    }

    public List<SupportMessageDto> getMessagesByPriority(Priority priority) {
        return mapper.toDtoList(repository.findByPriority(priority));
    }

    public List<SupportMessageDto> getMessagesByCategoryAndPriority(Category category, Priority priority) {
        return mapper.toDtoList(repository.findByCategoryAndPriority(category, priority));
    }

    public List<SupportMessageDto> filterMessages(Category category, Priority priority) {
        if (category != null && priority != null) {
            return mapper.toDtoList(repository.findByCategoryAndPriority(category, priority));
        } else if (category != null) {
            return mapper.toDtoList(repository.findByCategory(category));
        } else if (priority != null) {
            return mapper.toDtoList(repository.findByPriority(priority));
        }
        return mapper.toDtoList(repository.findAll());
    }

    public SupportMessageDto markAsResolved(Long id) {
        SupportMessage message = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        message.setResolved(true);
        return mapper.toDto(repository.save(message));
    }

    public Map<Category, Long> getCountByCategory() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(SupportMessage::getCategory, Collectors.counting()));
    }

    public Map<Priority, Long> getCountByPriority() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(SupportMessage::getPriority, Collectors.counting()));
    }
}
