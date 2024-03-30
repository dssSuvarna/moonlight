package com.builderbackend.app.repositories;

import java.time.Instant;
import java.util.List;
import com.builderbackend.app.models.CalanderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalanderEventRepository extends JpaRepository<CalanderEvent, String> {

    List<CalanderEvent> findByProjectProjectId(String projectId);

    List<CalanderEvent> findByEmployeeUserId(String userId);

    List<CalanderEvent> findByEmployeeUserIdAndStartBetween(String userId, Instant startOfDay, Instant endOfDay);

}
