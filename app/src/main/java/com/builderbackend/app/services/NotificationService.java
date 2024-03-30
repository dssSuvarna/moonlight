package com.builderbackend.app.services;

import lombok.Data;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.builderbackend.app.models.Notification;
import com.builderbackend.app.dtos.NotificationDTO;
import com.builderbackend.app.repositories.NotificationRepository;
import com.builderbackend.app.mappers.NotificationMapper;
import com.builderbackend.app.enums.EventType;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.repositories.ProjectRepository;

import java.time.Instant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Data
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    NotificationMapper notificationMapper;

    public NotificationDTO createNotification(String projectId, String eventId, EventType eventType) {
        Notification notification = new Notification();
        NotificationDTO notificationDTO = new NotificationDTO();

        String notificationId = UUID.randomUUID().toString();
        notification.setNotificationId(notificationId);

        notification.setProject(projectRepository.findByProjectId(projectId));

        notification.setSender(notification.getProject().getOwner());
        notification.setReceiver(notification.getProject().getClient());

        notification.setBusiness(notification.getProject().getBusiness());

        notification.setEventId(eventId);
        notification.setEventType(eventType);

        // Gets current time in UTC
        notification.setTimeStamp(Instant.now());

        // By default viewed and sent are false when first created
        notification.setViewed(false);
        notification.setSent(false);

        notificationRepository.save(notification);

        notificationDTO = notificationMapper.convert_Notification_to_NotificationDTO(notification);

        return notificationDTO;
    }

    public NotificationDTO createMessageNotification(String projectId, String userId, String eventId,
            EventType eventType) {
        Notification notification = new Notification();
        NotificationDTO notificationDTO = new NotificationDTO();

        String notificationId = UUID.randomUUID().toString();
        notification.setNotificationId(notificationId);

        notification.setProject(projectRepository.findByProjectId(projectId));

        notification.setSender(userRepository.findByUserId(userId));
        notification.setReceiver(notification.getProject().getClient());

        notification.setBusiness(notification.getProject().getBusiness());

        notification.setEventId(eventId);
        notification.setEventType(eventType);

        // Gets current time in UTC
        notification.setTimeStamp(Instant.now());

        // By default viewed and sent are false when first created
        notification.setViewed(false);
        notification.setSent(false);

        notificationRepository.save(notification);

        notificationDTO = notificationMapper.convert_Notification_to_NotificationDTO(notification);

        return notificationDTO;
    }

    public List<NotificationDTO> getAllNotificationForBusiness(String businessId) {
        List<NotificationDTO> notificationDTOList = new ArrayList<NotificationDTO>();

        List<Notification> notificationList = new ArrayList<Notification>();

        notificationList = notificationRepository.findUnviewedEmployeeNotificationsByBusinessId(businessId);

        NotificationDTO notificationDTO = new NotificationDTO();
        for (Notification notification : notificationList) {
            notificationDTO = notificationMapper.convert_Notification_to_NotificationDTO(notification);
            notificationDTOList.add(notificationDTO);
        }

        return notificationDTOList;

    }

    public List<NotificationDTO> getNotificationsForProject(String projectId, String employeeUserId) {
        List<NotificationDTO> notificationDTOList = new ArrayList<NotificationDTO>();

        List<Notification> notificationList = new ArrayList<Notification>();

        notificationList = notificationRepository
                .findUnviewedNotificationsByProjectIdAndUserRoleAndReceiverUserId(projectId, employeeUserId);

        NotificationDTO notificationDTO = new NotificationDTO();
        for (Notification notification : notificationList) {
            notificationDTO = notificationMapper.convert_Notification_to_NotificationDTO(notification);
            notificationDTOList.add(notificationDTO);
        }

        return notificationDTOList;
    }

    public List<NotificationDTO> getNotificationsForUserId(String userId) {
        List<NotificationDTO> notificationDTOList = new ArrayList<NotificationDTO>();

        List<Notification> notificationList = new ArrayList<Notification>();

        notificationList = notificationRepository.findUnviewedNotificationsByUserIdAndUserRole(userId);

        NotificationDTO notificationDTO = new NotificationDTO();
        for (Notification notification : notificationList) {
            notificationDTO = notificationMapper.convert_Notification_to_NotificationDTO(notification);
            notificationDTOList.add(notificationDTO);
        }

        return notificationDTOList;
    }

    @Transactional
    public void modifyNotificationViewedToTrue(String projectId, EventType eventType) {
        notificationRepository.updateViewedStatusToTrue(projectId, eventType);

    }

    @Transactional
    public void modifyNotificationViewedToTrueForUserId(String projectId, String userId, EventType eventType) {
        notificationRepository.updateViewedStatusToTrueForUserId(projectId, userId, eventType);

    }

    @Transactional
    public void updateNotificationsAsSent(String projectId, EventType eventType) {
        notificationRepository.updateSentStatusToTrue(projectId, eventType);

    }

    @Transactional
    public void modifyNotificationViewedToTrueForDocId(String projectId, String documentSharingId, EventType eventType) {
        notificationRepository.updateViewedStatusToTrueForDocId(projectId, documentSharingId, eventType);
    }

    @Transactional
    public void deleteNotificationForDocId(String projectId, String documentSharingId, EventType eventType) {
        notificationRepository.deleteNotificationForDocSharing(projectId, documentSharingId, eventType);
    }
}
