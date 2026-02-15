package com.lowkkid.github.customersupport.service;

import com.lowkkid.github.customersupport.domain.entity.enums.Category;
import com.lowkkid.github.customersupport.domain.entity.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;

public interface CategorizationService {

    CategoryAndPriority categorizeAndPrioritize(String messageBody);

    @Data
    @AllArgsConstructor
    class CategoryAndPriority {
        private Category category;
        private Priority priority;
    }
}
