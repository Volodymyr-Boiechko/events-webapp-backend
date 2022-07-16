package com.boiechko.eventswebapp.entity;

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
@Table(name = "permission")
@Data
@NoArgsConstructor
public class PermissionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NonNull
  @Column(name = "id", unique = true)
  private Long id;

  @NonNull
  @Column(name = "permission_name", unique = true)
  private String permissionName;

}
