package com.txt.mongoredis.services.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.txt.mongoredis.dto.common.PageableRequest;
import com.txt.mongoredis.dto.common.ResponseDTO;
import com.txt.mongoredis.dto.country.CountryDTO;
import com.txt.mongoredis.dto.country.CountryCreateRequest;
import com.txt.mongoredis.dto.country.CountryQueryRequest;
import com.txt.mongoredis.dto.country.CountryUpdateRequest;
import com.txt.mongoredis.entities.mongo.Country;
import com.txt.mongoredis.entities.mongo.QCountry;
import com.txt.mongoredis.repositories.mongo.CountryRepository;
import com.txt.mongoredis.repositories.mongo.GenericRepository;
import com.txt.mongoredis.services.CountryService;
import com.txt.mongoredis.services.generic.GenericMapper;
import com.txt.mongoredis.services.generic.GenericServiceImpl;
import com.txt.mongoredis.services.mapper.CountryMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CountryServiceImpl extends GenericServiceImpl<
        Country,
        CountryDTO,
        CountryCreateRequest,
        CountryUpdateRequest,
        CountryQueryRequest,
        String> implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CountryServiceImpl(GenericRepository<Country, String> genericRepository, GenericMapper<Country, CountryDTO> mapper,
                              CountryRepository countryRepository, CountryMapper countryMapper) {
        super(genericRepository, mapper);
        this.countryMapper = countryMapper;
        this.countryRepository = countryRepository;
    }

    @Override
    public Predicate createPredicate(PageableRequest pageableRequest) {
        CountryQueryRequest request = (CountryQueryRequest) pageableRequest;
        QCountry query = QCountry.country;

        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtils.isNoneBlank(request.getId())) {
            predicate.and(query.id.eq(request.getId()));
        }
        if (StringUtils.isNoneBlank(request.getCode())) {
            predicate.and(query.code.eq(request.getCode()));
        }
        if (StringUtils.isNoneBlank(request.getName())) {
            predicate.and(query.name.eq(request.getName()));
        }

        return predicate;
    }

    @Override
    @Cacheable(value = "countries")
    public ResponseDTO findAll() {
        List<Country> queryResult = countryRepository.findAllByOrderByName();
        List<CountryDTO> countryDTOList =
                queryResult.stream()
                        .map(countryMapper::toDTO)
                        .collect(Collectors.toList());
        ResponseDTO<CountryDTO> response = new ResponseDTO<>();
        response.setData(countryDTOList);
        return response;
    }

    @Override
    @Cacheable(value = "countryFindId", key = "#id")
    public Optional<CountryDTO> findById(String s) {
        return super.findById(s);
    }

    @Override
    @Cacheable(value = "countryQuery")
    public ResponseDTO query(CountryQueryRequest pageableRequest) {
        return super.query(pageableRequest);
    }
}
