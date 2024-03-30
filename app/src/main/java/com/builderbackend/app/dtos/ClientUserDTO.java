package com.builderbackend.app.dtos;

import lombok.Data;

@Data
public class ClientUserDTO {

    String userId;
    String firstName;
    String lastName;
    String userName; // this will need to be globally unique across all users and buisness
    String email;
    String phoneNumber;
    String role; // should eventually make this an enum
    String businessId;
}
