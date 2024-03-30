package com.builderbackend.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private String messageId;
    private String conversationId; // Reference by ID
    private String senderId; // Reference by ID
    private String messageText;
    private Instant sentAt;
    private Boolean isRead;
}