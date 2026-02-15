package com.lowkkid.github.customersupport.model;

import com.lowkkid.github.customersupport.model.enums.Category;
import com.lowkkid.github.customersupport.model.enums.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "support_message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupportMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "message_body", nullable = false, columnDefinition = "TEXT")
    private String messageBody;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    private Priority priority;

    @Column(name = "resolved", nullable = false)
    private boolean resolved = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
