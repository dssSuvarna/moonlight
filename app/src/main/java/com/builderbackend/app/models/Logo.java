package com.builderbackend.app.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import java.util.List;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Logo {

    @Id 
    String logoId;

    //Which logo is it? i.e. login page, sidebar, or email signature 
    String logoType;

    @ManyToOne
    private Business business;


   



}
