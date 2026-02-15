package com.lowkkid.github.customersupport.controller.impl;

import com.lowkkid.github.customersupport.controller.SupportMessageApi;
import com.lowkkid.github.customersupport.dto.CustomerMessage;
import com.lowkkid.github.customersupport.dto.StatsResponse;
import com.lowkkid.github.customersupport.dto.SupportMessageDto;
import com.lowkkid.github.customersupport.domain.entity.enums.Category;
import com.lowkkid.github.customersupport.domain.entity.enums.Priority;
import com.lowkkid.github.customersupport.domain.entity.enums.Status;
import com.lowkkid.github.customersupport.service.SupportMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class SupportMessageController implements SupportMessageApi {

    private final SupportMessageService service;

    @PostMapping
    public ResponseEntity<SupportMessageDto> createMessage(@RequestBody CustomerMessage customerMessage) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createMessage(customerMessage));
    }

    @GetMapping
    public ResponseEntity<Page<SupportMessageDto>> getAll(
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Status status,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        var messages = service.getAll(priority, category, status, pageNumber, pageSize, sortField, sortDirection);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportMessageDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getMessageById(id));
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(service.getStats());
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<SupportMessageDto> markAsResolved(@PathVariable Long id) {
        return ResponseEntity.ok(service.markAsResolved(id));
    }
}
