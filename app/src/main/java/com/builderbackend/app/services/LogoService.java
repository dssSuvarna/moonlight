package com.builderbackend.app.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.builderbackend.app.enums.EventType;
import com.builderbackend.app.dtos.LogoDTO;
import com.builderbackend.app.repositories.LogoRepository;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.FileInfoRepository;
import com.builderbackend.app.mappers.LogoMapper;
import com.builderbackend.app.models.Logo;
import com.builderbackend.app.models.FileInfo;
import com.builderbackend.app.mappers.FileInfoMapper;
import com.builderbackend.app.dtos.*;
import com.builderbackend.app.services.FileUploadService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.*;

@Service
public class LogoService {

    @Autowired
    LogoRepository logoRepository;
    
    @Autowired
    BusinessRepository businessRepository;
    
    @Autowired
    LogoMapper LogoMapper;

    @Autowired
    FileInfoRepository fileInfoRepository;

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Autowired
    FileUploadService fileUploadService;

    public List<LogoDTO> getLogosForBusinessId(String businessId) {
        List<LogoDTO> logoDTOs = new ArrayList<>(); 
    
        Optional<List<Logo>> optionalLogos =logoRepository.findByBusinessBusinessId(businessId);
    
        if (optionalLogos.isPresent()) {
            for (Logo logo : optionalLogos.get()) { 
                LogoDTO logoDTO = LogoMapper.convert_Logo_to_LogoDTO(logo);
                logoDTOs.add(logoDTO); 
            }
        }
    
        return logoDTOs;
    }
    
    
    public LogoDTO saveLogos(LogoDTO logoDTO, MultipartFile file) {

            String logoId = UUID.randomUUID().toString();
            String fileId = UUID.randomUUID().toString();
    
            logoDTO.setLogoId(logoId);
    
            Logo logoEntity = LogoMapper.convert_LogoDTO_to_Logo(logoDTO);
            logoRepository.save(logoEntity);
    
            FileInfoDTO newFileInfoDTO = new FileInfoDTO();
            newFileInfoDTO.setFileId(fileId);
            newFileInfoDTO.setFeatureId(logoId); 
            newFileInfoDTO.setFileUrl(String.format("uploads/logos/%s/%s", logoDTO.getBusinessId(), fileId));
    
            FileInfo newFileInfo = fileInfoMapper.convert_FileInfoDTO_to_FileInfo(newFileInfoDTO);
            fileInfoRepository.save(newFileInfo); 
    
            logoDTO.setFileInfoDTO(newFileInfoDTO);
            if (file != null) { 
                fileUploadService.save(file, newFileInfoDTO.getFileUrl());
            }
    
        return logoDTO;

    }
 
    @Transactional
    public void deleteLogo(String logoId) {
        Optional<Logo> logoOptional = logoRepository.findById(logoId);
    
        if (logoOptional.isPresent()) {
            List<FileInfo> fileInfoList = fileInfoRepository.findByFeatureId(logoId);
            
            // Since the list is expected to have a single element, check if it's not empty
            if (!fileInfoList.isEmpty()) {
                FileInfo fileInfo = fileInfoList.get(0); 
    
                try {
                    // Delete the physical file first
                    deleteFileWithPermissions(fileInfo.getFileUrl());

                    // If file deletion is successful, delete the FileInfo record

                    fileInfoRepository.delete(fileInfo);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete the file at: " + fileInfo.getFileUrl(), e);
                }
            }
        } else {
            throw new RuntimeException("Logo not found for id: " + logoId);
        }
        logoRepository.deleteById(logoId);
    }
    

    private void deleteFileWithPermissions(String fileUrl) throws Exception {
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