package com.mja123.security.controller;

import com.mja123.security.domain.dto.UpdateUserDTO;
import com.mja123.security.domain.dto.UserDTO;
import com.mja123.security.domain.service.UserService;
import com.mja123.security.exceptions.NotFoundException;
import com.mja123.security.exceptions.NotUniqueAttributeException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for user management endpoints.
 * All endpoints require authentication via Auth0 JWT tokens.
 * Authorization is enforced at both method and HTTP security levels.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_read:users', 'ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOS = userService.getAllUsers();

        if (userDTOS.isEmpty())
            return ResponseEntity.ok(userDTOS);
        return ResponseEntity.accepted().body(userDTOS);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_read:users', 'ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        try {
            return ResponseEntity.ok(userService.getUser(id));
        } catch (NotFoundException error) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_write:users', 'ROLE_ADMIN')")
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserDTO userDTO) throws NotUniqueAttributeException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_write:users', 'ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody @Valid UpdateUserDTO userData)
            throws NotFoundException {
        return ResponseEntity.ok(userService.updateUser(id, userData));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_delete:users', 'ROLE_ADMIN')")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable long id) throws NotFoundException {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
