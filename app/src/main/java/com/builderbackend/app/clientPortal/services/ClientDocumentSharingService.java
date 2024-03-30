package com.builderbackend.app.clientPortal.services;

import lombok.Data;
import java.lang.Exception;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.builderbackend.app.models.DocumentSharing;
import com.builderbackend.app.models.FileInfo;
import com.builderbackend.app.models.Folder;
import com.builderbackend.app.dtos.FileInfoDTO;
import com.builderbackend.app.dtos.FolderDTO;
import com.builderbackend.app.dtos.DocumentSharingDTO;
import com.builderbackend.app.repositories.DocumentSharingRepository;
import com.builderbackend.app.mappers.FileInfoMapper;
import com.builderbackend.app.mappers.FolderMapper;
import com.builderbackend.app.mappers.DocumentSharingMapper;
import com.builderbackend.app.enums.EventType;

import com.builderbackend.app.services.FileUploadService;
import com.builderbackend.app.repositories.FileInfoRepository;
import com.builderbackend.app.repositories.FolderRepository;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.nio.file.*;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Data
public class ClientDocumentSharingService {

    @Autowired
    DocumentSharingRepository documentSharingRepository;

    @Autowired
    FileInfoRepository fileInfoRepository;

    @Autowired
    DocumentSharingMapper documentSharingMapper;

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    ClientNotificationService notificationService;

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    FolderMapper folderMapper;

    //this method is used to create new folders
    @Transactional
    public FolderDTO createFolder(FolderDTO folderDTO) {

        try{
            folderDTO.setFolderId(UUID.randomUUID().toString());
            Folder folder = new Folder();
            folder = folderMapper.convert_FolderDTO_to_Folder(folderDTO);
            folderRepository.save(folder);
        } catch(Exception e) {
            // we will replace the print statments with a log once we come up with a logging strategy
            System.out.println("EXCEPTION - Failed to create new folder due to the following excpetion: " + e);
            System.out.println("FolderDTO: " + folderDTO);
            return null;
        }
        return folderDTO;
    }

    @Transactional
    public DocumentSharingDTO createDocument(DocumentSharingDTO documentSharingDTO, MultipartFile file, FolderDTO folderDTO) {
        try {
            String documentSharingId = initializeDocumentSharing(documentSharingDTO, file);
            if (documentSharingId == null) {
                return null; // or throw a custom exception
            }

            FileInfoDTO newFileInfoDTO = createFileInfoDTO(file, folderDTO, documentSharingId);
            // linking fileInfoDTO to documentSharingDTO
            documentSharingDTO.setFileInfoDTO(newFileInfoDTO);

            FileInfo newfileInfo = saveFileInfo(newFileInfoDTO);
            saveFileToPath(file, newFileInfoDTO.getFileUrl());

            DocumentSharing documentEntity = saveDocumentSharingEntity(documentSharingDTO);

            if (!linkDocumentToFolder(folderDTO, documentEntity)) {
                return null; // or throw a custom exception
            }

            createDocumentSharingNotification(documentSharingDTO, documentSharingId);

            return documentSharingDTO;
        } catch (Exception e) {
            System.out.println("Failed to create new document: " + documentSharingDTO + " with error: " + e);
            return null;
        }
    }
    
    // initializes the DocumentSharingDTO with a unique ID and checks if the file is null.
    private String initializeDocumentSharing(DocumentSharingDTO documentSharingDTO, MultipartFile file) {
        if (file == null) {
            System.out.println("File is null for documentSharingDTO: " + documentSharingDTO);
            return null;
        }
        String documentSharingId = UUID.randomUUID().toString();
        documentSharingDTO.setDocumentSharingId(documentSharingId);
        return documentSharingId;
    }

    //creates a FileInfoDTO and sets its properties
    private FileInfoDTO createFileInfoDTO(MultipartFile file, FolderDTO folderDTO, String documentSharingId) {
        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        String fileId = UUID.randomUUID().toString();
        fileInfoDTO.setFileId(fileId);
        fileInfoDTO.setFileUrl(String.format("%s/%s/%s", folderDTO.getPath(), folderDTO.getFolderId(), fileId));
        fileInfoDTO.setFeatureId(documentSharingId);
        return fileInfoDTO;
    }

    //saves the FileInfo entity to the database
    private FileInfo saveFileInfo(FileInfoDTO fileInfoDTO) {
        FileInfo fileInfo = fileInfoMapper.convert_FileInfoDTO_to_FileInfo(fileInfoDTO);
        try {
            return fileInfoRepository.save(fileInfo);
        } catch (Exception e) {
            System.out.println("Error saving FileInfo: " + fileInfoDTO + " failed with error: " + e);
            throw e;
        }
    }
    
