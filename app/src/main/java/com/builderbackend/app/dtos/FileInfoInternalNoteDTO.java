package com.builderbackend.app.dtos;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

//@AllArgsConstructor
@Data
public class FileInfoInternalNoteDTO {

    private String fileId;
    private String fileUrl;
    private MultipartFile file;
    private String internalNoteId;

}