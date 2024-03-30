package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.FileInfoDTO;
import com.builderbackend.app.models.FileInfo;

import org.springframework.stereotype.Service;

@Service
public class FileInfoMapper {

    public FileInfoDTO convert_FileInfo_to_FileInfoDTO(FileInfo fileInfo) {
        FileInfoDTO fileInfoDTO = new FileInfoDTO();

        fileInfoDTO.setFileId(fileInfo.getFileId());
        fileInfoDTO.setFileUrl(fileInfo.getFileUrl());
        fileInfoDTO.setFeatureId(fileInfo.getFeatureId());

        return fileInfoDTO;
    }

    public FileInfo convert_FileInfoDTO_to_FileInfo(FileInfoDTO fileInfoDTO) {

        FileInfo fileInfo = new FileInfo();

        fileInfo.setFileId(fileInfoDTO.getFileId());
        fileInfo.setFileUrl(fileInfoDTO.getFileUrl());
        fileInfo.setFeatureId(fileInfoDTO.getFeatureId());

        return fileInfo;

    }

    // public FileInfo convert_FileInfoDTO_to_FileInfo(FileInfoDTO fileInfoDTO,
    // InternalNotes internalNotes) {

    // FileInfo fileInfo = new FileInfo();

    // fileInfo.setFileId(fileInfoDTO.getFileId());
    // fileInfo.setFileUrl(fileInfoDTO.getFileUrl());
    // fileInfo.setInternalNotes(internalNotes);

    // return fileInfo;

    // }

}