    //saves the file to the specified path
    private void saveFileToPath(MultipartFile file, String path) {
        try {
            fileUploadService.save(file, path);
        } catch (Exception e) {
            System.out.println("Error saving file to path: " + path + " with error: " + e);
            throw e;
        }
    }
    

    //saves the DocumentSharing entity to the database.
    private DocumentSharing saveDocumentSharingEntity(DocumentSharingDTO documentSharingDTO) {
        DocumentSharing documentEntity = documentSharingMapper.convert_DocumentSharingDTO_to_DocumentSharing(documentSharingDTO);
        try {
            return documentSharingRepository.save(documentEntity);
        } catch (Exception e) {
            System.out.println("Error saving DocumentSharing entity: " + documentSharingDTO + " with error: " + e);
            throw e;
        }
    }

    // links the document to a folder
    private boolean linkDocumentToFolder(FolderDTO folderDTO, DocumentSharing documentEntity) {
        Optional<Folder> folder = folderRepository.findById(folderDTO.getFolderId());
        if (folder.isPresent()) {
            folder.get().getDocuments().add(documentEntity);
            try {
                folderRepository.save(folder.get());
                return true;
            } catch (Exception e) {
                System.out.println("Error linking document to folder: " + folderDTO.getFolderId() + " with error: " + e);
                throw e;
            }
        } else {
            System.out.println("Folder not found: " + folderDTO.getFolderId());
            return false;
        }
    }

    // creates a notification for document sharing.
    private void createDocumentSharingNotification(DocumentSharingDTO documentSharingDTO, String documentSharingId) {
        try {
            notificationService.createNotification(documentSharingDTO.getProjectId(), documentSharingId, EventType.EVENT_TYPE_10);
        } catch (Exception e) {
            System.out.println("Error creating notification for document sharing: {}" + documentSharingId + " with error: " + e);
            throw e;
        }
    }

    
     // this method will be used to get all content (both docs and folders) from a folder
     // it will basically return the folder with the folderId
    @Transactional
    public FolderDTO getFolderWithFolderId(String folderId) {
        try{
            Optional<Folder> folder = folderRepository.findById(folderId);
            if(folder.isPresent()){
                FolderDTO folderDTO = folderMapper.convert_Folder_to_folderDTO(folder.get());

                if (folderDTO.getDocuments().size() > 0) {
                    for (DocumentSharingDTO documentSharingDTO : folderDTO.getDocuments()) {
                        List<FileInfo> fileInfo = fileInfoRepository
                                .findByFeatureId(documentSharingDTO.getDocumentSharingId()); 

                        
                        if (fileInfo != null && !fileInfo.isEmpty()) {
                            FileInfoDTO fileDTO = (fileInfoMapper.convert_FileInfo_to_FileInfoDTO(fileInfo.get(0)));
                            documentSharingDTO.setFileInfoDTO(fileDTO);
                        }
                        notificationService.modifyNotificationViewedToTrueForDocId(folderDTO.getProjectId(), documentSharingDTO.getDocumentSharingId() ,EventType.EVENT_TYPE_4);    
                    }
                }

                return folderDTO;
            } else {
                System.out.println("Failed to find folder with folderId: " + folderId);
                return null;
            }

        } catch (Exception e) {
            System.out.println("Exception getting content from folder with FolderId: " + folderId + " : " + e);
            return null;
        }

    }

