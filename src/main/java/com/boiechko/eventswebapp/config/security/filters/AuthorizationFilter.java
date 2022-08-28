package com.boiechko.eventswebapp.config.security.filters;

import com.boiechko.eventswebapp.config.security.UserPrincipal;
import com.boiechko.eventswebapp.dto.ErrorDto;
import com.boiechko.eventswebapp.service.JwtTokenService;
import com.boiechko.eventswebapp.util.JacksonUtils;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

  private static final String TOKEN_PREFIX = "Bearer ";

  private final JwtTokenService jwtTokenService;

  public AuthorizationFilter(final JwtTokenService jwtTokenService) {
    this.jwtTokenService = jwtTokenService;
  }

  @Override
  protected void doFilterInternal(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final FilterChain filterChain)
      throws ServletException, IOException {

    final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith(TOKEN_PREFIX)) {

      final String jwtToken = authorizationHeader.substring(TOKEN_PREFIX.length());
      try {
        final String userName = jwtTokenService.getUserNameFromToken(jwtToken);
        log.info("User {} made request by endpoint {}", userName, request.getRequestURI());
      } catch (JwtException e) {
        setResponseStatus(response, HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        return;
      }
    }

    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final UserPrincipal userPrincipal =
        Objects.nonNull(authentication) ? (UserPrincipal) authentication.getPrincipal() : null;

    if (Objects.nonNull(userPrincipal) && !userPrincipal.isEnabled()) {
      setResponseStatus(response, HttpStatus.FORBIDDEN, "User is disabled");
      return;
    }

    filterChain.doFilter(request, response);
  }

  private void setResponseStatus(
      final HttpServletResponse response, final HttpStatus httpStatus, final String responseMessage)
      throws IOException {
    response.setStatus(httpStatus.value());
    response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    JacksonUtils.OBJECT_MAPPER.writeValue(
        response.getOutputStream(), new ErrorDto(HttpStatus.UNAUTHORIZED.value(), responseMessage));
  }
}
