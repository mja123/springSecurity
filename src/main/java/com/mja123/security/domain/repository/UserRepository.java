package com.mja123.security.domain.repository;

import com.mja123.security.exceptions.NotFoundException;
import com.mja123.security.persistence.entity.UserEntity;

import java.util.List;

public interface UserRepository {
    List<UserEntity> getAll();
    UserEntity getById(long id);
    UserEntity add(UserEntity user);
    UserEntity update(long id, UserEntity user) throws NotFoundException;
}