    // this method will be used to get the default project folder
    @Transactional
    public FolderDTO getDefaultFolderForProjectId(String projectId){
        try{
            String defaultFolderName = String.format("%s-%s", "defaultFolder", projectId );
            List<Folder> folder = folderRepository.findByProjectIdAndFolderName(projectId, defaultFolderName);
            if(folder.size() == 1){
                FolderDTO folderDTO = folderMapper.convert_Folder_to_folderDTO(folder.get(0));

                if (folderDTO.getDocuments().size() > 0) {
                    for (DocumentSharingDTO documentSharingDTO : folderDTO.getDocuments()) {
                        List<FileInfo> fileInfo = fileInfoRepository
                                .findByFeatureId(documentSharingDTO.getDocumentSharingId());

                        if (fileInfo != null && !fileInfo.isEmpty()) {
                            FileInfoDTO fileDTO = (fileInfoMapper.convert_FileInfo_to_FileInfoDTO(fileInfo.get(0)));
                            documentSharingDTO.setFileInfoDTO(fileDTO);
    
                        }
                        notificationService.modifyNotificationViewedToTrueForDocId(projectId, documentSharingDTO.getDocumentSharingId() ,EventType.EVENT_TYPE_4);        
                    }
                }

                return folderDTO;

            } else if(folder.size() > 1) {
                // this should never happen since we validate to makesure no other folder has this name in the controller
                // i guess we will just log the event and return null
                System.out.println("Error - when trying to get default folder for projectId: " + projectId + " we found several with the same name which should NEVER happen");
                return null;
            } else {
                System.out.println("Error - default folder for projectId: " + projectId + " not fountd");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Exception getting content from default folder for projectId: " + projectId + " : " + e);
            return null;
        }
    }

    // deleting folder only if its empty
    @Transactional
    public void deleteFolderIfEmpty(String folderId) {
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        if (!folderOptional.isPresent()) {
            throw new RuntimeException("Folder not found for id: " + folderId);
        }
    
        Folder folder = folderOptional.get();
    
        // Check if the folder is empty
        if (!folder.getSubFolders().isEmpty() || !folder.getDocuments().isEmpty()) {
            throw new RuntimeException("Folder is not empty, cannot delete: " + folderId);
            // Alternatively, you can handle this case as needed.
        }
    
        // If the folder has a parent, remove it from the parent's subFolders list
        Folder parentFolder = folder.getParentFolder();
        if (parentFolder != null) {
            parentFolder.getSubFolders().remove(folder);
            folderRepository.save(parentFolder); // Save the parent folder with updated subFolders list
        }
    
        // The folder is empty and detached from its parent, proceed to delete
        folderRepository.deleteById(folderId);
    }


    //This method is ued to delete a document
    @Transactional
    public void deleteDocument(String documentSharingId) throws Exception{
        Optional<DocumentSharing> documentSharingOptional = documentSharingRepository.findById(documentSharingId);
        if (!documentSharingOptional.isPresent()) {
            throw new RuntimeException("Document not found for id: " + documentSharingId);
        }
    
        DocumentSharing documentSharing = documentSharingOptional.get();
        Optional<Folder> folder = folderRepository.findById(documentSharing.getFolder().getFolderId());
        if (!folder.isPresent()) {
            System.out.println("Failed to find which folder documentSharing should be remove from. DocumentSharingId: " + documentSharingId);
            throw new RuntimeException("Folder not found for DocumentSharingId: " + documentSharingId);
        }
    
        List<FileInfo> fileInfoList = fileInfoRepository.findByFeatureId(documentSharingId);
        if (fileInfoList.isEmpty()) {
            System.out.println("failed to find the fileInfo record for documentSharingId: " + documentSharing);
            throw new RuntimeException("FileInfo not found for DocumentSharingId: " + documentSharingId);
        }
    
        // Remove the documentSharing record from the folder
        folder.get().getDocuments().remove(documentSharing);
        folderRepository.save(folder.get());
    
        // Delete the FileInfo record
        fileInfoRepository.delete(fileInfoList.get(0));
    
        // Delete the DocumentSharing record
        documentSharingRepository.deleteById(documentSharingId);

        //Delete the notification associated with the document 
        notificationService.deleteNotificationForDocId(folder.get().getProject().getProjectId(), documentSharingId, EventType.EVENT_TYPE_10);

        // Delete the actual file
        // can throw exceptions
        deleteFileWithPermissions(fileInfoList.get(0).getFileUrl());
    }

    private void deleteFileWithPermissions(String fileUrl) throws Exception {
        Path pathToFile = Paths.get("/moonlightUploads/"+fileUrl);
        if (Files.isWritable(pathToFile)) {
            try {
                Files.deleteIfExists(pathToFile);
            } catch (IOException e) {
                throw new RuntimeException("Error deleting the file: " + "/moonlightUploads/"+fileUrl, e);
            }
        } else {
            // this is temp not a good practice to sudo rm tbh so lets find a better way.
            // one thing is when saving a file make sure we set it the proper permission so
            // we dont have to sudo rm
            // the file is not writable
            // You can attempt to change permissions or delete with elevated permissions.
            // This is platform dependent and risky. Not recommended for server
            // applications.
            // For example, on UNIX systems:
            try {
                Process process = Runtime.getRuntime().exec("sudo rm " + fileUrl);
                process.waitFor();
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete file with elevated permissions: " + fileUrl, e);
            }
        }
    }
}
