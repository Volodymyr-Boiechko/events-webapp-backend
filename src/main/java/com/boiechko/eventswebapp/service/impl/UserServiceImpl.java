package com.boiechko.eventswebapp.service.impl;

import com.boiechko.eventswebapp.config.security.UserPrincipal;
import com.boiechko.eventswebapp.dto.UserDto;
import com.boiechko.eventswebapp.entity.UserEntity;
import com.boiechko.eventswebapp.enums.UserRole;
import com.boiechko.eventswebapp.exception.NotFoundException;
import com.boiechko.eventswebapp.exception.SystemApiException;
import com.boiechko.eventswebapp.mapper.UserMapper;
import com.boiechko.eventswebapp.repository.UserRepository;
import com.boiechko.eventswebapp.service.RoleService;
import com.boiechko.eventswebapp.service.UserService;
import com.boiechko.eventswebapp.util.Assert;
import com.boiechko.eventswebapp.util.DateUtils;
import com.boiechko.eventswebapp.util.GeneralUtils;
import com.boiechko.eventswebapp.util.SecurityUtils;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final RoleService roleService;

  public UserServiceImpl(
      final UserRepository userRepository,
      final UserMapper userMapper,
      final PasswordEncoder passwordEncoder,
      final RoleService roleService) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
    this.roleService = roleService;
  }

  @Override
  public UserDto getCurrentLoggedInUser() {
    final UserPrincipal userPrincipal = SecurityUtils.getUserPrincipal();

    UserEntity userEntity = null;
    if (Objects.nonNull(userPrincipal)) {
      userEntity =
          userRepository.findByUserNameOrPublicId(
              userPrincipal.getUsername(), userPrincipal.getPublicId());
    }

    if (Objects.isNull(userEntity)) {
      throw new NotFoundException("Account doesn't exists");
    }
    return userMapper.toDto(userEntity);
  }

  @Override
  public UserDto getUser(final String userName) {
    log.info("Fetching user {}", userName);
    return userMapper.toDto(userRepository.findByUserName(userName));
  }

  @Override
  public UserDto saveUser(final UserDto userDto) {
    Assert.isTrue(
        Objects.nonNull(userRepository.findByUserName(userDto.getUserName())),
        () ->
            new SystemApiException(
                String.format("User with username %s already exists", userDto.getUserName()),
                HttpStatus.BAD_REQUEST));

    if (Objects.isNull(userDto.getRole())) {
      userDto.setRole(roleService.getRoleByName(UserRole.USER.name()));
    }
    userDto.setCreatedAt(DateUtils.getCurrentDateTime());
    log.info("Saving new user {} to the database", userDto.getUserName());
    final UserEntity userEntity = userMapper.toEntity(userDto);
    if (Objects.nonNull(userDto.getPassword())) {
      userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }
    userEntity.setPublicId(GeneralUtils.generatePublicId());
    userEntity.setIsActive(true);
    return userMapper.toDto(userRepository.save(userEntity));
  }

  @Override
  public List<UserDto> getAllUsers() {
    log.info("Fetching all users");
    return userMapper.toDto(userRepository.findAll());
  }
}
