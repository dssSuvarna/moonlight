package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.DocumentSharingDTO;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.FolderRepository;
import com.builderbackend.app.repositories.ProjectRepository;
import com.builderbackend.app.models.DocumentSharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentSharingMapper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Autowired
    FolderRepository folderRepository;

    /*
     * Converts Update Entity to DocumentSharingDTO
     */
    public DocumentSharingDTO convert_DocumentSharing_to_DocumentSharingDTO(DocumentSharing documentSharing) {
        DocumentSharingDTO documentSharingDTO = new DocumentSharingDTO();

        documentSharingDTO.setDocumentSharingId(documentSharing.getDocumentSharingId());
        documentSharingDTO.setFileName(documentSharing.getFileName());
        documentSharingDTO.setUserOwnerId(documentSharing.getOwner().getUserId());
        documentSharingDTO.setBusinessId(documentSharing.getBusiness().getBusinessId());
        documentSharingDTO.setProjectId(documentSharing.getProject().getProjectId());
        documentSharingDTO.setFolderId(documentSharing.getFolder().getFolderId());
        documentSharingDTO.setCreatedDate(documentSharing.getCreatedDate());

        return documentSharingDTO;
    }

    /*
     * Converts documentSharingDTO to Update Entity
     */
    public DocumentSharing convert_DocumentSharingDTO_to_DocumentSharing(DocumentSharingDTO documentSharingDTO) {
        DocumentSharing documentSharing = new DocumentSharing();

        documentSharing.setDocumentSharingId(documentSharingDTO.getDocumentSharingId());
        documentSharing.setFileName(documentSharingDTO.getFileName());
        documentSharing.setOwner(userRepository.findById(documentSharingDTO.getUserOwnerId()).orElse(null));
        documentSharing.setBusiness(businessRepository.findById(documentSharingDTO.getBusinessId()).orElse(null));
        documentSharing.setProject(projectRepository.findById(documentSharingDTO.getProjectId()).orElse(null));
        documentSharing.setFolder(documentSharingDTO.getFolderId() != null ? folderRepository.findById(documentSharingDTO.getFolderId()).orElse(null) : null );
        documentSharing.setCreatedDate(documentSharingDTO.getCreatedDate());

        return documentSharing;
    }

}
