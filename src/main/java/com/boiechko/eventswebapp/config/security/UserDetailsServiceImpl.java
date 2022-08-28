package com.boiechko.eventswebapp.config.security;

import com.boiechko.eventswebapp.entity.UserEntity;
import com.boiechko.eventswebapp.mapper.UserMapper;
import com.boiechko.eventswebapp.repository.UserRepository;
import java.util.Objects;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserDetailsServiceImpl(final UserRepository userRepository, final UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public UserDetails loadUserByUsername(final String usernameOrEmail)
      throws UsernameNotFoundException {
    final UserEntity userEntity =
        userRepository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail);
    if (Objects.isNull(userEntity)) {
      throw new UsernameNotFoundException(
          String.format("User with username or email %s not found", usernameOrEmail));
    }
    return new UserPrincipal(userMapper.toDto(userEntity));
  }
}
