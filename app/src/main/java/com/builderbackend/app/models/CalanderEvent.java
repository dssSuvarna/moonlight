package com.builderbackend.app.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
public class CalanderEvent {

    @Id
    String eventId;

    String title;

    Instant start;
    Instant end;

    Boolean allDay;

    @ManyToOne
    private User employee;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Business business;

}
