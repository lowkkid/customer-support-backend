package com.lowkkid.github.customersupport.dto;

import java.util.Map;

public record StatsResponse(
        Map<String, Long> byCategory,
        Map<String, Long> byPriority
) {}
