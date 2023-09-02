package com.cameinw.cameinwbackend.user.controller;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.user.model.Message;
import com.cameinw.cameinwbackend.user.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;


    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;

    }

    @GetMapping("/{user_id}")
    public ResponseEntity<List<Message>> getUsersMessages(@PathVariable("user_id") Integer userId) {
        Optional<List<Message>> messages = messageService.getMessagesPerUser(userId);
        return messages
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{user_Id1}/{user_Id2}/chat")
    public ResponseEntity<List<Message>> getUsersChat(
            @PathVariable("user_Id1") Integer userId1,
            @PathVariable("user_Id2") Integer userId2
    ) {
        Optional<List<Message>> chatHistory = messageService.getChatHistory(userId1, userId2);
        return chatHistory
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{sender_id}/{receiver_id}/send")
    public ResponseEntity<String> createMessage(
            @PathVariable("sender_id") Integer senderId,
            @PathVariable("receiver_id") Integer receiverId,
            @RequestParam("message") String messageText) {

        try {
            Message newMessage = messageService.createMessage(senderId, receiverId, messageText);
            return ResponseEntity.status(HttpStatus.CREATED).body("Message successfully created."); // STATUS: 201
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
