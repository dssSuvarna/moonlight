package com.builderbackend.app.clientPortal.services;

import lombok.Data;
import java.lang.Exception;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.builderbackend.app.models.ProjectUpdate;
import com.builderbackend.app.models.FileInfo;
import com.builderbackend.app.dtos.FileInfoDTO;
import com.builderbackend.app.dtos.ProjectUpdateDTO;
import com.builderbackend.app.repositories.ProjectUpdateRepository;
import com.builderbackend.app.mappers.FileInfoMapper;
import com.builderbackend.app.mappers.ProjectUpdateMapper;
import com.builderbackend.app.enums.EventType;

import com.builderbackend.app.services.FileUploadService;
import com.builderbackend.app.repositories.FileInfoRepository;

import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Data
public class ClientProjectUpdateService {

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
    ClientNotificationService notificationService;

    @Transactional
    public List<ProjectUpdateDTO> getProjectUpdatesForProjectId(String projectId) throws Exception {
        try {

            List<ProjectUpdateDTO> projectUpdateDTOList = new ArrayList<>();

            List<ProjectUpdate> projectUpdateList = projectUpdateRepository.findByProjectProjectId(projectId);

            if (projectUpdateList.size() > 0) {
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
                notificationService.modifyNotificationViewedToTrue(projectId, EventType.EVENT_TYPE_5);
            }
            return projectUpdateDTOList;

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            throw e;
        }
    }

}
