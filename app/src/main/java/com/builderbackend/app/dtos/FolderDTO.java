package com.builderbackend.app.dtos;

import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;




import lombok.Data;

@Data
public class FolderDTO {

    private String folderId;

    @NotBlank
    private String businessId;

    @NotBlank
    private String projectId;

    @NotBlank
    private String folderName;

    @NotNull
    private Instant createdDate;

    @NotBlank
    private String createdBy; // user id of the person who create the folder

    @NotBlank
    private String parentFolderId;

    @NotBlank
    private String path; // url path to the folder NOT including its own name
    private List<SubFolderInfo> subFolders = new ArrayList<>();
    private List<DocumentSharingDTO> documents =  new ArrayList<>();

    @Data
    public static class SubFolderInfo {
        private String folderId;
        private String folderName;
        private Instant createdDate;
        private String createdBy;
    }
}