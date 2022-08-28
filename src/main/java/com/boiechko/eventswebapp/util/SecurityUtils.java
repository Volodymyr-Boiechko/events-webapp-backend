package com.boiechko.eventswebapp.util;

import com.boiechko.eventswebapp.config.security.UserPrincipal;
import com.boiechko.eventswebapp.exception.UnauthorizedException;
import java.io.Serializable;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils implements Serializable {

  public static UserPrincipal getUserPrincipal() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(authentication)
        || Objects.isNull(authentication.getPrincipal())
        || Objects.equals(AnonymousAuthenticationToken.class, authentication.getClass())) {
      throw new UnauthorizedException("Unauthorized");
    }
    return (UserPrincipal) authentication.getPrincipal();
  }
}
