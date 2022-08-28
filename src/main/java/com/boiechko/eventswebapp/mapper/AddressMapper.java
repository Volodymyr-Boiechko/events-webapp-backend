package com.boiechko.eventswebapp.mapper;

import com.boiechko.eventswebapp.dto.AddressDto;
import com.boiechko.eventswebapp.entity.AddressEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<AddressDto, AddressEntity> {}
