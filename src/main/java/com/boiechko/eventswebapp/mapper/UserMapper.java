package com.boiechko.eventswebapp.mapper;

import com.boiechko.eventswebapp.dto.UserDto;
import com.boiechko.eventswebapp.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, UserEntity> {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "publicId", ignore = true)
  @Mapping(target = "userName", ignore = true)
  @Mapping(target = "password", ignore = true)
  void updateUserEntity(final UserDto dto, @MappingTarget final UserEntity entity);
}
