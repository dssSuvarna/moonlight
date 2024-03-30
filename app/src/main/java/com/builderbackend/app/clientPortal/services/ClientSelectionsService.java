package com.builderbackend.app.clientPortal.services;

import lombok.Data;
import java.lang.Exception;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.builderbackend.app.models.Selections;
import com.builderbackend.app.models.FileInfo;
import com.builderbackend.app.dtos.FileInfoDTO;
import com.builderbackend.app.dtos.SelectionsDTO;
import com.builderbackend.app.repositories.SelectionsRepository;
import com.builderbackend.app.mappers.FileInfoMapper;
import com.builderbackend.app.mappers.SelectionsMapper;
import com.builderbackend.app.enums.EventType;

import com.builderbackend.app.services.FileUploadService;
import com.builderbackend.app.repositories.FileInfoRepository;

import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Data
public class ClientSelectionsService {

    @Autowired
    SelectionsRepository selectionsRepository;

    @Autowired
    FileInfoRepository fileInfoRepository;

    @Autowired
    SelectionsMapper selectionsMapper;

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    ClientNotificationService notificationService;

    @Transactional
    public List<SelectionsDTO> getSelectionsForProjectId(String projectId) throws Exception {
        try {

            List<SelectionsDTO> selectionsDTOList = new ArrayList<>();

            List<Selections> selectionsList = selectionsRepository.findByProjectProjectId(projectId);

            if (selectionsList.size() > 0) {
                for (Selections selections : selectionsList) {
                    SelectionsDTO selectionsDTO = selectionsMapper.convert_Selections_to_selectionsDTO(selections);

                    List<FileInfo> fileInfo = fileInfoRepository.findByFeatureId(selections.getSelectionId());
                    List<FileInfoDTO> fileInfoDTOs = new ArrayList<>();

                    if (fileInfo != null) {
                        FileInfoDTO fileDTO = (fileInfoMapper.convert_FileInfo_to_FileInfoDTO(fileInfo.get(0)));
                        selectionsDTO.setFileInfoDTO(fileDTO);
                    }

                    selectionsDTOList.add(selectionsDTO);

                }
                notificationService.modifyNotificationViewedToTrue(projectId, EventType.EVENT_TYPE_2);
            }
            return selectionsDTOList;

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            throw e;
        }
    }

    @Transactional
    public void ConfirmSelection(String selectionsId) {

        Optional<Selections> selection = selectionsRepository.findById(selectionsId);
        if (selection.isPresent()) {

            String projectId = selection.get().getProject().getProjectId();
            selectionsRepository.updateConfirmationStatus(selectionsId);
            notificationService.createNotification(projectId, selectionsId,
                    EventType.EVENT_TYPE_8);
        }

    }

}