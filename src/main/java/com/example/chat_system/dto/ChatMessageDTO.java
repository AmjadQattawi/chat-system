package com.example.chat_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {

    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime timestamp;
    private Long roomId;

}
