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
@NoArgsConstructor
@Entity
public class ProjectUpdate {

    @Id
    String projectUpdateId;

    String title;
    String date;

    @Column(columnDefinition = "TEXT")
    String description;

    @ManyToOne
    private User user;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Business business;

    // @OneToMany(cascade = CascadeType.ALL)
    // @JoinColumn(name = "featureId", referencedColumnName = "projectUpdateId")
    // List<FileInfo> fileInfo;

}
