package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.ProjectUpdateDTO;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.ProjectRepository;
import com.builderbackend.app.models.ProjectUpdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectUpdateMapper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FileInfoMapper fileInfoMapper;

    /*
     * Converts Update Entity to ProjectUpdateDTO
     */
    public ProjectUpdateDTO convert_ProjectUpdate_to_ProjectUpdateDTO(ProjectUpdate projectUpdate) {
        ProjectUpdateDTO projectUpdateDTO = new ProjectUpdateDTO();

        projectUpdateDTO.setProjectUpdateId(projectUpdate.getProjectUpdateId());
        projectUpdateDTO.setTitle(projectUpdate.getTitle());
        projectUpdateDTO.setDate(projectUpdate.getDate());
        projectUpdateDTO.setDescription(projectUpdate.getDescription());
        projectUpdateDTO.setUserId(projectUpdate.getUser().getUserId());
        projectUpdateDTO.setBusinessId(projectUpdate.getBusiness().getBusinessId());
        projectUpdateDTO.setProjectId(projectUpdate.getProject().getProjectId());

        // List<FileInfoDTO> files = new ArrayList<>();
        // for (FileInfo file : projectUpdate.getFileInfo()) {
        // files.add(fileInfoMapper.convert_FileInfo_to_FileInfoDTO(file));
        // }

        // projectUpdateDTO.setFileInfoDTOs(files);

        return projectUpdateDTO;
    }

    /*
     * Converts projectUpdateDTO to Update Entity
     */
    public ProjectUpdate convert_ProjectUpdateDTO_to_ProjectUpdate(ProjectUpdateDTO projectUpdateDTO) {
        ProjectUpdate projectUpdate = new ProjectUpdate();

        projectUpdate.setProjectUpdateId(projectUpdateDTO.getProjectUpdateId());
        projectUpdate.setTitle(projectUpdateDTO.getTitle());
        projectUpdate.setDate(projectUpdateDTO.getDate());
        projectUpdate.setDescription(projectUpdateDTO.getDescription());
        projectUpdate.setUser(userRepository.findById(projectUpdateDTO.getUserId()).orElse(null));
        projectUpdate.setBusiness(businessRepository.findById(projectUpdateDTO.getBusinessId()).orElse(null));
        projectUpdate.setProject(projectRepository.findById(projectUpdateDTO.getProjectId()).orElse(null));

        // List<FileInfo> files = new ArrayList<>();
        // if (projectUpdateDTO.getFileInfoDTOs() != null) {
        // for (FileInfoDTO fileDTO : projectUpdateDTO.getFileInfoDTOs()) {
        // files.add(fileInfoMapper.convert_FileInfoDTO_to_FileInfo(fileDTO));
        // }

        // }
        // projectUpdate.setFileInfo(files);

        // projectUpdate.setFileInfo(projectUpdate.getFileInfo());

        return projectUpdate;
    }
}
