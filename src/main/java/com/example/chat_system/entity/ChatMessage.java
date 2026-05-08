package com.example.chat_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_gen")
    @SequenceGenerator(name = "chat_gen", sequenceName = "chat_seq", allocationSize = 1)
    private Long id;

    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom room;


}
