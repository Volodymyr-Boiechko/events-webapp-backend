package com.boiechko.eventswebapp.service;

import com.boiechko.eventswebapp.config.security.UserPrincipal;
import com.boiechko.eventswebapp.dto.AuthDTO;
import com.boiechko.eventswebapp.dto.JwtTokenDTO;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {

  Authentication authenticate(final AuthDTO authDTO, final HttpServletRequest request);

  void saveInSecurityContext(final Authentication authentication);

  JwtTokenDTO generateToken(final UserPrincipal userPrincipal);

}