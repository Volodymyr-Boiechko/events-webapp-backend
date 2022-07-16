package com.boiechko.eventswebapp.mapper;

import com.boiechko.eventswebapp.dto.RoleDTO;
import com.boiechko.eventswebapp.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, RoleEntity> {

}