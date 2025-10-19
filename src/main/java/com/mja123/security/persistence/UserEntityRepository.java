package com.mja123.security.persistence;

import com.mja123.security.domain.repository.UserRepository;
import com.mja123.security.exceptions.NotFoundException;
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

    @Override
    public UserEntity getById(long id) {
        return userCRUD.findById(id).orElse(null);
    }

    @Override
    public UserEntity add(UserEntity user) {
        return userCRUD.save(user);
    }

    @Override
    public UserEntity update(long id, UserEntity user) throws NotFoundException {
        UserEntity userEntity = userCRUD.findById(id).orElse(null);

        if (userEntity == null) {
            throw new NotFoundException("User with id " + id + " was not found.");
        }
        userEntity.set
        return userEntity;
    }
}
