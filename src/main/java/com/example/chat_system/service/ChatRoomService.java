package com.example.chat_system.service;

import com.example.chat_system.dto.ChatMessageDTO;
import com.example.chat_system.dto.ChatRoomDTO;
import com.example.chat_system.entity.ChatMessage;
import com.example.chat_system.entity.ChatRoom;
import com.example.chat_system.entity.User;
import com.example.chat_system.exception.DuplicateResourceException;
import com.example.chat_system.exception.RoomNotFoundException;
import com.example.chat_system.mapper.ChatMapper;
import com.example.chat_system.mapper.ChatRoomMapper;
import com.example.chat_system.repository.ChatMessageRepository;
import com.example.chat_system.repository.ChatRoomRepository;
import com.example.chat_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserRepository userRepository;

    public ChatRoomDTO createRoom(String name){
        if (chatRoomRepository.existsByName(name)) {
            throw new DuplicateResourceException("Room '" + name + "' already exists!");
        }
        ChatRoom chatRoom=new ChatRoom();
        User user=userRepository.findByUserName(getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        chatRoom.setName(name);
        chatRoom.getMembers().add(user); //add user to the room
        ChatRoom saved=chatRoomRepository.save(chatRoom);
        return chatRoomMapper.toDTO(saved);
    }


    public List<ChatRoomDTO> getAllRooms() {
        String currentUser = getCurrentUsername();
        return chatRoomRepository.findAll()
                .stream()
                .map(room -> {
                    ChatRoomDTO dto = chatRoomMapper.toDTO(room);
                    dto.setMember(room.getMembers().stream()
                            .anyMatch(u -> u.getUserName().equals(currentUser)));
                    return dto;
                })
                .toList();
    }

    public List<ChatMessageDTO> getRoomHistory(Long roomId){
        List<ChatMessage> chatMessages=chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
        return chatMapper.toDTO(chatMessages);

    }

    public void joinRoom(Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found!"));

        User user = userRepository.findByUserName(getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (room.getMembers().contains(user)) {
            throw new DuplicateResourceException("Already a member!");
        }

        room.getMembers().add(user);
        chatRoomRepository.save(room);
    }
    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }




}
