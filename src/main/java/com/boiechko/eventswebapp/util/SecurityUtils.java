package com.boiechko.eventswebapp.util;

import com.boiechko.eventswebapp.config.security.UserPrincipal;
import com.boiechko.eventswebapp.enums.UserRole;
import com.boiechko.eventswebapp.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils implements Serializable {

  private static final String USER_ROLE_CLAIM = "userRole";
  private static final String USER_NAME_CLAIM = "userName";
  private static final String USER_PUBLIC_ID_CLAIM = "userPublicId";

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.validation-time-in-sec}")
  private Integer tokenValidationTimeInSeconds;

  public String getUserNameFromToken(final String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getExpirationDateFromToken(final String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public UserRole getUserRoleFromToken(final String token) {
    return UserRole.findByName(String.valueOf(getAllClaimsFromToken(token).get(USER_ROLE_CLAIM)));
  }

  private <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(final String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  public String generateToken(final UserPrincipal userPrincipal) {
    final Map<String, Object> claims = new HashMap<>();
    claims.put(USER_NAME_CLAIM, userPrincipal.getUsername());
    claims.put(USER_ROLE_CLAIM, userPrincipal.getUserRole().name());
    claims.put(USER_PUBLIC_ID_CLAIM, userPrincipal.getPublicId());
    return doGenerateToken(claims, userPrincipal.getUsername());
  }

  private String doGenerateToken(final Map<String, Object> claims, final String subject) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + tokenValidationTimeInSeconds * 1_000))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  public Boolean validateToken(final String token, final UserDetails userDetails) {
    final String userName = getUserNameFromToken(token);
    return StringUtils.equals(userName, userDetails.getUsername()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(final String token) {
    final LocalDateTime expirationDate = getExpirationDateFromToken(token).toInstant().atZone(
        ZoneId.systemDefault()).toLocalDateTime();
    return expirationDate.isBefore(LocalDateTime.now());
  }

  public static UserPrincipal getUserPrincipal() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(authentication) || Objects.isNull(authentication.getPrincipal())
        || Objects.equals(AnonymousAuthenticationToken.class, authentication.getClass())) {
      throw new UnauthorizedException("Unauthorized");
    }
    return (UserPrincipal) authentication.getPrincipal();
  }


}
