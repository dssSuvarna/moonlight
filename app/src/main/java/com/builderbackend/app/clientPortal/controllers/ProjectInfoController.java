package com.builderbackend.app.clientPortal.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.builderbackend.app.dtos.ProjectDTO;
import com.builderbackend.app.clientPortal.services.ProjectInfoService;

@RestController
@RequestMapping("projectInfo")
public class ProjectInfoController {

    @Autowired
    ProjectInfoService projectInfoService;
    
    /**
     * get list of project for the user
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllProjectsInfo(@CookieValue("userId") String clientId) {

        List<ProjectDTO> projectList = projectInfoService.getAllProjectsForUserId(clientId);

        return ResponseEntity.ofNullable(projectList);
    }

    /**
     * get single project based on id
     * @return
     */
    @GetMapping("/projectId")
    public ResponseEntity<ProjectDTO> getSingleProjectInfo(@RequestParam("projectId") String projectId) {

        ProjectDTO projectDTOResponse = projectInfoService.getProjectForprojectId(projectId);

        return ResponseEntity.ofNullable(projectDTOResponse);
    }
}
