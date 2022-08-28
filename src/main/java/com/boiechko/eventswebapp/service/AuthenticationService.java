package com.boiechko.eventswebapp.service;

import com.boiechko.eventswebapp.config.security.UserPrincipal;
import com.boiechko.eventswebapp.dto.AuthDto;
import com.boiechko.eventswebapp.dto.JwtTokenDto;
import com.boiechko.eventswebapp.dto.UserDto;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {

  Authentication authenticate(final AuthDto authDto, final HttpServletRequest request);

  Authentication authenticateWithoutPassword(
      final UserDto userDto, final HttpServletRequest request);

  void saveInSecurityContext(final Authentication authentication);

  JwtTokenDto generateToken(final UserPrincipal userPrincipal);
}
