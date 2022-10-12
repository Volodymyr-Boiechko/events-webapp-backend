package com.boiechko.eventswebapp.service.impl;

import com.boiechko.eventswebapp.config.security.UserPrincipal;
import com.boiechko.eventswebapp.dto.AuthDto;
import com.boiechko.eventswebapp.dto.JwtTokenDto;
import com.boiechko.eventswebapp.dto.UserDto;
import com.boiechko.eventswebapp.service.AuthenticationService;
import com.boiechko.eventswebapp.service.JwtTokenService;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;

  @Override
  public Authentication authenticate(final AuthDto authDto, final HttpServletRequest request) {
    final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());
    authenticationToken.setDetails(new WebAuthenticationDetails(request));
    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  public Authentication authenticateWithoutPassword(
      final UserDto userDto, final HttpServletRequest request) {
    final UserPrincipal userPrincipal = new UserPrincipal(userDto);
    final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            userPrincipal,
            null,
            Collections.singleton(new SimpleGrantedAuthority(userDto.getRole().getRoleName())));
    authenticationToken.setDetails(new WebAuthenticationDetails(request));
    return authenticationToken;
  }

  @Override
  public void saveInSecurityContext(final Authentication authentication) {
    final SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }

  @Override
  public JwtTokenDto generateToken(final UserPrincipal userPrincipal) {
    return new JwtTokenDto(jwtTokenService.generateToken(userPrincipal));
  }
}
