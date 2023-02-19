package com.txt.mongoredis.services.generic;


import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public interface GenericMapper<T, DTO> {

    T toEntity(DTO dto);

    DTO toDTO(T entity);

    @Mapping(target = "id", ignore = true)
    T toEntity(@MappingTarget T entity, DTO dto);
}
