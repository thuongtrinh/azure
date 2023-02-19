package com.txt.mongoredis.repositories.mongo;

import com.txt.mongoredis.entities.mongo.Country;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends GenericRepository<Country, String> {

    List<Country> findAllByOrderByName();

}
