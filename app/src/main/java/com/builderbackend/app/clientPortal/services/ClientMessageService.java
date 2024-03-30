package com.builderbackend.app.clientPortal.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.builderbackend.app.enums.EventType;
import com.builderbackend.app.dtos.ConversationDTO;
import com.builderbackend.app.dtos.MessageDTO;
import com.builderbackend.app.repositories.ConversationRepository;
import com.builderbackend.app.repositories.MessageRepository;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.mappers.ConversationMapper;
import com.builderbackend.app.mappers.MessageMapper;
import com.builderbackend.app.mappers.UserMapper;
import com.builderbackend.app.models.Conversation;
import com.builderbackend.app.models.Message;
import com.builderbackend.app.models.Participant;

@Service
public class ClientMessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public ClientMessageService(MessageRepository messageRepository,
            MessageMapper messageMapper,
            ConversationRepository conversationRepository,
            ConversationMapper conversationMapper,
            UserRepository userRepository,
            UserMapper userMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.conversationRepository = conversationRepository;
        this.conversationMapper = conversationMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Autowired
    ClientNotificationService notificationService;

    public Page<MessageDTO> getMessagesByConversationId(String conversationId, PageRequest pageRequest) {
        // if not found, repo retuerns null
        // messageMapper has null check and will return null too

        Optional<Conversation> conversation = conversationRepository.findById(conversationId);

        if (conversation.isPresent()) {
            String projectId = conversation.get().getProject().getProjectId();

            notificationService.modifyNotificationViewedToTrue(projectId, EventType.EVENT_TYPE_1);

        }
        return messageRepository.findByConversation_ConversationId(conversationId, pageRequest)
                .map(messageMapper::convert_Message_to_MessageDTO);
    }

    public MessageDTO saveMessage(MessageDTO messageDto) {
        // setting a UUID
        messageDto.setMessageId(UUID.randomUUID().toString());

        Message message = messageMapper.convert_MessageDTO_to_Message(messageDto);
        messageRepository.save(message);

        Optional<Conversation> conversation = conversationRepository.findById(messageDto.getConversationId());

        if (conversation.isPresent()) {
            String projectId = conversation.get().getProject().getProjectId();

            List<Participant> listOfUserIds = conversation.get().getParticipants();

            for (Participant participant : listOfUserIds) {

                String userId = participant.getUser().getUserId();
                if (!userId.equals(messageDto.getSenderId())) {
                    notificationService.createMessageNotification(projectId, userId, messageDto.getMessageId(),
                            EventType.EVENT_TYPE_7);
                }
            }

        }
        return messageDto;
    }

    public List<ConversationDTO> getAllChatsWithClient(String clientId) {
        List<Conversation> conversations = conversationRepository.findByParticipants_User_UserId(clientId);
        return conversations.stream()
                .map(conversationMapper::convert_Conversation_to_ConversationDTO)
                .collect(Collectors.toList());
    }

}
