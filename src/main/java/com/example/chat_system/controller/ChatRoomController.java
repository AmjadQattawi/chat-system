package com.example.chat_system.controller;

import com.example.chat_system.dto.ChatMessageDTO;
import com.example.chat_system.dto.ChatRoomDTO;
import com.example.chat_system.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    @PostMapping("createRoom")
    public ResponseEntity<ChatRoomDTO> createRoom(@RequestParam String name) {
        return ResponseEntity.ok(chatRoomService.createRoom(name));
    }

    @GetMapping("getAllRooms")
    public ResponseEntity<List<ChatRoomDTO>> getAllRooms() {
        return ResponseEntity.ok(chatRoomService.getAllRooms());
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getRoomHistory(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatRoomService.getRoomHistory(roomId));
    }



}
