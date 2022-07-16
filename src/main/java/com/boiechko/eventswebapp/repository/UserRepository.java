package com.boiechko.eventswebapp.repository;

import com.boiechko.eventswebapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  UserEntity findByPublicId(final String publicId);

  UserEntity findByUserName(final String userName);

  UserEntity findByUserNameOrEmail(final String userName, final String email);

}
