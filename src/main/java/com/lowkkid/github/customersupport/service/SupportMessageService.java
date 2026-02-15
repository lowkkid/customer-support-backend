package com.lowkkid.github.customersupport.service;

import com.lowkkid.github.customersupport.dto.CustomerMessage;
import com.lowkkid.github.customersupport.dto.SupportMessageDto;
import com.lowkkid.github.customersupport.mapper.SupportMessageMapper;
import com.lowkkid.github.customersupport.model.SupportMessage;
import com.lowkkid.github.customersupport.model.enums.Category;
import com.lowkkid.github.customersupport.model.enums.Priority;
import com.lowkkid.github.customersupport.model.enums.Status;
import com.lowkkid.github.customersupport.repository.SupportMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
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
        message.setStatus(Status.UNRESOLVED);
        message.setCreatedAt(LocalDateTime.now());

        return mapper.toDto(repository.save(message));
    }

    public Page<SupportMessageDto> getAll(
            Priority priority,
            Category category,
            Status status,
            Integer pageNumber,
            Integer pageSize,
            String sortField,
            Sort.Direction sortDirection
    ) {
        Sort sort;
        if ("priority".equals(sortField)) {
            sort = JpaSort.unsafe(sortDirection,
                    "(CASE sm.priority WHEN 'HIGH' THEN 3 WHEN 'MEDIUM' THEN 2 WHEN 'LOW' THEN 1 END)");
        } else {
            sort = Sort.by(sortDirection, sortField);
        }
        return repository.findAll(priority, category, status,
                PageRequest.of(pageNumber - 1, pageSize, sort));
    }

    public SupportMessageDto getMessageById(Long id) {
        SupportMessage message = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        return mapper.toDto(message);
    }

    public SupportMessageDto markAsResolved(Long id) {
        SupportMessage message = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        message.setStatus(Status.RESOLVED);
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
