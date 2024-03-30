package com.builderbackend.app.dtos;

import java.time.Instant;
import lombok.Data;

@Data
public class DocumentSharingDTO {

    String documentSharingId;
    String userOwnerId; // Link the update to a User who is uploading the file
    String businessId; // Link the update to a Business
    String projectId;
    String fileName;
    FileInfoDTO fileInfoDTO;
    String folderId; // specifies which folder this file is in
    private Instant createdDate;

}
