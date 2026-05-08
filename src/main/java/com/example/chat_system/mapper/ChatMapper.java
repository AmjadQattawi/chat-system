package com.example.chat_system.mapper;

import com.example.chat_system.dto.ChatMessageDTO;
import com.example.chat_system.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper extends BaseMapper<ChatMessage, ChatMessageDTO> {

    @Mapping(source = "room.id", target = "roomId")
    @Override
    ChatMessageDTO toDTO(ChatMessage entity);

    @Mapping(source = "roomId", target = "room.id")
    @Override
    ChatMessage toEntity(ChatMessageDTO dto);

}
