package com.example.chat_system.service;

import com.example.chat_system.dto.UserStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final SimpMessagingTemplate messagingTemplate;

    // تخزين اسم المستخدم كـ Key، والـ Session ID كـ Value
    private final ConcurrentHashMap<String, String> onlineUsers = new ConcurrentHashMap<>();

    // 1. عند دخول المستخدم
    public void userConnected(String username, String sessionId) {
        onlineUsers.put(username, sessionId);
        broadcastStatus(username, "ONLINE");
    }

    // 2. عند خروج المستخدم
    public void userDisconnected(String sessionId) {
        // بنلف على الـ Map عشان نلاقي اليوزر اللي عنده نفس الـ Session ID اللي فصلت
        onlineUsers.entrySet().stream()
                .filter(entry -> entry.getValue().equals(sessionId))
                .map(entry -> entry.getKey())
                .findFirst()
                .ifPresent(username -> {
                    onlineUsers.remove(username);
                    broadcastStatus(username, "OFFLINE");
                });
    }

    // 3. ميثود ترجع لنا قائمة بكل أسماء المتصلين حالياً
    public Set<String> getOnlineUsers() {
        return onlineUsers.keySet();
    }

    // ميثود مساعدة لبث الخبر عبر الـ WebSocket Topic
    private void broadcastStatus(String username, String status) {
        UserStatusDTO statusDTO = new UserStatusDTO(username, status);
        messagingTemplate.convertAndSend("/topic/users/status", statusDTO);
    }
}