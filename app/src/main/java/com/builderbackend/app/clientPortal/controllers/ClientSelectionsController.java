package com.builderbackend.app.clientPortal.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.SelectionsDTO;
import com.builderbackend.app.clientPortal.services.ClientSelectionsService;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("client-selections")
public class ClientSelectionsController {

    private final ClientSelectionsService selectionsService;

    @Autowired
    public ClientSelectionsController(ClientSelectionsService selectionsService) {
        this.selectionsService = selectionsService;

    }

    @GetMapping("/get-selections")
    public String getUpdatesForProject(@RequestParam("projectId") String projectId) {
        List<SelectionsDTO> selectionsDTOs;
        try {
            selectionsDTOs = selectionsService.getSelectionsForProjectId(projectId);
        } catch (Exception e) {
            return e.getMessage(); // need better error handling.
        }

        return convertToJson(selectionsDTOs);
    }

    @PutMapping("/confirm-selection")
    public ResponseEntity<Void> confirmSelectoion(@RequestParam("selectionId") String selectionId) {
        selectionsService.ConfirmSelection(selectionId);
        return ResponseEntity.noContent().build();

    }

    private String convertToJson(Object object) {
        return new Gson().toJson(object);
    }

}
