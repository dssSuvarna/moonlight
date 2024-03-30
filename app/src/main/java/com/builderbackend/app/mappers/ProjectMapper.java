package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.ProjectDTO;
import com.builderbackend.app.models.Project;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapper {

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    UserRepository userRepository;

    public ProjectDTO convert_Project_to_ProjectDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setAddress(project.getAddress());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setEndDate(project.getEndDate());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setClientId(project.getClient().getUserId());
        projectDTO.setOwnerId(project.getOwner().getUserId());
        projectDTO.setBusinessId((project.getBusiness().getBusinessId()));

        return projectDTO;
    }

    public Project convert_ProjectDTO_to_Project(ProjectDTO projectDTO) {
        Project project = new Project();

        project.setProjectId(projectDTO.getProjectId());
        project.setAddress(projectDTO.getAddress());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setDescription(projectDTO.getDescription());
        project.setBusiness(businessRepository.findByBusinessId(projectDTO.getBusinessId()));
        project.setClient(userRepository.findByUserId(projectDTO.getClientId()));
        project.setOwner(userRepository.findByUserId(projectDTO.getOwnerId()));

        return project;
    }

}
