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
@Table(name = "address")
@Data
@NoArgsConstructor
public class AddressEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NonNull
  @Column(name = "id")
  private Long id;


  @Column(name = "country")
  private String country;

  @Column(name = "city")
  private String city;

  @Column(name = "street")
  private String street;

  @Column(name = "postal_code")
  private String postalCode;

}
