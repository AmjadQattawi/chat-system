package com.example.chat_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_gen")
    @SequenceGenerator(name = "room_gen", sequenceName = "room_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
