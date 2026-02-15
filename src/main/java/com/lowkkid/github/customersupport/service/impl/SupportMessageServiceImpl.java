package com.lowkkid.github.customersupport.service.impl;

import com.lowkkid.github.customersupport.domain.projection.CategoryCount;
import com.lowkkid.github.customersupport.domain.projection.PriorityCount;
import com.lowkkid.github.customersupport.dto.CustomerMessage;
import com.lowkkid.github.customersupport.dto.StatsResponse;
import com.lowkkid.github.customersupport.dto.SupportMessageDto;
import com.lowkkid.github.customersupport.mapper.SupportMessageMapper;
import com.lowkkid.github.customersupport.domain.entity.SupportMessage;
import com.lowkkid.github.customersupport.domain.entity.enums.Category;
import com.lowkkid.github.customersupport.domain.entity.enums.Priority;
import com.lowkkid.github.customersupport.domain.entity.enums.Status;
import com.lowkkid.github.customersupport.domain.repository.SupportMessageRepository;
import com.lowkkid.github.customersupport.service.CategorizationService;
import com.lowkkid.github.customersupport.service.SupportMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportMessageServiceImpl implements SupportMessageService {

    private final SupportMessageRepository repository;
    private final SupportMessageMapper mapper;
    private final CategorizationService categorizationService;

    @Override
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

    @Override
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

    @Override
    public SupportMessageDto getMessageById(Long id) {
        SupportMessage message = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        return mapper.toDto(message);
    }

    @Override
    public SupportMessageDto markAsResolved(Long id) {
        SupportMessage message = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        message.setStatus(Status.RESOLVED);
        return mapper.toDto(repository.save(message));
    }

    @Override
    public StatsResponse getStats() {
        Map<String, Long> byCategory = repository.countUnresolvedByCategory().stream()
                .collect(Collectors.toMap(c -> c.getCategory().name(), CategoryCount::getCount));

        Map<String, Long> byPriority = repository.countUnresolvedByPriority().stream()
                .collect(Collectors.toMap(p -> p.getPriority().name(), PriorityCount::getCount));

        return new StatsResponse(byCategory, byPriority);
    }
}
