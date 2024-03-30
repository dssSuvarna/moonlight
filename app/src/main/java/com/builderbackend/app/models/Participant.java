package com.builderbackend.app.models;

import java.time.Instant;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * specifies a given user tied to a given chat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Participants")
public class Participant {

    @Id
    private String participantId;

    @ManyToOne
    @JoinColumn(name = "conversationId")
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant joinedAt;

}