package com.lowkkid.github.customersupport.domain.projection;

import com.lowkkid.github.customersupport.domain.entity.enums.Category;

public interface CategoryCount {
    Category getCategory();
    Long getCount();
}