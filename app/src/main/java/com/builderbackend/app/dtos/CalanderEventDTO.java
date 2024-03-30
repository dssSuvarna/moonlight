package com.builderbackend.app.dtos;

import lombok.Data;
import java.time.Instant;

@Data
public class CalanderEventDTO {

    String eventId;

    String title;

    Instant start;
    Instant end;

    Boolean allDay;

    String employeeId;
    String projectId;
    String businessId;

    // public void setStart(String startString) {
    // this.start = Instant.parse(startString);
    // }

    // public void setEnd(String endString) {
    // this.end = Instant.parse(endString);
    // }

}
