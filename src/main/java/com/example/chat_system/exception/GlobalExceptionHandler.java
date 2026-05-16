package com.example.chat_system.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateBooking(DuplicateResourceException ex){
        return ResponseEntity.status(409).body(ex.getMessage());
    }
    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<String> handleRoomNotFound(RoomNotFoundException ex){
        return ResponseEntity.status(404).body(ex.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        if (ex.getMessage() != null && ex.getMessage().equals("Access denied!"))
            return ResponseEntity.status(403).body(ex.getMessage());
        return ResponseEntity.status(500).body("Internal server error");
    }

}
