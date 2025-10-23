package com.mja123.security.persistence;

import com.mja123.security.domain.repository.UserRepository;
import com.mja123.security.exceptions.NotFoundException;
import com.mja123.security.persistence.crud.UserCRUD;
import com.mja123.security.persistence.entity.UserEntity;
import com.mja123.security.utils.ParsingUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<UserEntity> getById(long id)
    {
        return userCRUD.findById(id);
    }

    @Override
    public UserEntity add(UserEntity user) {
        return userCRUD.save(user);
    }

    @Override
    public Optional<UserEntity> update(long id, UserEntity updatedUser) {
        UserEntity userEntity = userCRUD.findById(id).orElse(null);

        if (userEntity != null) {
            ParsingUtil.setAttributesFromEntityToEntity(userEntity, updatedUser);
            userCRUD.save(userEntity);
        }
        return Optional.ofNullable(userEntity);
    }

    @Override
    public Optional<UserEntity> delete(long id) {
        UserEntity user = userCRUD.findById(id).orElse(null);
        if (user != null) {
            userCRUD.delete(user);
        }
        return Optional.ofNullable(user);
    }
}
