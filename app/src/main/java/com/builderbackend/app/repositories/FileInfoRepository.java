package com.builderbackend.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.builderbackend.app.models.FileInfo;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, String> {
    // void deleteByProjectUpdate(ProjectUpdate projectUpdate);

    List<FileInfo> findByFeatureId(String featureId);

}