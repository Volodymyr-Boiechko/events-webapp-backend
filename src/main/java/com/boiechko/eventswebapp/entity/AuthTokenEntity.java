package com.boiechko.eventswebapp.entity;

import com.boiechko.eventswebapp.enums.DestinationType;
import com.boiechko.eventswebapp.enums.TokenType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth_token")
@Data
@NoArgsConstructor
public class AuthTokenEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(name = "access_token")
  private String accessToken;

  @Column(name = "refresh_token")
  private String refreshToken;

  @Enumerated(EnumType.STRING)
  @Column(name = "token_type")
  private TokenType tokenType;

  @Column(name = "refresh_token_expires_in")
  private LocalDateTime refreshTokenExpiresIn;

  @Column(name = "issued")
  private LocalDateTime issued;

  @Column(name = "expires")
  private LocalDateTime expires;

  @Enumerated(EnumType.STRING)
  @Column(name = "destinationType")
  private DestinationType destinationType;
}
