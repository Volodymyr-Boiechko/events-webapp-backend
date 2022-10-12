package com.boiechko.eventswebapp.service.impl;

import com.boiechko.eventswebapp.dto.RoleDto;
import com.boiechko.eventswebapp.mapper.RoleMapper;
import com.boiechko.eventswebapp.repository.RoleRepository;
import com.boiechko.eventswebapp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  @Override
  public RoleDto getRoleByName(final String name) {
    return roleMapper.toDto(roleRepository.getByRoleName(name));
  }
}
