package com.boiechko.eventswebapp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

  private Long id;
  private String publicId;
  private String userName;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private LocalDate birthDate;
  private LocalDateTime createdAt;
  private Boolean isActive;
  private RoleDTO role;
  private AddressDTO address;

}
