package com.lowkkid.github.customersupport.controller;

import com.lowkkid.github.customersupport.domain.entity.enums.Category;
import com.lowkkid.github.customersupport.domain.entity.enums.Priority;
import com.lowkkid.github.customersupport.domain.entity.enums.Status;
import com.lowkkid.github.customersupport.dto.CustomerMessage;
import com.lowkkid.github.customersupport.dto.StatsResponse;
import com.lowkkid.github.customersupport.dto.SupportMessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

@Tag(name = "Support Messages", description = "Endpoints for managing customer support messages")
public interface SupportMessageApi {

    @Operation(summary = "Create a new support message",
            description = "Accepts a customer message, categorizes and prioritizes it using AI, and saves it")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Message created successfully",
                    content = @Content(schema = @Schema(implementation = SupportMessageDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content)
    })
    ResponseEntity<SupportMessageDto> createMessage(
            @Parameter(description = "Customer message to process", required = true)
            CustomerMessage customerMessage
    );

    @Operation(summary = "Get all support messages",
            description = "Returns a paginated, sortable and filterable list of support messages")
    @ApiResponse(responseCode = "200", description = "Page of messages returned successfully")
    ResponseEntity<Page<SupportMessageDto>> getAll(
            @Parameter(description = "Filter by priority") Priority priority,
            @Parameter(description = "Filter by category") Category category,
            @Parameter(description = "Filter by status") Status status,
            @Parameter(description = "Page number (1-based)", example = "1") Integer pageNumber,
            @Parameter(description = "Number of items per page", example = "10") Integer pageSize,
            @Parameter(description = "Field to sort by", example = "createdAt") String sortField,
            @Parameter(description = "Sort direction") Sort.Direction sortDirection
    );

    @Operation(summary = "Get a message by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message found",
                    content = @Content(schema = @Schema(implementation = SupportMessageDto.class))),
            @ApiResponse(responseCode = "404", description = "Message not found",
                    content = @Content)
    })
    ResponseEntity<SupportMessageDto> getById(
            @Parameter(description = "Message ID", required = true, example = "1") Long id
    );

    @Operation(summary = "Get statistics",
            description = "Returns counts of unresolved messages grouped by category and priority")
    @ApiResponse(responseCode = "200", description = "Statistics returned successfully",
            content = @Content(schema = @Schema(implementation = StatsResponse.class)))
    ResponseEntity<StatsResponse> getStats();

    @Operation(summary = "Mark a message as resolved")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message marked as resolved",
                    content = @Content(schema = @Schema(implementation = SupportMessageDto.class))),
            @ApiResponse(responseCode = "404", description = "Message not found",
                    content = @Content)
    })
    ResponseEntity<SupportMessageDto> markAsResolved(
            @Parameter(description = "Message ID", required = true, example = "1") Long id
    );
}
