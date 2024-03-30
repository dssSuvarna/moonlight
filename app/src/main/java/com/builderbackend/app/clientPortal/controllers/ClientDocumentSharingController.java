package com.builderbackend.app.clientPortal.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.DocumentSharingDTO;
import com.builderbackend.app.dtos.FolderDTO;
import com.builderbackend.app.models.Folder;
import com.builderbackend.app.repositories.FolderRepository;
import com.builderbackend.app.clientPortal.services.ClientDocumentSharingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;


import java.util.List;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@RestController
@RequestMapping("client-document-sharing")

// Todo 1: rename folders
// Todo 2: rename files
public class ClientDocumentSharingController {

    private final ClientDocumentSharingService clientDocumentSharingService;

    private final FolderRepository folderRepository;

    @Autowired
    public ClientDocumentSharingController(ClientDocumentSharingService clientDocumentSharingService, FolderRepository folderRepository) {
        this.clientDocumentSharingService = clientDocumentSharingService;
        this.folderRepository = folderRepository;
    }

    // endpoint is used to get the contents of folder with specified folderId
    @GetMapping("/get-folderContent/{folderId}")
    public ResponseEntity<FolderDTO> getDocumentsForFolder(@PathVariable String folderId) {
        FolderDTO folderDTO = clientDocumentSharingService.getFolderWithFolderId(folderId);

        return ResponseEntity.ofNullable(folderDTO);
    }

    // endpoint is used to get the contents of the default folder
    @GetMapping("/get-documents/{projectId}")
    public ResponseEntity<FolderDTO> getDocumentsForProject(@PathVariable String projectId) {
        FolderDTO folderDTO = clientDocumentSharingService.getDefaultFolderForProjectId(projectId);

        return ResponseEntity.ofNullable(folderDTO);
    }

    // endpoint used to create a new document
    @PostMapping("/create")
    public ResponseEntity<?> createDocument(
        @RequestPart("documentSharingDTO") String documentSharingString,
        @RequestPart("folderDTO") String folderString,
        @RequestPart("file") MultipartFile file) {

        FolderDTO folderDTO;
        DocumentSharingDTO documentSharingDTO;

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            documentSharingDTO = objectMapper.readValue(documentSharingString,
                DocumentSharingDTO.class);

            folderDTO = objectMapper.readValue(folderString, FolderDTO.class);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("unable to map to object: " + e.getMessage());
        }
        

        // validate that the file exists
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is required.");
        }

        // validate folder exists
        Optional<Folder> folder = folderRepository.findById(folderDTO.getFolderId());
        if(!folder.isPresent()){
            return ResponseEntity.badRequest().body("Atrempting to add file to Invalid folder");
        }

        DocumentSharingDTO responseDocument = clientDocumentSharingService.createDocument(documentSharingDTO, file, folderDTO);

        return ResponseEntity.ofNullable(responseDocument);
    }

    // endpoint used to create a new folder
    @PostMapping("/createFolder")
    public ResponseEntity<?> createFolder(@Valid @RequestBody FolderDTO folderDTO) {

        //validate name is not the root folder name
        String rootFolderName = String.format("%s-%s", "defaultFolder", folderDTO.getProjectId()); 
        if(folderDTO.getFolderName() == rootFolderName){
           return ResponseEntity.badRequest().body("Invalid folder name.");
        }

        //validate parent folder exist
        Optional<Folder> folder = folderRepository.findById(folderDTO.getParentFolderId());
        if(!folder.isPresent()){
           return ResponseEntity.badRequest().body("Invalid parent folder");
        }

        // validate path
        if(!isValidPath(folderDTO.getPath(), folderDTO.getBusinessId(), folderDTO.getProjectId())){
            return ResponseEntity.badRequest().body("Invalid path");
        }

        FolderDTO responseFolder = clientDocumentSharingService.createFolder(folderDTO);

        return ResponseEntity.ofNullable(responseFolder);
    }

    // endpont used to delete a document
    //todo: validate that document person deleting the folder has permission to do so (client can only delete their own, same with business)
    @DeleteMapping("/delete/{documentSharingId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String documentSharingId) {
        try {
            clientDocumentSharingService.deleteDocument(documentSharingId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Corrected this line
        }
    }


    // endpont used to delete a folder
    @DeleteMapping("/deleteFolder/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable String folderId) {
        try {

            //validation - ensure we are not deleting root folder
            Optional<Folder> folder = folderRepository.findById(folderId);
            // modify this to accept folderDTO and not just id
            // also make sure we do a similar check on create
            if(!folder.isPresent()){
                return ResponseEntity.badRequest().body("unable to delete since folder not found");
            }

            String rootFolderName = String.format("%s-%s", "defaultFolder", folder.get().getProject().getProjectId()); 
            if(folder.get().getFolderName() == rootFolderName) {
                return ResponseEntity.badRequest().body("Unable to delete root folder.");
            }

            clientDocumentSharingService.deleteFolderIfEmpty(folderId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // Corrected this line
        }
    }


    private static boolean isValidPath(String testString, String businessId, String projectId) {
        String patternString = "^uploads/" + Pattern.quote(businessId) + "/" + Pattern.quote(projectId) + "/" + "documentSharing" + ".*";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(testString);

        return matcher.matches();
    }
}