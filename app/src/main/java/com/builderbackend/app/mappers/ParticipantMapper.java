package com.builderbackend.app.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.builderbackend.app.dtos.ParticipantDTO;
import com.builderbackend.app.repositories.ConversationRepository;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.models.Participant;

@Service
public class ParticipantMapper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    UserMapper userMapper;

    public ParticipantDTO convert_Participant_to_ParticipantDTO(Participant participant) {
        if (participant == null) {
            return null;
        }

        ParticipantDTO dto = new ParticipantDTO();
        dto.setParticipantId(participant.getParticipantId());
        dto.setConversationId(participant.getConversation() != null ? participant.getConversation().getConversationId() : null);
        dto.setUserDTO(userMapper.convert_User_to_UserClientDTO(participant.getUser()));
        dto.setJoinedAt(participant.getJoinedAt());
        return dto;
    }

    public Participant convert_ParticipantDTO_to_Participant(ParticipantDTO dto) {
        if (dto == null) {
            return null;
        }
        Participant participant = new Participant();
        participant.setParticipantId(dto.getParticipantId());
        participant.setUser(userRepository.findByUserId(dto.getUserDTO().getUserId()));
        participant.setJoinedAt(dto.getJoinedAt());

        return participant;
    }
}
