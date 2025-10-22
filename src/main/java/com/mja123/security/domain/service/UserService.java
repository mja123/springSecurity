package com.mja123.security.domain.service;

import com.mja123.security.domain.dto.UpdateUserDTO;
import com.mja123.security.domain.dto.UserDTO;
import com.mja123.security.domain.repository.UserRepository;
import com.mja123.security.exceptions.NotFoundException;
import com.mja123.security.persistence.entity.UserEntity;
import com.mja123.security.persistence.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> getAllUsers() {
        return userMapper.entitiesToUsers(userRepository.getAll());
    }

    public UserDTO getUser(long id) throws NotFoundException {

        UserEntity userEntity = userRepository.getById(id);
        if (userEntity == null) throw new NotFoundException("User not found");
        return userMapper.entityToUser(userEntity);
    }

    public UserDTO addUser(UserDTO userDTO) {
        return userMapper.entityToUser(userRepository.add(userMapper.userToEntity(userDTO)));
    }

    public UserDTO updateUser(long id, UpdateUserDTO userDTO) {
        try {
            return userMapper.entityToUser(userRepository.update(id, userMapper.userToEntity(userDTO)));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
