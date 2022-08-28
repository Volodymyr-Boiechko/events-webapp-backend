package com.boiechko.eventswebapp.mapper;

import com.boiechko.eventswebapp.dto.AuthTokenDto;
import com.boiechko.eventswebapp.entity.AuthTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class})
public interface AuthTokenMapper extends EntityMapper<AuthTokenDto, AuthTokenEntity> {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  void updateAuthTokenEntity(
      @MappingTarget final AuthTokenEntity authTokenEntity, final AuthTokenDto authTokenDto);
}
