package com.mja123.security.domain.repository;

import com.mja123.security.persistence.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<UserEntity> getAll();
    Optional<UserEntity> getById(long id);
    UserEntity add(UserEntity user);
    Optional<UserEntity> update(long id, UserEntity user);
    Optional<UserEntity> delete(long id);
}
