package com.boiechko.eventswebapp.service.impl;

import com.boiechko.eventswebapp.config.security.UserPrincipal;
import com.boiechko.eventswebapp.dto.AuthDTO;
import com.boiechko.eventswebapp.dto.JwtTokenDTO;
import com.boiechko.eventswebapp.service.AuthenticationService;
import com.boiechko.eventswebapp.service.JwtTokenService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;

  public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
      JwtTokenService jwtTokenService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenService = jwtTokenService;
  }

  @Override
  public Authentication authenticate(final AuthDTO authDTO, final HttpServletRequest request) {
    final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        authDTO.getUsername(), authDTO.getPassword());
    authenticationToken.setDetails(new WebAuthenticationDetails(request));
    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  public void saveInSecurityContext(Authentication authentication) {
    final SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }

  @Override
  public JwtTokenDTO generateToken(UserPrincipal userPrincipal) {
    return new JwtTokenDTO(jwtTokenService.generateToken(userPrincipal));
  }
}