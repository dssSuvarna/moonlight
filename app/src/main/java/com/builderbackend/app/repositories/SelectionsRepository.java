
package com.builderbackend.app.repositories;

import java.util.List;
import com.builderbackend.app.models.Selections;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectionsRepository extends JpaRepository<Selections, String> {

    // List<ProjectUpdate> findByUserUserId(String userId);

    List<Selections> findByProjectProjectId(String projectId);

    @Modifying
    @Transactional
    @Query("UPDATE Selections s SET s.clientConfirmation = true WHERE s.selectionId = :selectionId")
    int updateConfirmationStatus(@Param("selectionId") String selectionId);
    // TODO: Add custom query method when editing update
}
