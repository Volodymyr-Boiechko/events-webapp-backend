package com.boiechko.eventswebapp.service;

import com.boiechko.eventswebapp.dto.ApiAuthUrlDto;
import com.boiechko.eventswebapp.dto.AuthTokenDto;
import com.boiechko.eventswebapp.dto.JwtTokenDto;
import com.boiechko.eventswebapp.dto.UserDto;
import java.util.Map;

public interface GoogleService {

  ApiAuthUrlDto createGoogleAuthorization(final String redirectUrl);

  JwtTokenDto authenticateInSystem(final String code, final Map<String, String> stateMap);

  AuthTokenDto createGoogleAccessToken(final String code, final Map<String, String> stateMap);

  UserDto getUserInfo(final AuthTokenDto authTokenDto);
}
