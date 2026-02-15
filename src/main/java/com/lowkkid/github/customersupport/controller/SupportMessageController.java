package com.lowkkid.github.customersupport.controller;

import com.lowkkid.github.customersupport.dto.SupportMessageDto;
import com.lowkkid.github.customersupport.service.SupportMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class SupportMessageController {

    private final SupportMessageService service;

    @GetMapping
    public List<SupportMessageDto> getAllMessages() {
        return service.getAllMessages();
    }
}
