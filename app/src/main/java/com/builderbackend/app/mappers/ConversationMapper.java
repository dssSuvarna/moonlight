package com.builderbackend.app.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.builderbackend.app.dtos.ConversationDTO;
import com.builderbackend.app.repositories.ProjectRepository;
import com.builderbackend.app.models.Conversation;

import java.util.stream.Collectors;

@Service
public class ConversationMapper {

    @Autowired
    private ParticipantMapper participantMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private ProjectRepository projectRepository;


    public ConversationDTO convert_Conversation_to_ConversationDTO(Conversation conversation) {
        if (conversation == null) {
            return null;
        }

        ConversationDTO dto = new ConversationDTO();
        dto.setConversationId(conversation.getConversationId());
        dto.setConversationName(conversation.getConversationName());
        dto.setProjectId(conversation.getProject().getProjectId());
        dto.setCreatedAt(conversation.getCreatedAt());
        dto.setParticipantDTOs(conversation.getParticipants().stream()
            .map(participantMapper::convert_Participant_to_ParticipantDTO)
            .collect(Collectors.toList()));
        //dto.setMessages(conversation.getMessages().stream()
            //.map(messageMapper::convert_Message_to_MessageDTO)
            //.collect(Collectors.toList()));
        return dto;
    }

    public Conversation convert_ConversationDTO_to_Conversation(ConversationDTO dto) {
        if (dto == null) {
            return null;
        }

        Conversation conversation = new Conversation();
       
        conversation.setConversationId(dto.getConversationId());
        conversation.setConversationName(dto.getConversationName());
        conversation.setProject(projectRepository.findByProjectId(dto.getProjectId()));
        conversation.setCreatedAt(dto.getCreatedAt());

        conversation.setParticipants(dto.getParticipantDTOs().stream()
            .map(participantMapper::convert_ParticipantDTO_to_Participant)
            .collect(Collectors.toList()));

        //conversation.setMessages(dto.getMessages().stream()
            //.map(messageMapper::convert_MessageDTO_to_Message)
            //.collect(Collectors.toList()));

        return conversation;
    }
}
