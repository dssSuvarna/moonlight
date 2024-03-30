package com.builderbackend.app.clientPortal.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.ProjectUpdateDTO;
import com.builderbackend.app.clientPortal.services.ClientProjectUpdateService;
import com.google.gson.Gson;

import java.util.List;

@RestController
@RequestMapping("client-project-update")
public class ClientProjectUpdateController {

    private final ClientProjectUpdateService projectUpdateService;

    @Autowired
    public ClientProjectUpdateController(ClientProjectUpdateService projectUpdateService) {
        this.projectUpdateService = projectUpdateService;

    }

    @GetMapping("/get-updates/{projectId}")
    public String getUpdatesForProject(@PathVariable String projectId) {
        List<ProjectUpdateDTO> projectUpdateDTO;
        try {
            projectUpdateDTO = projectUpdateService.getProjectUpdatesForProjectId(projectId);
        } catch (Exception e) {
            return e.getMessage(); // need better error handling.
        }

        return convertToJson(projectUpdateDTO);
    }

    private String convertToJson(Object object) {
        return new Gson().toJson(object);
    }

}
