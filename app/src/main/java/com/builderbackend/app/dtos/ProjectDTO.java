package com.builderbackend.app.dtos;

import lombok.Data;

@Data
public class ProjectDTO {

    String projectId;
    String address;
    String startDate;
    String endDate;
    String description;
    String clientId;
    String ownerId;
    String businessId;
}
