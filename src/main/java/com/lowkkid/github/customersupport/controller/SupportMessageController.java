package com.lowkkid.github.customersupport.controller;

import com.lowkkid.github.customersupport.dto.CustomerMessage;
import com.lowkkid.github.customersupport.dto.SupportMessageDto;
import com.lowkkid.github.customersupport.model.enums.Category;
import com.lowkkid.github.customersupport.model.enums.Priority;
import com.lowkkid.github.customersupport.model.enums.Status;
import com.lowkkid.github.customersupport.service.SupportMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class SupportMessageController {

    private final SupportMessageService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupportMessageDto createMessage(@RequestBody CustomerMessage customerMessage) {
        return service.createMessage(customerMessage);
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

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<SupportMessageDto> markAsResolved(@PathVariable Long id) {
        return ResponseEntity.ok(service.markAsResolved(id));
    }
}
