package com.mja123.security.controller;

import com.mja123.security.domain.dto.UserDTO;
import com.mja123.security.domain.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOS = userService.getAllUsers();
        if (!userDTOS.isEmpty())
            return ResponseEntity.ok(userDTOS);
        return ResponseEntity.accepted().body(userDTOS);
    }
}
