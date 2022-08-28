package com.boiechko.eventswebapp.config.security;

import com.boiechko.eventswebapp.dto.RoleDto;
import com.boiechko.eventswebapp.dto.UserDto;
import com.boiechko.eventswebapp.enums.UserRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class UserPrincipal implements UserDetails {

  private Long id;
  private String publicId;
  private UserRole userRole;
  private RoleDto role;
  private String userName;
  private String password;
  private boolean isEnabled;

  public UserPrincipal(final UserDto userDto) {
    this.id = userDto.getId();
    this.publicId = userDto.getPublicId();
    this.userRole = UserRole.findByName(userDto.getRole().getRoleName());
    this.role = userDto.getRole();
    this.userName = userDto.getUserName();
    this.password = userDto.getPassword();
    this.isEnabled = userDto.getIsActive();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    final List<GrantedAuthority> authorities = new ArrayList<>();
    if (Objects.nonNull(role)) {
      authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
    }
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }
}
