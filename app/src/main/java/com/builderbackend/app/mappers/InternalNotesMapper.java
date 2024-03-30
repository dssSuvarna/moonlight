package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.InternalNotesDTO;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.models.InternalNotes;
import com.builderbackend.app.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InternalNotesMapper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FileInfoMapper fileInfoMapper;

    /*
     * Converts InternalNotes Entity to InternalNotesDTO
     */
    public InternalNotesDTO convert_InternalNote_to_InternalNoteDTO(InternalNotes internalNotes) {
        InternalNotesDTO internalNotesDTO = new InternalNotesDTO();

        internalNotesDTO.setInternalNoteId(internalNotes.getInternalNoteId());
        internalNotesDTO.setTitle(internalNotes.getTitle());
        internalNotesDTO.setDate(internalNotes.getDate());
        internalNotesDTO.setDescription(internalNotes.getDescription());
        internalNotesDTO.setUserId(internalNotes.getUser().getUserId());
        internalNotesDTO.setBusinessId(internalNotes.getBusiness().getBusinessId());
        internalNotesDTO.setProjectId(internalNotes.getProject().getProjectId());

        // List<FileInfoDTO> files = new ArrayList<>();
        // for (FileInfo file : internalNotes.getFileInfo()) {
        // files.add(fileInfoMapper.convert_FileInfo_to_FileInfoDTO(file));
        // }

        // internalNotesDTO.setFileInfoDTOs(files);

        return internalNotesDTO;
    }

    /*
     * Converts InternalNotesDTO to InternalNotes Entity
     */
    public InternalNotes convert_InternalNotesDTO_to_InternalNotes(InternalNotesDTO internalNotesDTO) {
        InternalNotes internalNotes = new InternalNotes();

        internalNotes.setInternalNoteId(internalNotesDTO.getInternalNoteId());
        internalNotes.setTitle(internalNotesDTO.getTitle());
        internalNotes.setDate(internalNotesDTO.getDate());
        internalNotes.setDescription(internalNotesDTO.getDescription());
        internalNotes.setUser(userRepository.findById(internalNotesDTO.getUserId()).orElse(null));
        internalNotes.setBusiness(businessRepository.findById(internalNotesDTO.getBusinessId()).orElse(null));
        internalNotes.setProject(projectRepository.findById(internalNotesDTO.getProjectId()).orElse(null));

        // List<FileInfo> files = new ArrayList<>();
        // if (internalNotesDTO.getFileInfoDTOs() != null) {
        // for (FileInfoDTO fileDTO : internalNotesDTO.getFileInfoDTOs()) {
        // files.add(fileInfoMapper.convert_FileInfoDTO_to_FileInfo(fileDTO));
        // }

        // }
        // internalNotes.setFileInfo(files);
        return internalNotes;
    }

    // public InternalNotesAttachmentDTO
    // convert_InternalNotesAttachment_to_InternalNotesAttachmentDTO(
    // InternalNotesAttachment attachment) {
    // InternalNotesAttachmentDTO attachmentDTO = new InternalNotesAttachmentDTO();

    // attachmentDTO.setInternalNotesAttachmentId(attachment.getInternalNotesAttachmentId());
    // attachmentDTO.setBase64Data(attachment.getBase64Data());
    // attachmentDTO.setMimeType(attachment.getMimeType());

    // return attachmentDTO;
    // }

    // public InternalNotesAttachment
    // convert_InternalNotesAttachmentDTO_to_InternalNotesAttachment(
    // InternalNotesAttachmentDTO attachmentDTO,
    // InternalNotes internalNotes) {
    // InternalNotesAttachment internalNotesAttachment = new
    // InternalNotesAttachment();

    // internalNotesAttachment.setInternalNotes(internalNotes);

    // internalNotesAttachment.setInternalNotesAttachmentId(attachmentDTO.getInternalNotesAttachmentId());
    // internalNotesAttachment.setBase64Data(attachmentDTO.getBase64Data());
    // internalNotesAttachment.setMimeType(attachmentDTO.getMimeType());

    // return internalNotesAttachment;
    // }
}