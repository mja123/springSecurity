package com.mja123.security.persistence.mapper;

import com.mja123.security.domain.dto.UpdateUserDTO;
import com.mja123.security.domain.dto.UserDTO;
import com.mja123.security.persistence.entity.UserEntity;
import org.mapstruct.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "email", source = "email")
    UserDTO entityToUser(UserEntity entity);
    @InheritInverseConfiguration
    UserEntity userToEntity(UserDTO userDTO);
    List<UserDTO> entitiesToUsers(Iterable<UserEntity> entity);

    default void updateUserToEntity(UpdateUserDTO updateUser, UserEntity user) {

    }

}
