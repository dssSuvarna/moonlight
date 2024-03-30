package com.builderbackend.app.services;

import lombok.Data;
import java.lang.Exception;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.builderbackend.app.models.ProjectUpdate;
import com.builderbackend.app.models.FileInfo;
import com.builderbackend.app.dtos.FileInfoDTO;
import com.builderbackend.app.dtos.ProjectUpdateDTO;
import com.builderbackend.app.repositories.ProjectUpdateRepository;
import com.builderbackend.app.mappers.FileInfoMapper;
import com.builderbackend.app.mappers.ProjectUpdateMapper;
import com.builderbackend.app.enums.EventType;

import com.builderbackend.app.repositories.FileInfoRepository;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.nio.file.*;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Data
public class ProjectUpdateService {

    @Autowired
    ProjectUpdateRepository projectUpdateRepository;

    @Autowired
    FileInfoRepository fileInfoRepository;

    @Autowired
    ProjectUpdateMapper projectUpdateMapper;

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    NotificationService notificationService;

    // Error handling and throwing exceptions can be added later
    public ProjectUpdateDTO createProjectUpdate(ProjectUpdateDTO projectUpdateDTO, List<MultipartFile> files) {

        // Setting UUID for Update
        String projectUpdateId = UUID.randomUUID().toString();
        projectUpdateDTO.setProjectUpdateId(projectUpdateId);

        // gets all files for the project update
        List<FileInfo> existingFileInfoList = fileInfoRepository.findByFeatureId(projectUpdateId);

        // converts all files entitys to fileInfoDTOs
        List<FileInfoDTO> existingFileInfoDTOList = new ArrayList<>();
        for (FileInfo fileInfo : existingFileInfoList) {
            FileInfoDTO existingFileInfoDTO = fileInfoMapper.convert_FileInfo_to_FileInfoDTO(fileInfo);
            existingFileInfoDTOList.add(existingFileInfoDTO);
        }

        // save each image
        if (files != null) {
            for (MultipartFile file : files) {

                FileInfoDTO newFileInfoDTO = new FileInfoDTO();
                String fileId = UUID.randomUUID().toString();

                newFileInfoDTO.setFileId(fileId);
                newFileInfoDTO.setFileUrl(
                        String.format("uploads/%s/%s/%s/%s", projectUpdateDTO.getBusinessId(),
                                projectUpdateDTO.getProjectId(), "projectUpdates",
                                newFileInfoDTO.getFileId()));

                newFileInfoDTO.setFeatureId(projectUpdateId);

                FileInfo newfileInfo = fileInfoMapper.convert_FileInfoDTO_to_FileInfo(newFileInfoDTO);
                fileInfoRepository.save(newfileInfo);

                // Get the existing list
                // projectUpdateDTO.getFileInfoDTOs();

                // // Check if the existing list is null. If so, initialize it
                // if (existingFileInfoList == null) {
                // List<FileInfoDTO> existingFileInfoDTOList = new ArrayList<>();
                // } else {
                // for (FileInfo fileEntity : existingFileInfoList)

                // {
                // FileInfoDTO existingFileInfoDTOList =
                // fileInfoMapper.convert_FileInfo_to_FileInfoDTO(file);
                // }
                // }

                // Append new item to the list
                existingFileInfoDTOList.add(newFileInfoDTO);

                // 3. Set the updated list back (this step might not be needed if the list was
                // already initialized,
                // as Java lists are reference types and modifying the list modifies the
                // original reference in projectUpdateDTO)
                projectUpdateDTO.setFileInfoDTOs(existingFileInfoDTOList);

                fileUploadService.save(file, newFileInfoDTO.getFileUrl());

            }
        }

        // FileUploadController.
        // Convert DTO to Entity
        ProjectUpdate updateEntity = projectUpdateMapper.convert_ProjectUpdateDTO_to_ProjectUpdate(projectUpdateDTO);

        // Save to the database
        projectUpdateRepository.save(updateEntity);

        // create notification for project update
        notificationService.createNotification(projectUpdateDTO.getProjectId(), projectUpdateId,
                EventType.EVENT_TYPE_5);
        return projectUpdateDTO;
    }

    @Transactional
    public List<ProjectUpdateDTO> getProjectUpdatesForProjectId(String projectId) throws Exception {
        try {

            List<ProjectUpdateDTO> projectUpdateDTOList = new ArrayList<>();

            List<ProjectUpdate> projectUpdateList = projectUpdateRepository.findByProjectProjectId(projectId);

            if (projectUpdateList.size() > 0) {
                String role = projectUpdateList.get(0).getUser().getRole();
                for (ProjectUpdate projectUpdate : projectUpdateList) {
                    ProjectUpdateDTO projectUpdateDTO = projectUpdateMapper
                            .convert_ProjectUpdate_to_ProjectUpdateDTO(projectUpdate);

                    List<FileInfo> fileInfo = fileInfoRepository.findByFeatureId(projectUpdate.getProjectUpdateId());
                    List<FileInfoDTO> fileInfoDTOs = new ArrayList<>();
                    for (FileInfo file : fileInfo) {
                        FileInfoDTO fileDto = (fileInfoMapper.convert_FileInfo_to_FileInfoDTO(file));
                        fileInfoDTOs.add(fileDto);

                    }

                    projectUpdateDTO.setFileInfoDTOs(fileInfoDTOs);
                    projectUpdateDTOList.add(projectUpdateDTO);

                }
            }
            return projectUpdateDTOList;

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            throw e;
        }
    }

