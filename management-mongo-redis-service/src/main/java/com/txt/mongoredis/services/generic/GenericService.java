package com.txt.mongoredis.services.generic;

import com.txt.mongoredis.dto.common.BaseDTO;
import com.txt.mongoredis.dto.common.PageableRequest;
import com.txt.mongoredis.dto.common.ResponseDTO;
import com.txt.mongoredis.entities.mongo.BaseEntity;

import java.util.Optional;

public interface GenericService<T extends BaseEntity, DTO extends BaseDTO, S extends BaseDTO, U extends BaseDTO, R extends PageableRequest, ID> {

    ResponseDTO findAll();

    Optional<DTO> findById(ID id);

    DTO save(S dto);

    DTO update(U dto);

    Boolean deleteById(ID id);

    ResponseDTO query(R predicate);
}
