package com.boiechko.eventswebapp.service;

import com.boiechko.eventswebapp.dto.RoleDTO;

public interface RoleService {

  RoleDTO getRoleByName(final String name);

}