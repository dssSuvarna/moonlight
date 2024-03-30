package com.builderbackend.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import jakarta.persistence.*;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Business {

    @Id
    private String businessId;
    private String businessName;
    private String email;
    private String phoneNumber;
    private String subscriptionType;

    @OneToMany(mappedBy = "business",orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Logo> logos;
}