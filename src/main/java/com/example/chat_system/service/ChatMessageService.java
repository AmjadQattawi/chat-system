package com.example.chat_system.service;

import com.example.chat_system.dto.ChatMessageDTO;
import com.example.chat_system.entity.ChatMessage;
import com.example.chat_system.mapper.ChatMapper;
import com.example.chat_system.repository.ChatMessageRepository;
import com.example.chat_system.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMapper chatMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageDTO saveMessage(ChatMessageDTO chatMessageDTO){
        chatRoomRepository.findById(chatMessageDTO.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found!"));

        ChatMessage chatMessage=chatMapper.toEntity(chatMessageDTO);
        chatMessage.setTimestamp(LocalDateTime.now());
        ChatMessage saved=chatMessageRepository.save(chatMessage);
        return chatMapper.toDTO(saved);

    }


}
