package com.builderbackend.app.dtos;

import lombok.Data;
import java.time.Instant;
import com.builderbackend.app.enums.EventType;

@Data
public class NotificationDTO {
    String notificationId;
    String businessId;
    String projectId;
    String senderUserId;
    String receiverUserId;
    String senderFirstName;
    String senderLastName;
    // eventId maps to the event that took place (example: projectUpdateId,
    // todolistId, etc)
    String eventId;
    // eventType is what type of event (ex: new message, add doc, etc)
    EventType eventType;
    // The columnDefinition attribute allows us to define the SQL DDL (Data
    // Definition Language)
    // specifying "TIMESTAMP" ensures that the column is created as a TIMESTAMP type
    // in the database.
    Instant timeStamp;
    // Boolean indication if client has viewed the event default = false
    Boolean viewed;
}
