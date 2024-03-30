package com.builderbackend.app.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import com.builderbackend.app.repositories.FileInfoRepository;
import com.builderbackend.app.repositories.DocumentSharingRepository;

import com.builderbackend.app.models.FileInfo;
// these need to be modified once I create corrusponding classes
import com.builderbackend.app.services.FileUploadService;
import jakarta.servlet.ServletContext;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("file-upload")
public class FileUploadController {

    @Autowired
    FileUploadService storageService;

    @Autowired
    FileInfoRepository fileInfoRepository;

    @Autowired
    DocumentSharingRepository documentSharingRepository;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    private ServletContext servletContext;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            response = storageService.save(file, "tempUploads");
            response.put("message", "Uploaded the file successfully: " + file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message",
                    "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename, HttpServletRequest request) {
        Resource file = storageService.load(filename);
        String mimeType = request.getServletContext().getMimeType(file.getFilename());
        ContentDisposition contentDisposition = mimeType.equals("application/pdf")
                ? ContentDisposition.inline().filename(file.getFilename()).build()
                : ContentDisposition.attachment().filename(file.getFilename()).build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(file);
    }

    @GetMapping("/uploads/{fileName}/{fileId}")
    public ResponseEntity<?> serveFile(@PathVariable String fileName, @PathVariable String fileId) {
        Optional<FileInfo> fileInfo = fileInfoRepository.findById(fileId);

        if (fileInfo.isPresent()) {
            String filePath = fileInfo.get().getFileUrl();
            Resource file = fileUploadService.load(filePath);

            String mimeType = servletContext.getMimeType(fileName);
            if (mimeType == null || mimeType.equals("application/octet-stream")) {
                mimeType = fileName.endsWith(".pdf") ? "application/pdf" : "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found with id: " + fileId);
        }
    }

}