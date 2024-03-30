package com.builderbackend.app.clientPortal.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.NotificationDTO;
import com.builderbackend.app.clientPortal.services.ClientNotificationService;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("client-notification")
public class ClientNotificationsController {
    private final ClientNotificationService notificationService;

    @Autowired
    public ClientNotificationsController(ClientNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // @GetMapping("/get-notifications-for-business/{businessId}")
    // public String getNotificationsForBusiness(@PathVariable String businessId) {
    // List<NotificationDTO> notificationDTOList;
    // try {
    // notificationDTOList =
    // notificationService.getAllNotificationForBusiness(businessId);
    // return convertToJson(notificationDTOList);

    // } catch (Exception e) {
    // return e.getMessage(); // need better error handling.
    // }

    // }

    @GetMapping("/get-notifications-for-project/{projectId}")
    public String getNotificationsForProject(@PathVariable String projectId) {
        List<NotificationDTO> notificationDTOList;
        try {
            notificationDTOList = notificationService.getNotificationsForProject(projectId);
            return convertToJson(notificationDTOList);

        } catch (Exception e) {
            return e.getMessage(); // need better error handling.
        }

    }

    private String convertToJson(Object object) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
        return objectMapper.writeValueAsString(object);
    }
}
