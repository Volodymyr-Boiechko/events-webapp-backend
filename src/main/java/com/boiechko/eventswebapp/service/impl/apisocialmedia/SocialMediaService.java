package com.boiechko.eventswebapp.service.impl.apisocialmedia;

import com.boiechko.eventswebapp.dto.ApiAuthUrlDto;
import com.boiechko.eventswebapp.dto.AuthTokenDto;
import com.boiechko.eventswebapp.dto.JwtTokenDto;
import com.boiechko.eventswebapp.dto.UserDto;
import java.util.Map;

public interface SocialMediaService {

  ApiAuthUrlDto createAuthorization(final String redirectUrl);

  AuthTokenDto createAccessToken(final String code, final Map<String, String> stateMap);

  JwtTokenDto authenticateInSystem(final String code, final Map<String, String> stateMap);

  UserDto getUserInfo(final AuthTokenDto authTokenDto);
}
