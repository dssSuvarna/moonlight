package com.builderbackend.app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.NotificationDTO;
import com.builderbackend.app.services.NotificationService;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("notification")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/get-notifications-for-business/{businessId}")
    public String getNotificationsForBusiness(@PathVariable String businessId) {
        List<NotificationDTO> notificationDTOList;
        try {
            notificationDTOList = notificationService.getAllNotificationForBusiness(businessId);
            return convertToJson(notificationDTOList);

        } catch (Exception e) {
            return e.getMessage(); // need better error handling.
        }

    }

    @GetMapping("/get-notifications-for-project/{projectId}/{employeeUserId}")
    public String getNotificationsForProject(@PathVariable String projectId, @PathVariable String employeeUserId) {
        List<NotificationDTO> notificationDTOList;
        try {
            notificationDTOList = notificationService.getNotificationsForProject(projectId, employeeUserId);
            return convertToJson(notificationDTOList);

        } catch (Exception e) {
            return e.getMessage(); // need better error handling.
        }

    }

    @GetMapping("/get-notifications-for-user/{userId}")
    public String getNotificationsForUser(@PathVariable String userId) {
        List<NotificationDTO> notificationDTOList;
        try {
            notificationDTOList = notificationService.getNotificationsForUserId(userId);
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
