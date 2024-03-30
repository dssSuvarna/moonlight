package com.builderbackend.app.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import java.util.List;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Selections {

    @Id
    String selectionId;

    String name;

    @Column(columnDefinition = "TEXT")

    String description;
    String partNumber;
    String cost;

    Boolean clientConfirmation;

    @ManyToOne
    private User user;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Business business;

}
