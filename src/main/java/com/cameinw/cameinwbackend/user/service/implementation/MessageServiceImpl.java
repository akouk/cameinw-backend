package com.cameinw.cameinwbackend.user.service.implementation;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.user.dto.MessageDTO;
import com.cameinw.cameinwbackend.user.model.Message;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.MessageRepository;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.user.service.MessageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

//    @Override
//    public Optional<List<Message>> getMessagesPerUser(Integer userId) {
//        return userRepository.findById(userId)
//                .map(user -> messageRepository.findMessagesPerUser(userId));
//    }

    @Override
    public Optional<List<Message>> getMessagesPerUser(Integer userId) {
        return userRepository.findById(userId)
                .map(user -> messageRepository.findMessagesPerUser(userId));
    }

//    @Override
//    public Optional<List<Message>> getChatHistory(Integer userId1, Integer userId2) {
//        return userRepository.findById(userId1).flatMap(user1 ->
//                userRepository.findById(userId2).map(user2 ->
//                        messageRepository.findMessagesBetweenUsers(user1.getId(), user2.getId())));
//    }

    @Override
    public Optional<List<Message>> getChatHistory(Integer userId1, Integer userId2) {
        return userRepository.findById(userId1).flatMap(user1 ->
                userRepository.findById(userId2).map(user2 ->
                        messageRepository.findMessagesBetweenUsers(user1.getId(), user2.getId())));
    }

//    @Override
//    @Transactional
//    public Message createMessage(Integer senderId, Integer receiverId, String message) {
//        User sender = userRepository.findById(senderId)
//                .orElseThrow(() -> new ResourceNotFoundException("Sender with id " + senderId + " not found"));
//
//        User receiver = userRepository.findById(receiverId)
//                .orElseThrow(() -> new ResourceNotFoundException("Receiver with id " + receiverId + " not found"));
//
//
//        Message newMessage = new Message();
//        newMessage.setSender(sender);
//        newMessage.setReceiver(receiver);
//        newMessage.setMessage(message);
//
//
//        // Set the timestamp to the current date and time
//        newMessage.setTimestamp(LocalDateTime.now());
//
//        return messageRepository.save(newMessage);
//    }

    @Override
    @Transactional
    public MessageDTO createMessage(Integer senderId, Integer receiverId, String message) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender with id " + senderId + " not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver with id " + receiverId + " not found"));

        Message newMessage = new Message();
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setMessage(message);

        // Set the timestamp to the current date and time
        newMessage.setTimestamp(LocalDateTime.now());

        Message savedMessage = messageRepository.save(newMessage);
        return mapMessageToDTO(savedMessage);
    }

    private MessageDTO mapMessageToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSenderId(message.getSender().getId());
        messageDTO.setSenderUsername(message.getSender().getTheUserName());
        messageDTO.setSenderImageName(message.getSender().getImageName());
        messageDTO.setReceiverId(message.getReceiver().getId());
        messageDTO.setReceiverUsername(message.getReceiver().getTheUserName());
        messageDTO.setReceiverImageName(message.getReceiver().getImageName());
        messageDTO.setMessage(message.getMessage());
        messageDTO.setMessageTimestamp(message.getTimestamp());
        return messageDTO;
    }

}
