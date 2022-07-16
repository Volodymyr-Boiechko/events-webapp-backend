package com.boiechko.eventswebapp.service;

import com.boiechko.eventswebapp.config.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {

  String getUserNameFromToken(final String token);

  String generateToken(final UserPrincipal userPrincipal);

  boolean isTokenValid(final String token, final UserDetails userDetails);

  boolean isTokenExpired(final String token);

}