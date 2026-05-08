package com.example.chat_system.mapper;


import com.example.chat_system.dto.ChatRoomDTO;
import com.example.chat_system.entity.ChatRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper extends BaseMapper<ChatRoom, ChatRoomDTO> {



}
