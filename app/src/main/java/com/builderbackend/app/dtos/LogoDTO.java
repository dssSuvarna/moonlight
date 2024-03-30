package com.builderbackend.app.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LogoDTO {

    String logoId;

    String businessId; // Link the update to a Business 

    String logoType;
 
    //url for image
    FileInfoDTO fileInfoDTO;


}