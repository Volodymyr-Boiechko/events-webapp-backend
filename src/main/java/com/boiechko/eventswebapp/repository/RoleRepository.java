package com.boiechko.eventswebapp.repository;

import com.boiechko.eventswebapp.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

  RoleEntity getByRoleName(final String roleName);
}
