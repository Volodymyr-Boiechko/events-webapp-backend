package com.boiechko.eventswebapp.mapper;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface EntityMapper<D, E> {

  E toEntity(final D dto);

  List<E> toEntity(final List<D> dtoList);

  D toDto(final E entity);

  List<D> toDto(final List<E> entityList);
}
