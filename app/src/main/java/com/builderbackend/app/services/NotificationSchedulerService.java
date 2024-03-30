package com.builderbackend.app.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.builderbackend.app.repositories.NotificationRepository;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.models.EmailDetails;
import com.builderbackend.app.models.Notification;
import com.builderbackend.app.models.User;
import com.builderbackend.app.models.Business;
import com.builderbackend.app.services.LogoService;



import com.builderbackend.app.dtos.LogoDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Service
@Data
public class NotificationSchedulerService {
    private static final Logger log = LoggerFactory.getLogger(NotificationSchedulerService.class);

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final LogoService logoService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Value("${login.path}")
    private String loginPath;

    @Scheduled(fixedRate =  900000) // 900,000 milliseconds = 15 minutes

    public void sendClientNotifications() {
        System.out.println("Scheduled task is running");

        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);

        List<Notification> notifications = notificationRepository
                .findBySentFalseAndViewedFalseAndTimeStampBefore(oneHourAgo);

        if (notifications.size() > 0) {
            Map<String, List<Notification>> batchedNotifications = new HashMap<>();

            for (Notification notification : notifications) {
                batchedNotifications
                        .computeIfAbsent(notification.getReceiver().getUserId(), k -> new ArrayList<>())
                        .add(notification);
            }

            // Now send emails for each batch
            for (Map.Entry<String, List<Notification>> entry : batchedNotifications.entrySet()) {
                String userId = entry.getKey();
                List<Notification> userNotifications = entry.getValue();
                // Construct the email body here using userNotifications details

                // This checks if the recipient is the employee or the client
                // If the recipient is a client than that means the BUSINESS is sending the
                // email
                // otherwise the client is sending the email

                // if recipeient is client than we need business info
                EmailDetails emailDetails = new EmailDetails();

                String role = getRolebyId(userId);

                if ("client".equals(role)) {

                    String businessName = getBusinessNameById(userId);
                    String emailBody = "You have new notifications from " + businessName;

                    emailDetails.setRecipient(getUserEmailById(userId));
                    emailDetails.setMsgBody(emailBody);
                    emailDetails.setSubject(
                            "New Notifications From " + businessName + " On Your Moonlight Connect Account");
                }
                // if recipeient is employee than we need client info

                else if ("employee".equals(role) || "employee_admin".equals(role)) {

                    String clientName = getClientNamebyNotificaton(userNotifications.get(0));
                    String emailBody = "You have new notifications from "
                            + clientName;

                    emailDetails.setRecipient(getUserEmailById(userId));
                    emailDetails.setMsgBody(emailBody);
                    emailDetails.setSubject(
                            "New Notifications From " + clientName
                                    + " On Your Moonlight Connect Account");
                }

                // Send the email
                Business business = getBusinesByUserId(userId);
     
                String url = loginPath + "?token=" + business.getBusinessId();
                

                List<LogoDTO> logos = logoService.getLogosForBusinessId(business.getBusinessId());
                String imageUrl = null;

                for (LogoDTO logo : logos){
                    if ("emailLogo".equals(logo.getLogoType())) {
                        imageUrl = logo.getFileInfoDTO().getFileUrl();
                        break; 
                    }
                }
                String response = emailService.sendHtmlMail(emailDetails, url, imageUrl);

                if (response == "Mail Sent Successfully...") {
                    // updating notification boolean sent as true
                    for (Notification notification : userNotifications) {

                        notificationService.updateNotificationsAsSent(notification.getProject().getProjectId(),
                                notification.getEventType());
                    }
                }
            }
        }

    }

    public String getUserEmailById(String userId) {
        // Fetch user by ID and then extract email if present
        User user = userRepository.findByUserId(userId);

        String email = user.getEmail();

        return email;

    }

    public String getBusinessNameById(String userId) {
        // Fetch user by ID and then extract business name if present
        Optional<User> userOpt = userRepository.findById(userId);
        // Returns business name or null if user is not found
        return userOpt.map(user -> user.getBusiness().getBusinessName()).orElse(null);
    }

    public Business getBusinesByUserId(String userId) {
        // Fetch user by ID and then extract business name if present
        Optional<User> userOpt = userRepository.findById(userId);
        // Returns business name or null if user is not found
        return userOpt.map(user -> user.getBusiness()).orElse(null);
    }

    public String getRolebyId(String userId) {

        return userRepository.findByUserId(userId).getRole();
        // Optional<User> userOpt = userRepository.findById(userId);
        // return userOpt.map(user -> user.getRole()).orElse(null);
    }

    public String getClientNamebyNotificaton(Notification userNotifications) {
        Optional<Notification> notificationOpt = notificationRepository.findById(userNotifications.getNotificationId());
        return notificationOpt
                .map(notification -> notification.getSender().getFirstName() + notification.getSender().getLastName())
                .orElse(null);
    }

}
