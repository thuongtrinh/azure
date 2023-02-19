package com.txt.mongoredis.controller;

import com.txt.mongoredis.dto.common.BaseDTO;
import com.txt.mongoredis.dto.common.PageableRequest;
import com.txt.mongoredis.dto.common.ResponseDTO;
import com.txt.mongoredis.entities.mongo.BaseEntity;
import com.txt.mongoredis.services.generic.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public abstract class CurdController<T extends BaseEntity, DTO extends BaseDTO, C extends BaseDTO, U extends BaseDTO, R extends PageableRequest, ID> {

    protected GenericService<T, DTO, C, U, R, ID> genericService;

    public CurdController(GenericService<T, DTO, C, U, R, ID> genericService) {
        this.genericService = genericService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<ResponseDTO<DTO>> findAll() {
        ResponseDTO<DTO> responseDTO = genericService.findAll();
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<DTO> create(@RequestBody @Valid C payload) {
        DTO data = genericService.save(payload);

        return ResponseEntity.ok(data);
    }

    @PutMapping(value = "/id", consumes = "application/json")
    public ResponseEntity<DTO> update(@RequestBody U payload) {
        if (genericService.update(payload) == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        DTO data = genericService.update(payload);

        return ResponseEntity.ok(data);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") ID id) {
        if (genericService.deleteById(id)) {
            return ResponseEntity.ok("OK");
        }
        return new ResponseEntity<>("ID not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/query", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseDTO<DTO>> query(@RequestBody R queryRequest) {
        if (queryRequest.getStart() < 0 || queryRequest.getSize() <= 0) {
            return new ResponseEntity<>(new ResponseDTO<>(), HttpStatus.BAD_REQUEST);
        }
        ResponseDTO<DTO> responseDTO = genericService.query(queryRequest);

        return ResponseEntity.ok(responseDTO);
    }
}
