# 💬 Real-Time Chat System

A production-ready, full-stack real-time chat application built with **Spring Boot 4** and **WebSocket (STOMP)**, featuring JWT-based authentication, role-based access control, live typing indicators, and online presence tracking.

---

## ✨ Features

- 🔐 **JWT Authentication** — Secure stateless auth on both HTTP and WebSocket connections
- 💬 **Real-Time Messaging** — Instant message delivery via WebSocket (STOMP over SockJS)
- ✍️ **Typing Indicators** — Live "User is typing..." notifications broadcast to room members
- 🟢 **Online Presence** — Real-time online/offline status tracking per session
- 🏠 **Room Membership System** — Users must join a room before sending or reading messages
- 🔒 **Authorization Enforcement** — Membership verified on every message, not just at connection time
- 🛡️ **Role-Based Access Control** — `ADMIN` and `USER` roles with method-level security (`@PreAuthorize`)
- 🗑️ **Admin Room Management** — Admins can delete rooms; room creators are auto-joined
- 📜 **Persistent Message History** — All messages stored in Oracle DB, loaded on room join
- 🌐 **Built-in Frontend** — Responsive dark-themed UI served directly from Spring Boot

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                        Frontend                          │
│           HTML + CSS + Vanilla JS + SockJS              │
└────────────────────────┬────────────────────────────────┘
                         │ HTTP (REST) + WebSocket (STOMP)
┌────────────────────────▼────────────────────────────────┐
│                     Spring Boot 4                        │
│                                                          │
│  ┌─────────────┐  ┌──────────────┐  ┌───────────────┐  │
│  │AuthController│  │ChatController│  │ChatRoomControl│  │
│  └──────┬──────┘  └──────┬───────┘  └───────┬───────┘  │
│         │                │                   │          │
│  ┌──────▼──────────────────────────────────▼─────────┐ │
│  │              Service Layer                          │ │
│  │  UserService · ChatRoomService · ChatMessageService │ │
│  │  JwtService · UserStatusService                     │ │
│  └──────────────────────┬──────────────────────────── ┘ │
│                         │                                │
│  ┌──────────────────────▼──────────────────────────────┐│
│  │           Spring Security + JWT Filter               ││
│  │    JwtAuthenticationFilter (HTTP)                    ││
│  │    WebSocketAuthChannelInterceptor (STOMP)           ││
│  └──────────────────────┬──────────────────────────────┘│
└────────────────────────┬────────────────────────────────┘
                         │ JPA / Hibernate
┌────────────────────────▼────────────────────────────────┐
│                    Oracle Database XE                    │
│           app_users · chat_rooms · messages              │
│               room_members (join table)                  │
└─────────────────────────────────────────────────────────┘
```

---

## 🔄 WebSocket Flow

```
Client                          Server
  │                               │
  │── STOMP CONNECT ─────────────▶│  WebSocketAuthChannelInterceptor
  │   Authorization: Bearer <jwt> │  validates JWT → sets Principal
  │◀─ CONNECTED ──────────────────│
  │                               │  WebSocketEventListener
  │                               │  fires userConnected() → broadcasts ONLINE
  │                               │
  │── SUBSCRIBE /topic/room/1 ───▶│
  │── SUBSCRIBE /topic/rooms/1/typing ▶│
  │── SUBSCRIBE /topic/users/status ──▶│
  │                               │
  │── SEND /app/room/1/send ─────▶│  validates membership
  │                               │  saves to DB
  │◀─ /topic/room/1 (message) ────│  broadcasts to all subscribers
  │                               │
  │── SEND /app/rooms/1/typing ──▶│  sets sender from Principal
  │◀─ /topic/rooms/1/typing ──────│  broadcasts typing event
  │                               │
  │── DISCONNECT ─────────────────▶│  WebSocketEventListener
  │                               │  fires userDisconnected() → broadcasts OFFLINE
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend Framework | Spring Boot 4, Spring MVC |
| Real-Time | Spring WebSocket, STOMP, SockJS |
| Security | Spring Security 6, JWT (jjwt 0.11.5) |
| Persistence | Spring Data JPA, Hibernate |
| Database | Oracle Database XE 21c |
| Object Mapping | MapStruct 1.5.5 |
| Code Generation | Lombok |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Frontend | HTML5, CSS3, Vanilla JS |
| Build Tool | Maven |
| Java Version | Java 21 |

