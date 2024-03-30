package com.builderbackend.app.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
public class DocumentSharing {

    @Id
    String documentSharingId;

    String fileName;

    // who uploaded the document
    @ManyToOne
    private User owner;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Business business;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    private Instant createdDate;


}
