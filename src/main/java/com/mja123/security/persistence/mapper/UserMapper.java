package com.mja123.security.persistence.mapper;

import com.mja123.security.domain.dto.IUserDTO;
import com.mja123.security.domain.dto.UserDTO;
import com.mja123.security.persistence.entity.UserEntity;
import com.mja123.security.utils.ParsingUtil;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "email", source = "email")
    UserDTO entityToUser(UserEntity entity);
    @InheritInverseConfiguration
    UserEntity userToEntity(IUserDTO userDTO);
    List<UserDTO> entitiesToUsers(Iterable<UserEntity> entity);
}
