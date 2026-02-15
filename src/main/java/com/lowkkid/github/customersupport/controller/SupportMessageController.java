package com.lowkkid.github.customersupport.controller;

import com.lowkkid.github.customersupport.dto.CustomerMessage;
import com.lowkkid.github.customersupport.dto.SupportMessageDto;
import com.lowkkid.github.customersupport.service.SupportMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class SupportMessageController {

    private final SupportMessageService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupportMessageDto createMessage(@RequestBody CustomerMessage customerMessage) {
        return service.createMessage(customerMessage);
    }

    @GetMapping
    public List<SupportMessageDto> getAllMessages() {
        return service.getAllMessages();
    }
}
