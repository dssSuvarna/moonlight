package com.builderbackend.app.models;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Data
@Entity
public class InternalNotesAttachment {

    @Id
    String internalNotesAttachmentId;

    private String mimeType;

    private String base64Data;

    @ManyToOne
    InternalNotes internalNotes;
}