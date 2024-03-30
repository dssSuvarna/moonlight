package com.builderbackend.app.repositories;

import java.util.List;
import com.builderbackend.app.models.DocumentSharing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentSharingRepository extends JpaRepository<DocumentSharing, String> {

    List<DocumentSharing> findByProjectProjectId(String projectId);

}
