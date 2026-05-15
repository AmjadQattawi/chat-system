package com.example.chat_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusDTO {
    private String username;
    private String status; // رح تكون إما "ONLINE" أو "OFFLINE"
}