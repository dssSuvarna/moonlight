package com.builderbackend.app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.SelectionsDTO;
import com.builderbackend.app.services.SelectionsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("selections")
public class SelectionsController {

    private final SelectionsService selectionsService;

    @Autowired
    public SelectionsController(SelectionsService selectionsService) {
        this.selectionsService = selectionsService;
    }

    @GetMapping("/get-selections/{projectId}")
    public String getUpdatesForProject(@PathVariable String projectId) {
        List<SelectionsDTO> selectionsDTOs;
        try {
            selectionsDTOs = selectionsService.getSelectionsForProjectId(projectId);
        } catch (Exception e) {
            return e.getMessage(); // need better error handling.
        }

        return convertToJson(selectionsDTOs);
    }

    @PostMapping("/create")
    public String createSelection(@RequestPart("selectionsDTO") String selectionsDTOString,
            @RequestPart(name = "files", required = false) MultipartFile file) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        SelectionsDTO selectionsDTO = objectMapper.readValue(selectionsDTOString, SelectionsDTO.class);
        SelectionsDTO response = selectionsService.createSelection(selectionsDTO, file);

        return convertToJson(response);
    }

    @DeleteMapping("/delete/{selectionId}")
    public ResponseEntity<Void> deleteSelection(@PathVariable String selectionId) {
        selectionsService.deleteSelection(selectionId);
        return ResponseEntity.noContent().build();
    }

    private String convertToJson(Object object) {
        return new Gson().toJson(object);
    }

}
