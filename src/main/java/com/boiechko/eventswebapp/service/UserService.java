package com.boiechko.eventswebapp.service;

import com.boiechko.eventswebapp.dto.UserDto;
import java.util.List;

public interface UserService {

  UserDto getCurrentLoggedInUser();

  UserDto getUser(final String userName);

  UserDto saveUser(final UserDto userDto);

  List<UserDto> getAllUsers();
}
