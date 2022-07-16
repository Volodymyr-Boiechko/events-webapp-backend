package com.boiechko.eventswebapp.config.security;

import com.boiechko.eventswebapp.dto.RoleDTO;
import com.boiechko.eventswebapp.dto.UserDTO;
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
  private RoleDTO role;
  private String userName;
  private String password;
  private boolean isEnabled;

  public UserPrincipal(final UserDTO userDTO) {
    this.id = userDTO.getId();
    this.publicId = userDTO.getPublicId();
    this.userRole = UserRole.findByName(userDTO.getRole().getRoleName());
    this.role = userDTO.getRole();
    this.userName = userDTO.getUserName();
    this.password = userDTO.getPassword();
    this.isEnabled = userDTO.getIsActive();
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
