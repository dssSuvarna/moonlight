package com.builderbackend.app.services;

import lombok.Data;

import java.io.IOException;
import java.lang.Exception;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.builderbackend.app.models.FileInfo;
import com.builderbackend.app.models.InternalNotes;
import com.builderbackend.app.dtos.InternalNotesDTO;
import com.builderbackend.app.dtos.FileInfoDTO;
import com.builderbackend.app.repositories.InternalNotesRepository;
import com.builderbackend.app.repositories.InternalNotesAttachmentRepository;
import com.builderbackend.app.mappers.InternalNotesMapper;
import lombok.AllArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import com.builderbackend.app.repositories.FileInfoRepository;
import java.nio.file.*;

import com.builderbackend.app.mappers.FileInfoMapper;

@AllArgsConstructor
@Service
@Data
public class InternalNotesService {

    @Autowired
    InternalNotesRepository internalNotesRepository;

    @Autowired
    FileInfoRepository fileInfoRepository;

    @Autowired
    InternalNotesAttachmentRepository internalNotesAttachmentRepository;

    @Autowired
    InternalNotesMapper internalNotesMapper;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    FileInfoMapper fileInfoMapper;

    // Error handling and throwing exceptions can be added later
    @Transactional
    public InternalNotesDTO createInternalNote(InternalNotesDTO internalNotesDTO, List<MultipartFile> files) {

        // Setting UUID for Update
        String internalNoteId = UUID.randomUUID().toString();
        internalNotesDTO.setInternalNoteId(internalNoteId);

        // gets all files for the project update
        List<FileInfo> existingFileInfoList = fileInfoRepository.findByFeatureId(internalNoteId);

        // converts all files entitys to fileInfoDTOs
        List<FileInfoDTO> existingFileInfoDTOList = new ArrayList<>();
        for (FileInfo fileInfo : existingFileInfoList) {
            FileInfoDTO existingFileInfoDTO = fileInfoMapper.convert_FileInfo_to_FileInfoDTO(fileInfo);
            existingFileInfoDTOList.add(existingFileInfoDTO);
        }

        // // FileUploadController.
        // // Convert DTO to Entity
        // InternalNotes internalNoteEntity = internalNotesMapper
        // .convert_InternalNotesDTO_to_InternalNotes(internalNotesDTO);

        // // Save to the database
        // internalNotesRepository.save(internalNoteEntity);

        // save each image
        if (files != null) {
            for (MultipartFile file : files) {

                FileInfoDTO newFileInfoDTO = new FileInfoDTO();
                String fileId = UUID.randomUUID().toString();

                newFileInfoDTO.setFileId(fileId);
                newFileInfoDTO.setFileUrl(
                        String.format("uploads/%s/%s/%s/%s", internalNotesDTO.getBusinessId(),
                                internalNotesDTO.getProjectId(), "internalNotes",
                                newFileInfoDTO.getFileId()));

                newFileInfoDTO.setFeatureId(internalNoteId);

                FileInfo newfileInfo = fileInfoMapper.convert_FileInfoDTO_to_FileInfo(newFileInfoDTO);
                fileInfoRepository.save(newfileInfo);

                // Append new item to the list
                existingFileInfoDTOList.add(newFileInfoDTO);

                // 3. Set the updated list back (this step might not be needed if the list was
                // already initialized,
                // as Java lists are reference types and modifying the list modifies the
                // original reference in projectUpdateDTO)
                internalNotesDTO.setFileInfoDTOs(existingFileInfoDTOList);

                fileUploadService.save(file, newFileInfoDTO.getFileUrl());

                // FileInfo fileInfo =
                // fileInfoMapper.convert_FileInfoDTO_to_FileInfo(newFileInfoDTO);
                // fileInfoRepository.save(fileInfo);

            }
        }

        InternalNotes internalNotesEntity = internalNotesMapper
                .convert_InternalNotesDTO_to_InternalNotes(internalNotesDTO);
        // internalNoteEntity =
        // internalNotesRepository.findById(internalNoteId).orElse(null);

        internalNotesRepository.save(internalNotesEntity);
        // internalNotesDTO =
        // internalNotesMapper.convert_InternalNote_to_InternalNoteDTO(internalNoteEntity);

        return internalNotesDTO;
    }

