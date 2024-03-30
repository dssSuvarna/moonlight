
package com.builderbackend.app.repositories;

import java.util.List;
import com.builderbackend.app.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    // List<InternalNotes> findByUserUserId(String userId);

    // TODO: Add custom query method when editing update
    List<Project> findByClientUserId(String clientId);

    Project findByProjectId(String projectId);

}