package com.example.chat_system.service;

import com.example.chat_system.dto.ChatMessageDTO;
import com.example.chat_system.dto.ChatRoomDTO;
import com.example.chat_system.entity.ChatMessage;
import com.example.chat_system.entity.ChatRoom;
import com.example.chat_system.exception.DuplicateResourceException;
import com.example.chat_system.mapper.ChatMapper;
import com.example.chat_system.mapper.ChatRoomMapper;
import com.example.chat_system.repository.ChatMessageRepository;
import com.example.chat_system.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;

    public ChatRoomDTO createRoom(String name){
        if (chatRoomRepository.existsByName(name)) {
            throw new DuplicateResourceException("Room '" + name + "' already exists!");
        }
        ChatRoom chatRoom=new ChatRoom();
        chatRoom.setName(name);
        chatRoom.setCreatedAt(LocalDateTime.now());
        ChatRoom saved=chatRoomRepository.save(chatRoom);
        return chatRoomMapper.toDTO(saved);
    }


    public List<ChatRoomDTO> getAllRooms(){
        List<ChatRoom> chatRooms=chatRoomRepository.findAll();
        return chatRoomMapper.toDTO(chatRooms);
    }

    public List<ChatMessageDTO> getRoomHistory(Long roomId){
        List<ChatMessage> chatMessages=chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
        return chatMapper.toDTO(chatMessages);

    }




}
