package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.FolderDTO;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.FolderRepository;
import com.builderbackend.app.repositories.ProjectRepository;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.models.Folder;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderMapper {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private DocumentSharingMapper documentSharingMapper;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ProjectRepository projectRepository;

    public FolderDTO convert_Folder_to_folderDTO(Folder folder) {
        FolderDTO dto = new FolderDTO();
        dto.setFolderId(folder.getFolderId());
        dto.setBusinessId(folder.getBusiness() != null ? folder.getBusiness().getBusinessId() : null);
        dto.setProjectId(folder.getProject() != null ? folder.getProject().getProjectId() : null);
        dto.setFolderName(folder.getFolderName());
        dto.setCreatedDate(folder.getCreatedDate());
        dto.setCreatedBy(folder.getCreatedBy() != null ? folder.getCreatedBy().getUserId() : null);
        dto.setParentFolderId(folder.getParentFolder() != null ? folder.getParentFolder().getFolderId() : null);
        dto.setPath(folder.getPath());

        if (folder.getSubFolders() != null) {
            List<FolderDTO.SubFolderInfo> subFolderInfoList = folder.getSubFolders().stream()
                .map(subFolder -> {
                    FolderDTO.SubFolderInfo info = new FolderDTO.SubFolderInfo();
                    info.setFolderId(subFolder.getFolderId());
                    info.setFolderName(subFolder.getFolderName());
                    info.setCreatedDate(subFolder.getCreatedDate());
                    info.setCreatedBy(subFolder.getCreatedBy().getUserId());
                    return info;
                })
                .collect(Collectors.toList());
            dto.setSubFolders(subFolderInfoList);
        }

        dto.setDocuments(
            folder.getDocuments() != null 
            ? folder.getDocuments().stream()
                .map(document -> documentSharingMapper.convert_DocumentSharing_to_DocumentSharingDTO(document))
                .collect(Collectors.toList()) 
            : null
        );

        return dto;
    }

    public Folder convert_FolderDTO_to_Folder(FolderDTO dto) {
        Folder folder = new Folder();
        folder.setFolderId(dto.getFolderId());
        folder.setBusiness(dto.getBusinessId() != null ? businessRepository.findById(dto.getBusinessId()).orElse(null) : null);
        folder.setProject(dto.getProjectId() != null ? projectRepository.findById(dto.getProjectId()).orElse(null) : null);
        folder.setFolderName(dto.getFolderName());
        folder.setCreatedDate(dto.getCreatedDate());
        folder.setCreatedBy(dto.getCreatedBy() != null ? userRepository.findById(dto.getCreatedBy()).orElse(null) : null);
        folder.setParentFolder(dto.getParentFolderId() != null ? folderRepository.findById(dto.getParentFolderId()).orElse(null) : null);
        folder.setPath(dto.getPath());

        // Handling SubFolders using FolderRepository
        if (!dto.getSubFolders().isEmpty()) {
            List<Folder> subFolders = dto.getSubFolders().stream()
                .map(subFolderInfo -> folderRepository.findById(subFolderInfo.getFolderId())
                    .orElse(null)) // Handle case where folder might not exist
                .collect(Collectors.toList());
            folder.setSubFolders(subFolders);
        }
        folder.setDocuments(
            dto.getDocuments() != null 
            ? dto.getDocuments().stream()
                .map(documentDTO -> documentSharingMapper.convert_DocumentSharingDTO_to_DocumentSharing(documentDTO))
                .collect(Collectors.toList()) 
            : null
        );

        return folder;
    }

}
