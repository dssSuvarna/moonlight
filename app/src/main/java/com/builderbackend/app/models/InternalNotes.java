package com.builderbackend.app.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import java.util.List;
import java.time.LocalDateTime;

@Data
@Entity
public class InternalNotes {

    @Id
    String internalNoteId;

    String title;
    String date;

    @Column(columnDefinition = "TEXT")
    String description;

    @ManyToOne
    private User user;

    @ManyToOne
    private Business business;

    @ManyToOne
    private Project project;

}
