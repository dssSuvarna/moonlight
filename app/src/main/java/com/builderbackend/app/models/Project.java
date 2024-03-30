package com.builderbackend.app.models;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Data
@Entity
public class Project {

    @Id
    String projectId;
    String address;
    String startDate;
    String endDate;
    String description;

    @ManyToOne
    private User client;

    @ManyToOne
    private User Owner;

    @ManyToOne
    private Business business;

}
