package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.MessageDTO;
import com.builderbackend.app.repositories.ConversationRepository;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.models.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageMapper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConversationRepository conversationRepository;

    public MessageDTO convert_Message_to_MessageDTO(Message message) {
        if (message == null) {
            return null;
        }

        MessageDTO dto = new MessageDTO();
        dto.setMessageId(message.getMessageId());
        dto.setConversationId(message.getConversation() != null ? message.getConversation().getConversationId() : null);
        dto.setSenderId(message.getSender() != null ? message.getSender().getUserId() : null);
        dto.setMessageText(message.getMessageText());
        dto.setSentAt(message.getSentAt());
        dto.setIsRead(message.getIsRead());
        return dto;
    }

    public Message convert_MessageDTO_to_Message(MessageDTO dto) {
        if (dto == null) {
            return null;
        }

        Message message = new Message();
        message.setMessageId(dto.getMessageId());
        message.setSender(userRepository.findByUserId(dto.getSenderId()));
        message.setConversation(conversationRepository.findByConversationId(dto.getConversationId()));
        message.setMessageText(dto.getMessageText());
        message.setSentAt(dto.getSentAt());
        message.setIsRead(dto.getIsRead());
        return message;
    }
}