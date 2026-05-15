package com.example.chat_system.mapper;


import com.example.chat_system.dto.ChatRoomDTO;
import com.example.chat_system.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper extends BaseMapper<ChatRoom, ChatRoomDTO> {

    @Mapping(target = "membersCount", expression = "java(entity.getMembers() != null ? entity.getMembers().size() : 0)")
    @Override
    ChatRoomDTO toDTO(ChatRoom entity);

}
