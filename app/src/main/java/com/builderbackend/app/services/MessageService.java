package com.builderbackend.app.services;

import java.time.Instant;
import java.util.ArrayList;
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
import com.builderbackend.app.dtos.ParticipantDTO;
import com.builderbackend.app.repositories.ConversationRepository;
import com.builderbackend.app.repositories.MessageRepository;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.exceptions.ConversationAlreadyExistsException;
import com.builderbackend.app.mappers.ConversationMapper;
import com.builderbackend.app.mappers.MessageMapper;
import com.builderbackend.app.mappers.UserMapper;
import com.builderbackend.app.models.Conversation;
import com.builderbackend.app.models.Message;
import com.builderbackend.app.models.Notification;
import com.builderbackend.app.models.Participant;
import com.builderbackend.app.repositories.NotificationRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public MessageService(MessageRepository messageRepository,
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
    NotificationService notificationService;

    @Autowired
    NotificationRepository notificationRepository;

    public List<ConversationDTO> getAllChats(String employeeId) {
        List<Conversation> conversations = conversationRepository.findByParticipants_User_UserId(employeeId);
        return conversations.stream()
                .map(conversationMapper::convert_Conversation_to_ConversationDTO)
                .collect(Collectors.toList());
    }

    public Page<MessageDTO> getMessagesByConversationId(String conversationId, String userId, PageRequest pageRequest) {
        // if not found, repo retuerns null
        // messageMapper has null check and will return null too
        Optional<Conversation> conversation = conversationRepository.findById(conversationId);

        if (conversation.isPresent()) {
            String projectId = conversation.get().getProject().getProjectId();

            List<Notification> notificationList = notificationRepository
                    .findByProjectProjectIdAndEventTypeAndReceiverUserId(projectId, EventType.EVENT_TYPE_7, userId);

            if (!notificationList.isEmpty()) {
                // Accessing the first element of the list to get userId
                String receiverUserId = notificationList.get(0).getReceiver().getUserId();

                notificationService.modifyNotificationViewedToTrueForUserId(projectId, receiverUserId,
                        EventType.EVENT_TYPE_7);
            }
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

            notificationService.createMessageNotification(projectId, messageDto.getSenderId(),
                    messageDto.getMessageId(),
                    EventType.EVENT_TYPE_1);
        }

        return messageDto;

    }

    public ConversationDTO startNewConversation(ConversationDTO conversationDto) {
        // setting a UUID
        String listId = UUID.randomUUID().toString();
        conversationDto.setConversationId(listId);
        Conversation conversation = conversationMapper.convert_ConversationDTO_to_Conversation(conversationDto);

        // link participant to conversation
        // unsure if this is correct...
        for (Participant p : conversation.getParticipants()) {
            p.setConversation(conversation);
        }

        Conversation newConversation = conversationRepository.save(conversation);

        return conversationMapper.convert_Conversation_to_ConversationDTO(newConversation);
    }

    public List<ConversationDTO> getAllChatsWithClient(String clientId) {
        List<Conversation> conversations = conversationRepository.findByParticipants_User_UserId(clientId);
        return conversations.stream()
                .map(conversationMapper::convert_Conversation_to_ConversationDTO)
                .collect(Collectors.toList());
    }

    /**
     * this will be used to init the conversation/chat between the project owner
     * (which is an employee) and a client when a new project is created
     * 
     * @return
     */
    public ConversationDTO initConversation(String clientId, String employeeId, String projectId)
            throws ConversationAlreadyExistsException {
        // now when we create a project, we also need to create a new chat conversation
        Instant curTime = Instant.now();

        // to do - check if the clientId/employeeId combo already exists
        List<Conversation> convList = conversationRepository.findByProject_ProjectId(projectId);
        List<String> userList = convList.stream()
                .flatMap(conversation -> conversation.getParticipants().stream())
                .map(participant -> participant.getUser().getUserId())
                .collect(Collectors.toList());

        if (userList.contains(employeeId)) {
            // this chat alrady exists
            throw new ConversationAlreadyExistsException("This conversation already exists");
        }

        ConversationDTO conversationDTO = new ConversationDTO();
        ParticipantDTO clientParticipantDTO = new ParticipantDTO();
        ParticipantDTO employeeParticipantDTO = new ParticipantDTO();
        List<ParticipantDTO> participantList = new ArrayList<>();
        // List<MessageDTO> messageList = new ArrayList<>();

        String conversationId = UUID.randomUUID().toString();
        conversationDTO.setConversationId(conversationId);
        conversationDTO.setConversationName("Default");
        conversationDTO.setCreatedAt(curTime);
        conversationDTO.setProjectId(projectId);
        // conversationDTO.setMessages(messageList);

        clientParticipantDTO.setParticipantId(UUID.randomUUID().toString());
        clientParticipantDTO.setConversationId(conversationId);
        clientParticipantDTO.setJoinedAt(curTime);
        clientParticipantDTO.setUserDTO(
                userMapper.convert_User_to_UserClientDTO(
                        userRepository.findByUserId(clientId)));

        employeeParticipantDTO.setParticipantId(UUID.randomUUID().toString());
        employeeParticipantDTO.setConversationId(conversationId);
        employeeParticipantDTO.setJoinedAt(curTime);
        employeeParticipantDTO.setUserDTO(
                userMapper.convert_User_to_UserClientDTO(
                        userRepository.findByUserId(employeeId)));

        participantList.add(clientParticipantDTO);
        participantList.add(employeeParticipantDTO);

        conversationDTO.setParticipantDTOs(participantList);

        ConversationDTO responseConversationDTO = startNewConversation(conversationDTO);

        if (responseConversationDTO == null) {
            // faild to create new chat
            // todo: convert this to log statment, possibly throw exception
            System.out.println("failed to create new chat conversation");
            return null;
        }

        return responseConversationDTO;
    }
}