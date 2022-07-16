package com.boiechko.eventswebapp.service;

import com.boiechko.eventswebapp.dto.UserDTO;
import java.util.List;

public interface UserService {

  UserDTO getCurrentLoggedInUser();

  UserDTO getUser(final String userName);

  UserDTO saveUser(final UserDTO userDTO);

  List<UserDTO> getAllUsers();

}