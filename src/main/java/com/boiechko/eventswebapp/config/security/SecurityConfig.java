package com.boiechko.eventswebapp.config.security;

import com.boiechko.eventswebapp.config.security.filters.AuthenticationFilter;
import com.boiechko.eventswebapp.config.security.filters.AuthorizationFilter;
import com.boiechko.eventswebapp.enums.UserRole;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final AuthenticationFilter authenticationFilter;
  private final AuthorizationFilter authorizationFilter;

  public SecurityConfig(final UserDetailsService userDetailsService,
      @Lazy final AuthenticationFilter authenticationFilter,
      final AuthorizationFilter authorizationFilter) {
    this.userDetailsService = userDetailsService;
    this.authenticationFilter = authenticationFilter;
    this.authorizationFilter = authorizationFilter;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(final HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/api/login", "/api/google/**").permitAll()
        .antMatchers("/api/user/**").hasAuthority(UserRole.USER.name())
        .anyRequest()
        .authenticated()
        .and()
        .addFilter(getAuthenticationFilter())
        .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        .maximumSessions(1)
        .sessionRegistry(sessionRegistry());

  }

  private UsernamePasswordAuthenticationFilter getAuthenticationFilter() {
    authenticationFilter.setFilterProcessesUrl("/api/login");
    return authenticationFilter;
  }

}