package com.txt.mongoredis.controller;

import com.txt.mongoredis.dto.country.CountryCreateRequest;
import com.txt.mongoredis.dto.country.CountryDTO;
import com.txt.mongoredis.dto.country.CountryQueryRequest;
import com.txt.mongoredis.dto.country.CountryUpdateRequest;
import com.txt.mongoredis.entities.mongo.Country;
import com.txt.mongoredis.services.generic.GenericService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/country")
@Tag(name = "Country", description = "Country")
public class CountryController extends CurdController<
        Country,
        CountryDTO,
        CountryCreateRequest,
        CountryUpdateRequest,
        CountryQueryRequest,
        String> {

    public CountryController(GenericService<Country, CountryDTO, CountryCreateRequest, CountryUpdateRequest, CountryQueryRequest, String> genericService) {
        super(genericService);
    }

}