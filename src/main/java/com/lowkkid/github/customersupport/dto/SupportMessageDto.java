package com.lowkkid.github.customersupport.dto;

import com.lowkkid.github.customersupport.model.enums.Category;
import com.lowkkid.github.customersupport.model.enums.Priority;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SupportMessageDto {

    private Long id;
    private String customerName;
    private String messageBody;
    private Category category;
    private Priority priority;
    private boolean resolved;
    private LocalDateTime createdAt;
}
