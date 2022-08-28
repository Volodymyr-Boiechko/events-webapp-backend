package com.boiechko.eventswebapp.config.security.filters;

import com.boiechko.eventswebapp.config.security.UserPrincipal;
import com.boiechko.eventswebapp.dto.AuthDto;
import com.boiechko.eventswebapp.service.AuthenticationService;
import com.boiechko.eventswebapp.util.JacksonUtils;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationService authenticationService;

  public AuthenticationFilter(final AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  @Autowired
  public void setAuthenticationManager(final AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
  }

  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(
      final HttpServletRequest request, final HttpServletResponse response)
      throws AuthenticationException {
    final String requestBodyInJsonString =
        request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    final AuthDto authDto = JacksonUtils.deserialize(requestBodyInJsonString, AuthDto.class);
    assert authDto != null;
    log.info("Username is: {}, password is: {}", authDto.getUsername(), authDto.getPassword());
    return authenticationService.authenticate(authDto, request);
  }

  @Override
  protected void successfulAuthentication(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final FilterChain chain,
      final Authentication authentication)
      throws IOException, ServletException {
    authenticationService.saveInSecurityContext(authentication);
    final UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    JacksonUtils.OBJECT_MAPPER.writeValue(
        response.getOutputStream(), authenticationService.generateToken(userPrincipal));
    getSuccessHandler().onAuthenticationSuccess(request, response, authentication);
  }
}
