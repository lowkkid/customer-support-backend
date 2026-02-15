package com.lowkkid.github.customersupport.service;

import com.lowkkid.github.customersupport.dto.CustomerMessage;
import com.lowkkid.github.customersupport.dto.StatsResponse;
import com.lowkkid.github.customersupport.dto.SupportMessageDto;
import com.lowkkid.github.customersupport.domain.entity.enums.Category;
import com.lowkkid.github.customersupport.domain.entity.enums.Priority;
import com.lowkkid.github.customersupport.domain.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface SupportMessageService {

    SupportMessageDto createMessage(CustomerMessage customerMessage);

    Page<SupportMessageDto> getAll(
            Priority priority,
            Category category,
            Status status,
            Integer pageNumber,
            Integer pageSize,
            String sortField,
            Sort.Direction sortDirection
    );

    SupportMessageDto getMessageById(Long id);

    SupportMessageDto markAsResolved(Long id);

    StatsResponse getStats();
}
