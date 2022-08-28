package com.boiechko.eventswebapp.service.impl;

import com.boiechko.eventswebapp.dto.RoleDto;
import com.boiechko.eventswebapp.mapper.RoleMapper;
import com.boiechko.eventswebapp.repository.RoleRepository;
import com.boiechko.eventswebapp.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  public RoleServiceImpl(final RoleRepository roleRepository,
      final RoleMapper roleMapper) {
    this.roleRepository = roleRepository;
    this.roleMapper = roleMapper;
  }


  @Override
  public RoleDto getRoleByName(final String name) {
    return roleMapper.toDto(roleRepository.getByRoleName(name));
  }
}