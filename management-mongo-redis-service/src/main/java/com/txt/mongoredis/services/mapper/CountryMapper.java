package com.txt.mongoredis.services.mapper;

import com.txt.mongoredis.dto.country.CountryDTO;
import com.txt.mongoredis.entities.mongo.Country;
import com.txt.mongoredis.services.generic.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CountryMapper extends GenericMapper<Country, CountryDTO> {
}
