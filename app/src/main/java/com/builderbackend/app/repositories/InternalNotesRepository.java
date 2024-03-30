package com.builderbackend.app.repositories;

import java.util.List;
import com.builderbackend.app.models.InternalNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalNotesRepository extends JpaRepository<InternalNotes, String> {

    List<InternalNotes> findByUserUserId(String userId);

    List<InternalNotes> findByProjectProjectId(String projectId);

    // TODO: Add custom query method when editing update
}