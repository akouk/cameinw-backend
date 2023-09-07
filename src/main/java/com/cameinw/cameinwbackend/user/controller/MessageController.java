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
@RequestMapping("/api")
public class MessageController {
    private final MessageService messageService;


    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/users/{user_id}/messages") // !!!CHECK OK !!
    public ResponseEntity<List<Message>> getUsersMessages(@PathVariable("user_id") Integer userId) {
        Optional<List<Message>> messages = messageService.getMessagesPerUser(userId);
        return messages
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{user_id}/messages/{otherUser_id}") // !!!CHECK OK !!
    public ResponseEntity<List<Message>> getUsersChat(
            @PathVariable("user_id") Integer userId,
            @PathVariable("otherUser_id") Integer otherUserId
    ) {
        Optional<List<Message>> chatHistory = messageService.getChatHistory(userId, otherUserId);
        return chatHistory
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/users/{user_id}/messages/{otherUser_id}") // !!! CHECK OK !!
    public ResponseEntity<Message> createMessage(
            @PathVariable("user_id") Integer userId,
            @PathVariable("otherUser_id") Integer otherUserId,
            @RequestParam("message") String messageText) {
        try {
            Message newMessage = messageService.createMessage(userId, otherUserId, messageText);
            return ResponseEntity.ok(newMessage);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
