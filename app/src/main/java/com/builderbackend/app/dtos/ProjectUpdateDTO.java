package com.builderbackend.app.dtos;

import lombok.Data;
import java.util.List;

@Data
public class ProjectUpdateDTO {

    private String projectUpdateId;
    private String userId; // Link the update to a User (client the update is intended for)
    private String businessId; // Link the update to a Business (business who is creating/assigning the update)
    private String projectId;
    private String date;
    private String title;
    private String description;
    private List<FileInfoDTO> fileInfoDTOs;

}
