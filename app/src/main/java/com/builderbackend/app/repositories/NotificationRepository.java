package com.builderbackend.app.repositories;

import com.builderbackend.app.models.Notification;
import com.builderbackend.app.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

        List<Notification> findByBusinessBusinessId(String businessId);

        List<Notification> findByProjectProjectId(String projectId);

        // @Query("SELECT n FROM Notification n JOIN n.receiver u WHERE
        // n.business.businessId = :businessId AND u.role = 'employee' AND n.viewed =
        // false")
        @Query("SELECT n FROM Notification n JOIN n.receiver u WHERE n.business.businessId = :businessId AND u.role IN ('employee', 'employee_admin') AND n.viewed = false")
        List<Notification> findUnviewedEmployeeNotificationsByBusinessId(@Param("businessId") String businessId);

        // @Query("SELECT n FROM Notification n JOIN n.receiver u WHERE
        // n.project.projectId = :projectId AND n.viewed = false AND u.role =
        // 'employee'")
        @Query("SELECT n FROM Notification n JOIN n.receiver u WHERE n.project.projectId = :projectId AND n.receiver.userId = :userId AND n.viewed = false AND u.role IN ('employee', 'employee_admin')")
        List<Notification> findUnviewedNotificationsByProjectIdAndUserRoleAndReceiverUserId(
                        @Param("projectId") String projectId, @Param("userId") String userId);

        // @Query("SELECT n FROM Notification n JOIN n.receiver u WHERE
        // n.project.Owner.userId = :userId AND n.viewed = false AND u.role =
        // 'employee'")
        @Query("SELECT n FROM Notification n JOIN n.receiver u WHERE u.userId = :userId AND n.viewed = false AND u.role IN ('employee', 'employee_admin')")
        List<Notification> findUnviewedNotificationsByUserIdAndUserRole(@Param("userId") String projectId);

        @Query("SELECT n FROM Notification n JOIN n.receiver u WHERE n.business.businessId = :businessId AND u.role = 'client' AND n.viewed = false")
        List<Notification> findUnviewedClientNotificationsByBusinessId(@Param("businessId") String businessId);

        @Query("SELECT n FROM Notification n JOIN n.receiver u WHERE n.project.projectId = :projectId AND n.viewed = false AND u.role = 'client'")
        List<Notification> findUnviewedClientNotificationsByProjectIdAndUserRole(@Param("projectId") String projectId);

        List<Notification> findByProjectProjectIdAndEventType(String projectId, EventType eventType);

        List<Notification> findByProjectProjectIdAndEventTypeAndReceiverUserId(String projectId, EventType eventType,
                        String userId);

        List<Notification> findBySentFalseAndViewedFalseAndTimeStampBefore(Instant timeStamp);

        @Modifying
        @Query("UPDATE Notification n SET n.viewed = true WHERE n.project.projectId = :projectId AND n.eventType = :eventType")
        int updateViewedStatusToTrue(@Param("projectId") String projectId, @Param("eventType") EventType eventType);

        @Modifying
        @Query("UPDATE Notification n SET n.viewed = true WHERE n.project.projectId = :projectId AND n.receiver.userId = :userId AND n.eventType = :eventType")
        int updateViewedStatusToTrueForUserId(@Param("projectId") String projectId,
                        @Param("userId") String userId, @Param("eventType") EventType eventType);

        @Modifying
        @Query("UPDATE Notification n SET n.viewed = true WHERE n.project.projectId = :projectId AND n.eventId = :eventId AND n.eventType = :eventType")
        int updateViewedStatusToTrueForDocId(@Param("projectId") String projectId, @Param("eventId") String eventId, @Param("eventType") EventType eventType);

        @Modifying
        @Query("UPDATE Notification n SET n.sent = true WHERE n.project.projectId = :projectId AND n.eventType = :eventType ")
        int updateSentStatusToTrue(@Param("projectId") String projectId, @Param("eventType") EventType eventType);

        @Modifying
        @Query("DELETE FROM Notification n WHERE n.project.projectId = :projectId AND n.eventId = :eventId AND n.eventType = :eventType")
        int deleteNotificationForDocSharing(@Param("projectId") String projectId, @Param("eventId") String eventId, @Param("eventType") EventType eventType);
}
