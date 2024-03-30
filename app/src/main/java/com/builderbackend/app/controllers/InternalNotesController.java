package com.builderbackend.app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.InternalNotesDTO;
import com.builderbackend.app.services.InternalNotesService;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("internalNotes")

public class InternalNotesController {

    private final InternalNotesService internalNotesService;

    @Autowired
    public InternalNotesController(InternalNotesService internalNotesService) {
        this.internalNotesService = internalNotesService;
    }

    @GetMapping("/get-notes/{projectId}")
    public String getInternalNotesForProject(@PathVariable String projectId) {
        List<InternalNotesDTO> internalNotesDTO;
        try {
            internalNotesDTO = internalNotesService.getInternalNotesForProjectId(projectId);
        } catch (Exception e) {
            return e.getMessage(); // need better error handling.
        }

        return convertToJson(internalNotesDTO);
    }

    @PostMapping("/create")
    public String createNote(@RequestPart("internalNoteDTO") String internalNotesDTOString,
            @RequestPart(name = "files", required = false) List<MultipartFile> files)
            throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        InternalNotesDTO internalNotesDTO = objectMapper.readValue(internalNotesDTOString, InternalNotesDTO.class);
        InternalNotesDTO response = internalNotesService.createInternalNote(internalNotesDTO, files);

        return convertToJson(response);
    }

    @PutMapping("/modify")
    public String modifyNote(@RequestPart("internalNoteDTO") String internalNoteDTOString,
            @RequestPart(name = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        InternalNotesDTO internalNotesDTO = objectMapper.readValue(internalNoteDTOString, InternalNotesDTO.class);

        InternalNotesDTO modifiedNoteDTO = internalNotesService.modifyInternalNote(internalNotesDTO, files);
        return convertToJson(modifiedNoteDTO);

    }

    @DeleteMapping("/delete/{internalNoteId}")
    public ResponseEntity<Void> deleteInternalNote(@PathVariable String internalNoteId) {
        internalNotesService.deleteInternalNote(internalNoteId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-fileInfo/{fileInfoId}")
    public ResponseEntity<Void> deleteFileInfo(@PathVariable String fileInfoId) {
        try {
            internalNotesService.deleteFileInfo(fileInfoId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String convertToJson(Object object) {
        return new Gson().toJson(object);
    }

}