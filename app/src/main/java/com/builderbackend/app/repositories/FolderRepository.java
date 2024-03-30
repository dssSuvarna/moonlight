package com.builderbackend.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.builderbackend.app.models.Folder;


@Repository
public interface FolderRepository extends JpaRepository<Folder, String>{

    @Query("SELECT f FROM Folder f WHERE f.project.projectId = :projectId AND f.folderName = :folderName")
    List<Folder> findByProjectIdAndFolderName(@Param("projectId") String projectId, @Param("folderName") String folderName);

}