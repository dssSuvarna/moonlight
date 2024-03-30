package com.builderbackend.app.repositories;

import java.util.List;
import com.builderbackend.app.models.ProjectUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectUpdateRepository extends JpaRepository<ProjectUpdate, String> {

    List<ProjectUpdate> findByUserUserId(String userId);

    List<ProjectUpdate> findByProjectProjectId(String projectId);

    // TODO: Add custom query method when editing update
}
