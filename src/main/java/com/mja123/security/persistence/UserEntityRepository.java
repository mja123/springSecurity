package com.mja123.security.persistence;

import com.mja123.security.domain.repository.UserRepository;
import com.mja123.security.persistence.crud.UserCRUD;
import com.mja123.security.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserEntityRepository implements UserRepository {
    private final UserCRUD userCRUD;

    public UserEntityRepository(UserCRUD userCRUD) {
        this.userCRUD = userCRUD;
    }

    @Override
    public List<UserEntity> getAll() {
        return (List<UserEntity>) userCRUD.findAll();
    }
}
