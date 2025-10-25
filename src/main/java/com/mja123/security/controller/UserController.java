package com.mja123.security.controller;

import com.mja123.security.domain.dto.UpdateUserDTO;
import com.mja123.security.domain.dto.UserDTO;
import com.mja123.security.domain.service.UserService;
import com.mja123.security.exceptions.NotFoundException;
import com.mja123.security.exceptions.NotUniqueAttributeException;
import jakarta.validation.Valid;
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
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserDTO userDTO) throws NotUniqueAttributeException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody @Valid UpdateUserDTO userData)
            throws NotFoundException {
        return ResponseEntity.ok(userService.updateUser(id, userData));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable long id) throws NotFoundException {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
