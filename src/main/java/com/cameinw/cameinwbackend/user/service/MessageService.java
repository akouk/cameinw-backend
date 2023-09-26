package com.cameinw.cameinwbackend.user.service;

import com.cameinw.cameinwbackend.user.dto.MessageDTO;
import com.cameinw.cameinwbackend.user.model.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {
//    Optional<List<Message>> getMessagesPerUser(Integer userId);
//    Optional<List<Message>> getChatHistory(Integer userId1, Integer userId2);
//    Message createMessage(Integer senderId, Integer receiverId, String message);



        Optional<List<Message>> getMessagesPerUser(Integer userId);
        Optional<List<Message>> getChatHistory(Integer userId1, Integer userId2);
        MessageDTO createMessage(Integer senderId, Integer receiverId, String message);


}
