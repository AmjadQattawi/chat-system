package com.example.chat_system.controller;

import com.example.chat_system.dto.ChatMessageDTO;
import com.example.chat_system.dto.TypingEventDTO;
import com.example.chat_system.entity.ChatRoom;
import com.example.chat_system.repository.ChatRoomRepository;
import com.example.chat_system.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/room/{roomId}/send")
    public void sendMessage(
            @DestinationVariable Long roomId,
            ChatMessageDTO message,
            Principal principal
    ) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        boolean isMember = room.getMembers().stream()
                .anyMatch(u -> u.getUserName().equals(principal.getName()));

        if (!isMember) return;

        message.setSender(principal.getName());
        message.setRoomId(roomId);
        ChatMessageDTO saved = chatMessageService.saveMessage(message);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, saved);
    }

    @MessageMapping("/rooms/{roomId}/typing")
    public void handleTyping(@DestinationVariable Long roomId,
                             TypingEventDTO event,
                             Principal principal) {
        event.setSender(principal.getName());
        messagingTemplate.convertAndSend(
                "/topic/rooms/" + roomId + "/typing", event
        );

    }


}
