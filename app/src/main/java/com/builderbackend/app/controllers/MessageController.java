package com.builderbackend.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.builderbackend.app.dtos.ConversationDTO;
import com.builderbackend.app.dtos.MessageDTO;
import com.builderbackend.app.services.MessageService;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Get all chats for a given clent
    @GetMapping("/AllChatsWithClient")
    public ResponseEntity<List<ConversationDTO>> getAllChatsWithClient(@RequestParam("clientId") String clientId) {
        List<ConversationDTO> conversations = messageService.getAllChatsWithClient(clientId);
        return ResponseEntity.ofNullable(conversations);
    }

    // Get all chats for a given employee
    @GetMapping("/AllChats")
    public ResponseEntity<List<ConversationDTO>> getAllChatsforEmployee(@RequestParam("employeeId") String employeeId) {
        List<ConversationDTO> conversations = messageService.getAllChats(employeeId);
        return ResponseEntity.ofNullable(conversations);
    }

    // Get messages for a conversation with pagination
    @GetMapping("/getMessages")
    public ResponseEntity<Page<MessageDTO>> getMessages(
            @RequestParam("conversationId") String conversationId,
            @RequestParam("userId") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("sentAt").descending());
        Page<MessageDTO> messages = messageService.getMessagesByConversationId(conversationId, userId, pageRequest);
        return ResponseEntity.ofNullable(messages);
    }

    // Post a new message
    @PostMapping("/send")
    public ResponseEntity<MessageDTO> postMessage(@RequestBody MessageDTO message) {
        MessageDTO savedMessage = messageService.saveMessage(message);
        return ResponseEntity.ofNullable(savedMessage);
    }

    // Start a new chat
    @PostMapping("/chat")
    public ResponseEntity<?> startChat(@RequestParam("clientId") String clientId,
            @RequestParam("employeeId") String employeeId, @RequestParam("projectId") String projectId) {
        try {
            ConversationDTO newConversation = messageService.initConversation(clientId, employeeId, projectId);
            return ResponseEntity.ofNullable(newConversation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("This Conversation already exists");
        }
    }
}