package com.mja123.security.domain.repository;

import com.mja123.security.persistence.entity.UserEntity;

import java.util.List;

public interface UserRepository {
    List<UserEntity> getAll();
}
