package com.mja123.security.persistence.mapper;

import com.mja123.security.domain.dto.UserDTO;
import com.mja123.security.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "email", source = "email")
    UserDTO entityToUser(UserEntity entity);
    List<UserDTO> entitiesToUsers(Iterable<UserEntity> entity);
}
