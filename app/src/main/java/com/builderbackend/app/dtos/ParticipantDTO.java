package com.builderbackend.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantDTO {

    private String participantId;
    private String conversationId; // Reference by ID rather than the whole entity
    // the DTO name "ClientUserDTO" is a misnomer. the userDTO atreibute will hold the user details for either a client or employee.. possible consider creating a generic dto for this
    // i also dont want to use the userDTO since that contains sensative info such as password
    private ClientUserDTO userDTO;         // Reference by ID
    private Instant joinedAt;
}