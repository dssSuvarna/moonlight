package com.builderbackend.app.dtos;

import lombok.Data;

@Data
public class BusinessDTO {
    String businessId;
    String businessName;
    String email;
    String phoneNumber;
    String subscriptionType;
}
