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

import com.builderbackend.app.models.Selections;
import com.builderbackend.app.models.FileInfo;
import com.builderbackend.app.dtos.FileInfoDTO;
import com.builderbackend.app.dtos.SelectionsDTO;
import com.builderbackend.app.repositories.SelectionsRepository;
import com.builderbackend.app.mappers.FileInfoMapper;
import com.builderbackend.app.mappers.SelectionsMapper;
import com.builderbackend.app.enums.EventType;

import com.builderbackend.app.repositories.FileInfoRepository;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.nio.file.*;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Data
public class SelectionsService {

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
    NotificationService notificationService;

    // Error handling and throwing exceptions can be added later
    public SelectionsDTO createSelection(SelectionsDTO selectionDTO, MultipartFile file) {

        // Setting UUID for Update
        String selectionId = UUID.randomUUID().toString();
        selectionDTO.setSelectionId(selectionId);

        // save each image
        if (file != null) {

            FileInfoDTO newFileInfoDTO = new FileInfoDTO();
            String fileId = UUID.randomUUID().toString();

            newFileInfoDTO.setFileId(fileId);
            newFileInfoDTO.setFileUrl(
                    String.format("uploads/%s/%s/%s/%s", selectionDTO.getBusinessId(),
                            selectionDTO.getProjectId(), "selections",
                            newFileInfoDTO.getFileId()));

            newFileInfoDTO.setFeatureId(selectionId);

            FileInfo newfileInfo = fileInfoMapper.convert_FileInfoDTO_to_FileInfo(newFileInfoDTO);
            fileInfoRepository.save(newfileInfo);

            // 3. Set the updated list back (this step might not be needed if the list was
            // already initialized,
            // as Java lists are reference types and modifying the list modifies the
            // original reference in selectionDTO)
            selectionDTO.setFileInfoDTO(newFileInfoDTO);

            fileUploadService.save(file, newFileInfoDTO.getFileUrl());

        }

        // FileUploadController.
        // Convert DTO to Entity
        Selections selectionsEntity = selectionsMapper.convert_selectionsDTO_to_Selections(selectionDTO);

        // Save to the database
        selectionsRepository.save(selectionsEntity);

        // create notification for project update
        notificationService.createNotification(selectionDTO.getProjectId(), selectionId,
                EventType.EVENT_TYPE_2);
        return selectionDTO;
    }

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
                    // for (FileInfo file : fileInfo) {
                    // FileInfoDTO fileDto = (fileInfoMapper.convert_FileInfo_to_FileInfoDTO(file));
                    // fileInfoDTOs.add(fileDto);

                    // }

                    if (fileInfo != null) {
                        FileInfoDTO fileDTO = (fileInfoMapper.convert_FileInfo_to_FileInfoDTO(fileInfo.get(0)));
                        selectionsDTO.setFileInfoDTO(fileDTO);
                    }

                    selectionsDTOList.add(selectionsDTO);

                }
                notificationService.modifyNotificationViewedToTrue(projectId, EventType.EVENT_TYPE_8);
            }
            return selectionsDTOList;

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            throw e;
        }
    }

    public void deleteSelection(String selectionId) {
        Optional<Selections> selectionOptional = selectionsRepository.findById(selectionId);

        // Delete all the files first
        if (selectionOptional.isPresent()) {
            Selections selections = selectionOptional.get();

            List<FileInfo> fileInfoList = fileInfoRepository.findByFeatureId(selectionId);

            if (fileInfoList != null) {
                // Delete the actual file
                deleteFileWithPermissions(fileInfoList.get(0).getFileUrl());

                // Delete the fileInfo record
                fileInfoRepository.delete(fileInfoList.get(0));

            }

        } else {
            throw new RuntimeException("Document not found for id: " + selectionId);
        }

        // delete the project update itself
        selectionsRepository.deleteById(selectionId);
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
