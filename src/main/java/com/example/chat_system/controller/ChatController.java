package com.example.chat_system.controller;

import com.example.chat_system.dto.ChatMessageDTO;
import com.example.chat_system.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

//    @MessageMapping("/send")
//    @SendTo("/topic/messages")
//    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
//        return chatMessageService.saveMessage(message);
//    }


    @MessageMapping("/room/{roomId}/send")
    public void sendMessage(
            @DestinationVariable Long roomId,
            ChatMessageDTO message
    ) {
        message.setRoomId(roomId);
        ChatMessageDTO saved = chatMessageService.saveMessage(message);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, saved);
    }



}
