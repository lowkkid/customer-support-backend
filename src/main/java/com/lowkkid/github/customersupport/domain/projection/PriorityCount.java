package com.lowkkid.github.customersupport.domain.projection;

import com.lowkkid.github.customersupport.domain.entity.enums.Priority;

public interface PriorityCount {
    Priority getPriority();
    Long getCount();
}