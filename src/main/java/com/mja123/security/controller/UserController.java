package com.mja123.security.controller;

import com.mja123.security.domain.dto.UpdateUserDTO;
import com.mja123.security.domain.dto.UserDTO;
import com.mja123.security.domain.service.UserService;
import com.mja123.security.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOS = userService.getAllUsers();

        if (userDTOS.isEmpty())
            return ResponseEntity.ok(userDTOS);
        return ResponseEntity.accepted().body(userDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        try {
            return ResponseEntity.ok(userService.getUser(id));
        } catch (NotFoundException error) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody UpdateUserDTO userData) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, userData));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable long id) {
        try {
            return ResponseEntity.ok(userService.deleteUser(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
