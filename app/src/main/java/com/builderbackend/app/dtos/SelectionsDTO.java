package com.builderbackend.app.dtos;

import lombok.Data;

@Data
public class SelectionsDTO {

    String selectionId;
    String name;
    String description;
    String partNumber;
    String cost;

    Boolean clientConfirmation;

    String userId; // Link the update to a User (client the selection is intended for)
    String businessId; // Link the update to a Business (business who is creating/assigning the
                       // selection)
    String projectId;

    private FileInfoDTO fileInfoDTO;

}
