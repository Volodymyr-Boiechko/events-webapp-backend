package com.boiechko.eventswebapp.controller;

import com.boiechko.eventswebapp.dto.UserDto;
import com.boiechko.eventswebapp.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  public UserController(final UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser() {
    return ResponseEntity.ok(userService.getCurrentLoggedInUser());
  }

  @GetMapping("/all")
  public ResponseEntity<List<UserDto>> getAllUsers() {
    return ResponseEntity.ok().body(userService.getAllUsers());
  }

  @PostMapping
  public ResponseEntity<UserDto> saveUser(@RequestBody final UserDto userDto) {
    return ResponseEntity.ok().body(userService.saveUser(userDto));
  }


}