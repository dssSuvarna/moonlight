package com.builderbackend.app.clientPortal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.builderbackend.app.clientPortal.services.ClientMessageService;

import com.builderbackend.app.dtos.ConversationDTO;
import com.builderbackend.app.dtos.MessageDTO;

import java.util.List;

@RestController
@RequestMapping("/clientMessages")
public class ClientMessageController {

    private final ClientMessageService clientMessageService;

    @Autowired
    public ClientMessageController(ClientMessageService clientMessageService) {
        this.clientMessageService = clientMessageService;
    }

    // Get all chats for a given clent
    @GetMapping("/AllChatsWithClient")
    public ResponseEntity<List<ConversationDTO>> getAllChatsWithClient(@RequestParam("clientId") String clientId) {
        List<ConversationDTO> conversations = clientMessageService.getAllChatsWithClient(clientId);
        return ResponseEntity.ofNullable(conversations);
    }
    
    // Get messages for a conversation with pagination
    @GetMapping("/getMessages")
    public ResponseEntity<Page<MessageDTO>> getMessages(
            @RequestParam("conversationId") String conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("sentAt").descending());
        Page<MessageDTO> messages = clientMessageService.getMessagesByConversationId(conversationId, pageRequest);
        return ResponseEntity.ofNullable(messages);
    }

    // Post a new message
    @PostMapping("/send")
    public ResponseEntity<MessageDTO> postMessage(@RequestBody MessageDTO message) {
        MessageDTO savedMessage = clientMessageService.saveMessage(message);
        return ResponseEntity.ofNullable(savedMessage);
    }
}