    @Transactional
    public List<InternalNotesDTO> getInternalNotesForProjectId(String projectId) throws Exception {
        try {

            List<InternalNotesDTO> internalNotesDTOList = new ArrayList<>();

            List<InternalNotes> internalNotesList = internalNotesRepository.findByProjectProjectId(projectId);

            for (InternalNotes internalNotes : internalNotesList) {
                InternalNotesDTO internalNotesDTO = internalNotesMapper
                        .convert_InternalNote_to_InternalNoteDTO(internalNotes);

                List<FileInfo> fileInfo = fileInfoRepository.findByFeatureId(internalNotes.getInternalNoteId());
                List<FileInfoDTO> fileInfoDTOs = new ArrayList<>();
                for (FileInfo file : fileInfo) {
                    FileInfoDTO fileDto = (fileInfoMapper.convert_FileInfo_to_FileInfoDTO(file));
                    fileInfoDTOs.add(fileDto);

                }

                internalNotesDTO.setFileInfoDTOs(fileInfoDTOs);
                internalNotesDTOList.add(internalNotesDTO);

            }
            // for (InternalNotes internalNotes : internalNotesList) {
            // internalNotesDTOList.add(internalNotesMapper.convert_InternalNote_to_InternalNoteDTO(internalNotes));
            // }
            return internalNotesDTOList;
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            throw e;
        }
    }

    public InternalNotesDTO modifyInternalNote(InternalNotesDTO internalNotesDTO, List<MultipartFile> files) {

        // save each image

        // get saved project update to modify
        String internalNoteId = internalNotesDTO.getInternalNoteId();

        InternalNotes internalNotes = internalNotesRepository.findById(internalNoteId).orElse(null);
        internalNotesDTO.setDate(internalNotes.getDate());

        InternalNotesDTO currentInternalNoteDTO = internalNotesMapper
                .convert_InternalNote_to_InternalNoteDTO(internalNotes);

        internalNotesDTO.setInternalNoteId(internalNoteId);

        // gets all files for the project update
        List<FileInfo> existingFileInfoList = fileInfoRepository.findByFeatureId(internalNoteId);

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
                        String.format("uploads/%s/%s/%s/%s", internalNotesDTO.getBusinessId(),
                                internalNotesDTO.getProjectId(), "internalNotes",
                                newFileInfoDTO.getFileId()));
                newFileInfoDTO.setFeatureId(internalNoteId);

                FileInfo newfileInfo = fileInfoMapper.convert_FileInfoDTO_to_FileInfo(newFileInfoDTO);
                fileInfoRepository.save(newfileInfo);

                // Get the existing list
                // List<FileInfoDTO> existingFileInfoDTOList =
                // currentInternalNoteDTO.getFileInfoDTOs();

                // Append new item to the list
                existingFileInfoDTOList.add(newFileInfoDTO);

                // 3. Set the updated list back (this step might not be needed if the list was
                // already initialized,
                // as Java lists are reference types and modifying the list modifies the
                // original reference in projectUpdateDTO)
                internalNotesDTO.setFileInfoDTOs(existingFileInfoDTOList);

                fileUploadService.save(file, newFileInfoDTO.getFileUrl());

            }
        }
        // Convert DTO to Entity
        InternalNotes internalNoteEntity = internalNotesMapper
                .convert_InternalNotesDTO_to_InternalNotes(internalNotesDTO);

        // Save to the database
        internalNotesRepository.save(internalNoteEntity);

        // createProjectUpdate(projectUpdateDTO, files);
        return internalNotesDTO;
    }

    public void deleteInternalNote(String internalNoteId) {
        Optional<InternalNotes> internalNotesOptional = internalNotesRepository.findById(internalNoteId);

        // Delete all the files first
        if (internalNotesOptional.isPresent()) {
            InternalNotes internalNotes = internalNotesOptional.get();

            List<FileInfo> fileInfoList = fileInfoRepository.findByFeatureId(internalNoteId);
            for (FileInfo fileInfo : fileInfoList) {
                // Delete the actual file
                deleteFileWithPermissions(fileInfo.getFileUrl());

                // Delete the fileInfo record
                fileInfoRepository.delete(fileInfo);
            }
        } else {
            throw new RuntimeException("Internal Note not found for id: " + internalNoteId);
        }

        // delete the project update itself
        internalNotesRepository.deleteById(internalNoteId);
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