package com.builderbackend.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/*
 * 
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private String userName; // this will need to be globally unique across all users and buisness
    private String email;
    private String password;
    private String phoneNumber;
    private String role; // should eventually make this an enum
    @ManyToOne()
    private Business business;
}