---

## 📁 Project Structure

```
src/main/java/com/example/chat_system/
├── configration/
│   ├── SecurityConfig.java          # JWT filter chain, admin seeder
│   └── WebSocketConfig.java         # STOMP broker, channel interceptor
├── controller/
│   ├── AuthController.java          # POST /register, POST /login
│   ├── ChatController.java          # @MessageMapping send + typing
│   └── ChatRoomController.java      # REST CRUD for rooms
├── dto/                             # Data Transfer Objects
├── entity/                          # JPA Entities: User, ChatRoom, ChatMessage
├── enums/                           # Role: ADMIN, USER
├── exception/                       # GlobalExceptionHandler, custom exceptions
├── mapper/                          # MapStruct mappers (BaseMapper pattern)
├── repository/                      # Spring Data JPA repositories
├── service/                         # Business logic layer
│   ├── UserService.java
│   ├── ChatRoomService.java
│   ├── ChatMessageService.java
│   ├── JwtService.java
│   └── UserStatusService.java       # In-memory online tracking (ConcurrentHashMap)
└── validations/
    ├── WebSocketAuthChannelInterceptor.java  # JWT auth for STOMP CONNECT
    └── WebSocketEventListener.java           # connect/disconnect events
```

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- Oracle Database XE 21c (running on `localhost:1521`)

### 1. Clone the repository

```bash
git clone https://github.com/AmjadQattawi/chat-system.git
cd chat-system
```

### 2. Configure environment variables

The application reads sensitive config from environment variables — **never hardcoded**.

```bash
export DB_USERNAME=your_db_user
export DB_PASSWORD=your_db_password
export JWT_SECRET=your_256bit_secret_key
export DEFAULT_ADMIN_USER=admin
export DEFAULT_ADMIN_PASS=admin123
```

Or set them in your IDE's run configuration.

### 3. Database setup

Make sure Oracle XE is running and the user has CREATE TABLE privileges.
Hibernate will auto-create the schema on first run (`ddl-auto=update`).

### 4. Run

```bash
./mvnw spring-boot:run
```

Open your browser at **`http://localhost:8080`**

---

## 🔑 API Reference

### Auth

| Method | Endpoint | Body | Description |
|--------|----------|------|-------------|
| `POST` | `/register` | `{ userName, password }` | Register new user |
| `POST` | `/login` | `{ userName, password }` | Returns JWT token |

### Rooms `(🔒 JWT Required)`

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| `GET` | `/api/rooms` | USER | List all rooms with membership flag |
| `POST` | `/api/rooms?name=...` | USER | Create a new room |
| `POST` | `/api/rooms/{id}/join` | USER | Join a room |
| `GET` | `/api/rooms/{id}/messages` | MEMBER | Get room message history |
| `DELETE` | `/api/rooms/{id}` | ADMIN | Delete a room |
| `GET` | `/api/rooms/online-users` | USER | Get currently online users |

### WebSocket Topics

| Destination | Direction | Description |
|---|---|---|
| `/app/room/{id}/send` | Client → Server | Send a message |
| `/topic/room/{id}` | Server → Client | Receive messages |
| `/app/rooms/{id}/typing` | Client → Server | Send typing event |
| `/topic/rooms/{id}/typing` | Server → Client | Receive typing events |
| `/topic/users/status` | Server → Client | Online/Offline broadcasts |

---

## 🔐 Security Design

- **HTTP requests**: `JwtAuthenticationFilter` extracts and validates the Bearer token before the request reaches any controller.
- **WebSocket connections**: `WebSocketAuthChannelInterceptor` validates the JWT on the `STOMP CONNECT` frame and sets the `Principal` — so all subsequent `@MessageMapping` handlers receive an authenticated user automatically.
- **Message-level enforcement**: Even after connecting, `sendMessage()` verifies room membership before saving or broadcasting — the token alone is not enough.
- **Secrets via environment variables**: No credentials are stored in source code.

---

## 📖 API Documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you'd like to change.

---

## 👨‍💻 Author

**Amjad Qattawi**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=flat&logo=linkedin)](https://linkedin.com/in/your-profile)
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black?style=flat&logo=github)](https://github.com/AmjadQattawi)
