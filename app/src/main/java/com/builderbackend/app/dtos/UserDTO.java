package com.builderbackend.app.dtos;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;

@Data
public class UserDTO {

    String userId;

    @NotEmpty
    String firstName;

    @NotEmpty
    String lastName;

    @NotEmpty
    String userName; // this will need to be globally unique across all users and buisness

    @NotEmpty
    @Email(message = "Email Not Valid")
    String email;

    String password;

    @NotEmpty
    String phoneNumber;

    String role; // should eventually make this an enum

    @NotEmpty
    String businessId;
}
