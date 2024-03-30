package com.builderbackend.app.services;

import lombok.Data;
import java.lang.Exception;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.builderbackend.app.models.Project;
import com.builderbackend.app.dtos.ConversationDTO;
import com.builderbackend.app.dtos.FolderDTO;
import com.builderbackend.app.dtos.ParticipantDTO;
import com.builderbackend.app.dtos.ProjectDTO;
import com.builderbackend.app.repositories.ProjectRepository;
import com.builderbackend.app.mappers.ProjectMapper;
import com.builderbackend.app.mappers.UserMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Data
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    MessageService messageService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    DocumentSharingService documentSharingService;

    public ProjectDTO createProject(ProjectDTO projectDTO, String userIdCookie) {
        // Setting UUID for project
        String projectId = UUID.randomUUID().toString();
        projectDTO.setProjectId(projectId);

        projectDTO.setOwnerId(userIdCookie);

        // Convert DTO to Entity
        Project projectEntity = projectMapper.convert_ProjectDTO_to_Project(projectDTO);

        // Save to the database
        projectRepository.save(projectEntity);

        //init a new conversation/chat for the project
        initConversation(projectEntity);

        //init a new base folder for the project
        initDefaultSharedDocsFolderForNewProject(projectEntity);

        return projectDTO;
    }

    // GET all projects from client Id assuming clients may have multiple projects
    public List<ProjectDTO> getProjectsFromClientId(String clientId) {
        List<Project> projectList = new ArrayList<Project>();

        try {
            projectList = projectRepository.findByClientUserId(clientId);

        } catch (Exception e) {
            // should probably log this
            System.out.println("Exception: " + e);
            throw e;
        }

        List<ProjectDTO> ProjectDTOList = new ArrayList<ProjectDTO>();

        for (Project projectEntity : projectList) {
            ProjectDTOList.add(projectMapper.convert_Project_to_ProjectDTO(projectEntity));
        }
        return ProjectDTOList;

    }

    public ProjectDTO getProjectFromProjectId(String projectId) {
        Project project = new Project();

        try {
            project = projectRepository.getReferenceById(projectId);
        }

        catch (Exception e) {
            // should probably log this
            System.out.println("Exception: " + e);
            throw e;
        }
        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO = projectMapper.convert_Project_to_ProjectDTO(project);
        return projectDTO;

    }

    /**
     * this will be used to init the conversation/chat between the project owner 
     * (which is an employee) and a client when a new project is created 
     * @return
     */
    private boolean initConversation(Project projectEntity){
         // now when we create a project, we also need to create a new chat conversation
         Instant curTime = Instant.now(); 


         ConversationDTO conversationDTO = new ConversationDTO();
         ParticipantDTO clientParticipantDTO = new ParticipantDTO();
         ParticipantDTO employeeParticipantDTO = new ParticipantDTO();
         List<ParticipantDTO> participantList = new ArrayList<>();
         //List<MessageDTO> messageList = new ArrayList<>();
 

         String conversationId = UUID.randomUUID().toString();
         conversationDTO.setConversationId(conversationId);
         conversationDTO.setConversationName("Default");
         conversationDTO.setCreatedAt(curTime);
         conversationDTO.setProjectId(projectEntity.getProjectId());
         //conversationDTO.setMessages(messageList);

         clientParticipantDTO.setParticipantId(UUID.randomUUID().toString());
         clientParticipantDTO.setConversationId(conversationId);
         clientParticipantDTO.setJoinedAt(curTime);
         clientParticipantDTO.setUserDTO(userMapper.convert_User_to_UserClientDTO(projectEntity.getClient()));

 
         employeeParticipantDTO.setParticipantId(UUID.randomUUID().toString());
         employeeParticipantDTO.setConversationId(conversationId);
         employeeParticipantDTO.setJoinedAt(curTime);
         employeeParticipantDTO.setUserDTO(userMapper.convert_User_to_UserClientDTO(projectEntity.getOwner()));
 
         participantList.add(clientParticipantDTO);
         participantList.add(employeeParticipantDTO);
 
         conversationDTO.setParticipantDTOs(participantList);

         ConversationDTO responseConversationDTO = messageService.startNewConversation(conversationDTO);

 
         if(responseConversationDTO == null){
             // faild to create new chat
             // todo: convert this to log statment, possibly throw exception
             System.out.println("failed to create new chat conversation");
             return false;
         }

         return true;
    }

    public ProjectDTO editProjectInfo (ProjectDTO projectDTO){

        String projectId = projectDTO.getProjectId();

        Project project = projectRepository.findById(projectId).orElse(null);

        project.setAddress(projectDTO.getAddress());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setDescription(projectDTO.getDescription());

        projectRepository.save(project);

        return projectMapper.convert_Project_to_ProjectDTO(project);

    }

    // method will init a new base folder for the projects shared docs feature
    private void initDefaultSharedDocsFolderForNewProject(Project projectEntity){

        FolderDTO folderDTO = new FolderDTO();
        folderDTO.setBusinessId(projectEntity.getBusiness().getBusinessId());
        folderDTO.setProjectId(projectEntity.getProjectId());
        folderDTO.setFolderName(String.format("%s-%s", "defaultFolder", projectEntity.getProjectId()));
        folderDTO.setCreatedDate(null); // todo: possible update this to actual creadted timestamp eventually
        folderDTO.setCreatedBy(projectEntity.getOwner().getUserId());
        folderDTO.setParentFolderId(null); // this is the root folder, so parent folder is null
        folderDTO.setPath(String.format("uploads/%s/%s/%s", projectEntity.getBusiness().getBusinessId(),
            projectEntity.getProjectId(), "documentSharing"));

        documentSharingService.createFolder(folderDTO);
    }

}
