package com.boiechko.eventswebapp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true)
  private Long id;

  @Column(name = "public_id", unique = true)
  private String publicId;

  @NonNull
  @Column(name = "user_name", unique = true)
  private String userName;

  @Column(name = "password")
  private String password;

  @NonNull
  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "is_active")
  private Boolean isActive;

}
