package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.NotificationDTO;
import com.builderbackend.app.models.Notification;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.ProjectRepository;
import com.builderbackend.app.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationMapper {

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    public NotificationDTO convert_Notification_to_NotificationDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setNotificationId(notification.getNotificationId());
        notificationDTO.setBusinessId(notification.getBusiness().getBusinessId());
        notificationDTO.setProjectId(notification.getProject().getProjectId());
        notificationDTO.setSenderUserId(notification.getSender().getUserId());
        notificationDTO.setReceiverUserId(notification.getReceiver().getUserId());
        notificationDTO.setEventId(notification.getEventId());
        notificationDTO.setEventType(notification.getEventType());
        notificationDTO.setTimeStamp(notification.getTimeStamp());
        notificationDTO.setViewed(notification.getViewed());
        notificationDTO.setSenderFirstName(notification.getSender().getFirstName());
        notificationDTO.setSenderLastName(notification.getSender().getLastName());

        return notificationDTO;
    }

    public Notification convert_NotificationDTO_to_Notification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();

        notification.setNotificationId(notificationDTO.getNotificationId());
        notification.setBusiness(businessRepository.findByBusinessId(notificationDTO.getBusinessId()));
        notification.setSender(userRepository.findByUserId(notificationDTO.getSenderUserId()));
        notification.setReceiver(userRepository.findByUserId(notificationDTO.getReceiverUserId()));
        notification.setProject(projectRepository.findByProjectId(notificationDTO.getProjectId()));
        notification.setEventId(notificationDTO.getEventId());
        notification.setEventType(notificationDTO.getEventType());
        notification.setTimeStamp(notificationDTO.getTimeStamp());
        notification.setViewed(notificationDTO.getViewed());
        return notification;
    }

}