    public ProjectUpdateDTO modifyProjectUpdate(ProjectUpdateDTO projectUpdateDTO, List<MultipartFile> files) {

        // save each image

        // get saved project update to modify
        String projectUpdateId = projectUpdateDTO.getProjectUpdateId();

        ProjectUpdate projectUpdate = projectUpdateRepository.findById(projectUpdateId).orElse(null);
        projectUpdateDTO.setDate(projectUpdate.getDate());

        ProjectUpdateDTO currentProjectUpdateDTO = projectUpdateMapper
                .convert_ProjectUpdate_to_ProjectUpdateDTO(projectUpdate);

        projectUpdateDTO.setProjectUpdateId(projectUpdateId);

        // gets all files for the project update
        List<FileInfo> existingFileInfoList = fileInfoRepository.findByFeatureId(projectUpdateId);

        // converts all files entitys to fileInfoDTOs
        List<FileInfoDTO> existingFileInfoDTOList = new ArrayList<>();
        for (FileInfo fileInfo : existingFileInfoList) {
            FileInfoDTO existingFileInfoDTO = fileInfoMapper.convert_FileInfo_to_FileInfoDTO(fileInfo);
            existingFileInfoDTOList.add(existingFileInfoDTO);
        }

        if (files != null) {
            for (MultipartFile file : files) {

                FileInfoDTO newFileInfoDTO = new FileInfoDTO();
                String fileId = UUID.randomUUID().toString();

                newFileInfoDTO.setFileId(fileId);
                newFileInfoDTO.setFileUrl(
                        String.format("uploads/%s/%s/%s/%s", projectUpdateDTO.getBusinessId(),
                                projectUpdateDTO.getProjectId(), "projectUpdates",
                                newFileInfoDTO.getFileId()));
                newFileInfoDTO.setFeatureId(projectUpdateId);

                FileInfo newfileInfo = fileInfoMapper.convert_FileInfoDTO_to_FileInfo(newFileInfoDTO);
                fileInfoRepository.save(newfileInfo);

                // Get the existing list
                // List<FileInfo> existingFileInfoList =
                // fileInfoRepository.findByFeatureId(projectUpdateId);
                // List<FileInfoDTO> existingFileInfoDTOList =
                // fileInfoMapper.convert_FileInfo_to_FileInfoDTO(existingFileInfoList)
                // projectUpdateDTO.getFileInfoDTOs();

                // Append new item to the list
                existingFileInfoDTOList.add(newFileInfoDTO);

                // 3. Set the updated list back (this step might not be needed if the list was
                // already initialized,
                // as Java lists are reference types and modifying the list modifies the
                // original reference in projectUpdateDTO)
                projectUpdateDTO.setFileInfoDTOs(existingFileInfoDTOList);

                fileUploadService.save(file, newFileInfoDTO.getFileUrl());

            }
        }
        // Convert DTO to Entity
        ProjectUpdate updateEntity = projectUpdateMapper
                .convert_ProjectUpdateDTO_to_ProjectUpdate(projectUpdateDTO);

        // Save to the database
        projectUpdateRepository.save(updateEntity);

        // createProjectUpdate(projectUpdateDTO, files);
        return projectUpdateDTO;
    }

    public void deleteProjectUpdate(String projectUpdateId) {
        Optional<ProjectUpdate> projectUpdateOptional = projectUpdateRepository.findById(projectUpdateId);

        // Delete all the files first
        if (projectUpdateOptional.isPresent()) {
            ProjectUpdate projectUpdate = projectUpdateOptional.get();

            List<FileInfo> fileInfoList = fileInfoRepository.findByFeatureId(projectUpdateId);
            for (FileInfo fileInfo : fileInfoList) {
                // Delete the actual file
                deleteFileWithPermissions(fileInfo.getFileUrl());

                // Delete the fileInfo record
                fileInfoRepository.delete(fileInfo);
            }

        } else {
            throw new RuntimeException("ProjectUpdate not found for id: " + projectUpdateId);
        }

        // delete the project update itself
        projectUpdateRepository.deleteById(projectUpdateId);
    }

    public void deleteFileInfo(String fileInfoId) {
        // Find the project update first
        Optional<FileInfo> FileInfoOptional = fileInfoRepository.findById(fileInfoId);

        if (FileInfoOptional.isPresent()) {
            FileInfo fileInfo = FileInfoOptional.get();
            // Delete the actual file
            deleteFileWithPermissions(fileInfo.getFileUrl());

            // Delete the fileInfo record
            fileInfoRepository.delete(fileInfo);

        } else {
            throw new RuntimeException("ProjectUpdate not found for id: " + fileInfoId);
        }
    }

    private void deleteFileWithPermissions(String fileUrl) {
        Path pathToFile = Paths.get(fileUrl);
        if (Files.isWritable(pathToFile)) {
            try {
                Files.deleteIfExists(pathToFile);
            } catch (IOException e) {
                throw new RuntimeException("Error deleting the file: " + fileUrl, e);
            }
        } else {
            // this is temp not a good practice to sudo rm tbh so lets find a better way.
            // one thing is when saving a file make sure we set it the proper permission so
            // we dont have to sudo rm
            // the file is not writable
            // You can attempt to change permissions or delete with elevated permissions.
            // This is platform dependent and risky. Not recommended for server
            // applications.
            // For example, on UNIX systems:
            try {
                Process process = Runtime.getRuntime().exec("sudo rm " + fileUrl);
                process.waitFor();
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete file with elevated permissions: " + fileUrl, e);
            }
        }
    }

}
