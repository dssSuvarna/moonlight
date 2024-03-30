package com.builderbackend.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.Instant;
import jakarta.persistence.Column;

import com.builderbackend.app.enums.EventType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification {

    @Id
    String notificationId;

    @ManyToOne
    Project project;

    @ManyToOne
    User sender;

    @ManyToOne
    User receiver;

    @ManyToOne
    Business business;

    // eventId maps to the event that took place (example: projectUpdateId,
    // todolistId, etc)
    String eventId;
    // eventType is what type of event (ex: new message, add doc, etc)
    @Enumerated(EnumType.STRING) // this stores the eventType as the eventType string ex: "EVENT_TYPE_1"
    EventType eventType;
    // The columnDefinition attribute allows us to define the SQL DDL (Data
    // Definition Language)
    // specifying "TIMESTAMP" ensures that the column is created as a TIMESTAMP type
    // in the database.
    @Column(columnDefinition = "TIMESTAMP")
    Instant timeStamp;
    // Boolean indication if client has viewed the event default = false
    Boolean viewed;
    // Boolean specifiying if notification has already been sent default = false
    Boolean sent;

}
