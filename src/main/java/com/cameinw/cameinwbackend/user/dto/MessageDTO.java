package com.cameinw.cameinwbackend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Integer senderId;
    private String senderUsername;
    private String senderImageName;
    private Integer receiverId;
    private String receiverUsername;
    private String receiverImageName;
    private String message;
    private LocalDateTime messageTimestamp;
}
