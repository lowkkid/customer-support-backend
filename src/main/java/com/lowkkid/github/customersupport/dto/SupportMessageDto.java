package com.lowkkid.github.customersupport.dto;

import com.lowkkid.github.customersupport.model.enums.Category;
import com.lowkkid.github.customersupport.model.enums.Priority;
import com.lowkkid.github.customersupport.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class SupportMessageDto {

    private Long id;
    private String customerName;
    private String messageBody;
    private Category category;
    private Priority priority;
    private Status status;
    private LocalDateTime createdAt;
}
