package com.example.chat_system.mapper;

import com.example.chat_system.dto.UserDTO;
import com.example.chat_system.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDTO> {


}
