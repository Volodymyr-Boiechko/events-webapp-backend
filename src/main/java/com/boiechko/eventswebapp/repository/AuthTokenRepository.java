package com.boiechko.eventswebapp.repository;

import com.boiechko.eventswebapp.entity.AuthTokenEntity;
import com.boiechko.eventswebapp.enums.DestinationType;
import java.util.List;
import java.util.Optional;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthTokenEntity, Long> {

  @Modifying
  @Query("delete from AuthTokenEntity a where a.id in :ids")
  void deleteAllByIds(@Param("ids") List<Long> ids);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  @Query(
      "select a from AuthTokenEntity a "
          + "where a.user.id=:userid and a.destinationType=:destinationType")
  Optional<AuthTokenEntity> findByOwnerAccountIdAndDestinationType(
      @Param("userid") Long userid, @Param("destinationType") DestinationType destinationType);
}
