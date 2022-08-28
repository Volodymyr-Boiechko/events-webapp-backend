package com.boiechko.eventswebapp.service;

import com.boiechko.eventswebapp.dto.RoleDto;

public interface RoleService {

  RoleDto getRoleByName(final String name);

}