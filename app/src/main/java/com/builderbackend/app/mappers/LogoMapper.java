package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.LogoDTO;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.models.Logo;
import com.builderbackend.app.repositories.FileInfoRepository;
import com.builderbackend.app.dtos.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import com.builderbackend.app.models.FileInfo;


@Service
public class LogoMapper {

    @Autowired
    BusinessRepository businessRepository;
  
    @Autowired
    FileInfoMapper fileInfoMapper;


    @Autowired
    FileInfoRepository fileInfoRepository;
    
        /*
     * Converts Logo Entity to Logo Response DTO
     */
    public LogoDTO convert_Logo_to_LogoDTO(Logo logo) {
        LogoDTO logoDTO = new LogoDTO();

        logoDTO.setLogoId(logo.getLogoId());
        logoDTO.setLogoType(logo.getLogoType());
        logoDTO.setBusinessId(logo.getBusiness().getBusinessId());

        List<FileInfo> fileInfoList = fileInfoRepository.findByFeatureId(logo.getLogoId());
        if (!CollectionUtils.isEmpty(fileInfoList)){
            FileInfoDTO fileInfoDTO= new FileInfoDTO();
            fileInfoDTO.setFileUrl(fileInfoList.get(0).getFileUrl());
            fileInfoDTO.setFileId(fileInfoList.get(0).getFileId());
            fileInfoDTO.setFeatureId(fileInfoList.get(0).getFeatureId());
            logoDTO.setFileInfoDTO(fileInfoDTO);
        }
        return logoDTO;
    }

        /*
     * Converts Logo  DTO to Logo Entity
     */
    public Logo convert_LogoDTO_to_Logo(LogoDTO logoDTO) {


        Logo logo = new Logo();

        logo.setLogoId(logoDTO.getLogoId());
        logo.setBusiness(businessRepository.findByBusinessId(logoDTO.getBusinessId()));
        logo.setLogoType(logoDTO.getLogoType());

        return logo;
    }

}