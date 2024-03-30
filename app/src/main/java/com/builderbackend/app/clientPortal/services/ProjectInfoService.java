package com.builderbackend.app.clientPortal.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.builderbackend.app.dtos.ProjectDTO;
import com.builderbackend.app.repositories.ProjectRepository;
import com.builderbackend.app.mappers.ProjectMapper;
import com.builderbackend.app.models.Project;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProjectInfoService {
    
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMapper projectMapper;


    // GET all projects from client Id assuming clients may have multiple projects
    public List<ProjectDTO> getAllProjectsForUserId(String clientId) {

        List<Project> projectList = new ArrayList<Project>();
        projectList = projectRepository.findByClientUserId(clientId);
        
        if(projectList.isEmpty()){
            System.out.println("clientId: "+ clientId + " not found when trying to getAllProjectsForUserId");
            return null;
        }

        List<ProjectDTO> ProjectDTOList = new ArrayList<ProjectDTO>();

        for (Project projectEntity : projectList) {
            ProjectDTOList.add(projectMapper.convert_Project_to_ProjectDTO(projectEntity));
        }
        return ProjectDTOList;
    }

    // get single project from id
    public ProjectDTO getProjectForprojectId(String projectId) {

         Project project = projectRepository.getReferenceById(projectId);

        if(project == null){
            System.out.println("projectId: "+ projectId + " not found when trying to getProjectForProjectId");
            return null;
        }

        ProjectDTO projectDTO = projectMapper.convert_Project_to_ProjectDTO(project);
        return projectDTO;

    }
}
