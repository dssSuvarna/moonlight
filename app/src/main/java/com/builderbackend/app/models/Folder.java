package com.builderbackend.app.models;

import java.util.ArrayList;
import java.util.List;
import java.time.Instant;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "folder")
public class Folder {

    @Id
    @Column(name = "folder_id")
    private String folderId;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "folder_name")
    private String folderName;

    @Column(name = "created_date", columnDefinition = "TIMESTAMP")
    private Instant createdDate;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;

    @Column(name = "path")
    private String path; // URL path to the folder NOT including its own name

    @OneToMany(mappedBy = "parentFolder")
    private List<Folder> subFolders = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<DocumentSharing> documents = new ArrayList<>();
}