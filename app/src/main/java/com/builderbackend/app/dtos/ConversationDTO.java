package com.builderbackend.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDTO {

    private String conversationId;
    private String conversationName;
    private String projectId;
    private Instant createdAt;
    private List<ParticipantDTO> participantDTOs; // List of participant DTOs
    //private List<MessageDTO> messages;   // List of message DTOs
}