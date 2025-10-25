package com.mja123.security.persistence.crud;

import com.mja123.security.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserCRUD extends CrudRepository<UserEntity, Long> {
    UserEntity findUserByEmail(String email);
}
