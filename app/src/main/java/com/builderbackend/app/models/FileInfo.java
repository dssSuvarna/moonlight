package com.builderbackend.app.models;

import org.hibernate.annotations.ManyToAny;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

//@AllArgsConstructor
@Data
@Entity
public class FileInfo {

    @Id
    private String fileId;

    private String fileUrl;

    private String featureId;


}
