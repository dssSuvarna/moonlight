package com.builderbackend.app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.ProjectUpdateDTO;
import com.builderbackend.app.services.ProjectUpdateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("project-update")
public class ProjectUpdateController {

    private final ProjectUpdateService projectUpdateService;

    @Autowired
    public ProjectUpdateController(ProjectUpdateService projectUpdateService) {
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

    @PostMapping("/create")
    public String createUpdate(@RequestPart("projectUpdateDTO") String projectUpdateDTOString,
            @RequestPart(name = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProjectUpdateDTO projectUpdateDTO = objectMapper.readValue(projectUpdateDTOString, ProjectUpdateDTO.class);
        ProjectUpdateDTO response = projectUpdateService.createProjectUpdate(projectUpdateDTO, files);

        return convertToJson(response);
    }

    @PutMapping("/modify")
    public String modifyUpdate(@RequestPart("projectUpdateDTO") String projectUpdateDTOString,
            @RequestPart(name = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProjectUpdateDTO projectUpdateDTO = objectMapper.readValue(projectUpdateDTOString, ProjectUpdateDTO.class);

        ProjectUpdateDTO modifiedUpdateDTO = projectUpdateService.modifyProjectUpdate(projectUpdateDTO, files);
        return convertToJson(modifiedUpdateDTO);

    }

    @DeleteMapping("/delete/{projectUpdateId}")
    public ResponseEntity<Void> deleteProjectUpdate(@PathVariable String projectUpdateId) {
        projectUpdateService.deleteProjectUpdate(projectUpdateId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-fileInfo/{fileInfoId}")
    public ResponseEntity<Void> deleteFileInfo(@PathVariable String fileInfoId) {
        try {
            projectUpdateService.deleteFileInfo(fileInfoId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String convertToJson(Object object) {
        return new Gson().toJson(object);
    }

}