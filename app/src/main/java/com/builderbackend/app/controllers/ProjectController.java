package com.builderbackend.app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.ProjectDTO;
import com.builderbackend.app.exceptions.UserAlreadyExistsException;
import com.builderbackend.app.services.ProjectService;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("project")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/createProject")
    public String createProject(@RequestBody ProjectDTO projectDTO,
            @CookieValue(value = "userId", required = true) String userIdCookie)
            throws UserAlreadyExistsException {

        ProjectDTO response = projectService.createProject(projectDTO, userIdCookie);
        return convertToJson(response);
    }

    @GetMapping("/get-projects-for-client")
    public String getProjects(@RequestParam String clientUserId) {
        List<ProjectDTO> ProjectDTOList;
        try {
            ProjectDTOList = projectService.getProjectsFromClientId(clientUserId);
        } catch (Exception e) {
            return e.getMessage(); // need better error handling.
        }

        return convertToJson(ProjectDTOList);
    }

    @GetMapping("/get-single-project-for-client")
    public String getSingleProject(@RequestParam String projectId) {
        ProjectDTO projectDTO;
        try {
            projectDTO = projectService.getProjectFromProjectId(projectId);
        } catch (Exception e) {
            return e.getMessage(); // need better error handling.
        }

        return convertToJson(projectDTO);
    }

    @PutMapping("/modify")
    public ResponseEntity<ProjectDTO> editProjectInfo(@RequestBody ProjectDTO project){
        ProjectDTO updatedProjectInfo = projectService.editProjectInfo(project);
        return ResponseEntity.ofNullable(updatedProjectInfo);
    }


    private String convertToJson(Object object) {
        return new Gson().toJson(object);
    }

}
