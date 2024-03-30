package com.builderbackend.app.models;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Conversations")
public class Conversation {

    @Id
    private String conversationId;

    private String conversationName;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conversation")
    private List<Participant> participants;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conversation")
    private List<Message> messages;

}