package com.txt.mongoredis.services.generic;

import com.querydsl.core.types.Predicate;
import com.txt.mongoredis.dto.common.BaseDTO;
import com.txt.mongoredis.dto.common.PageableRequest;
import com.txt.mongoredis.dto.common.ResponseDTO;
import com.txt.mongoredis.entities.mongo.BaseEntity;
import com.txt.mongoredis.repositories.mongo.GenericRepository;
import com.txt.mongoredis.services.caching.CacheService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Data
public abstract class GenericServiceImpl<T extends BaseEntity, DTO extends BaseDTO, C extends BaseDTO, U extends BaseDTO, R extends PageableRequest, ID> implements GenericService<T, DTO, C, U, R, ID> {

    private GenericRepository<T, ID> genericRepository;
    private GenericMapper<T, DTO> mapper;

    @Autowired
    private CacheService cacheService;

    private Class<T> clazz;
    private Sort sort = Sort.by("initial");


    public GenericServiceImpl(GenericRepository<T, ID> genericRepository, GenericMapper<T, DTO> mapper) {
        this.genericRepository = genericRepository;
        this.mapper = mapper;
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public ResponseDTO findAll() {
        List<T> listT = genericRepository.findAll();
        List<DTO> data = StreamSupport.stream(listT.spliterator(), false).map(item -> toDto(item)).collect(Collectors.toList());
        ResponseDTO responseDTO = new ResponseDTO<>();
        responseDTO.setTotal(data.size());
        responseDTO.setSize(-1);
        responseDTO.setData(data);

        return responseDTO;
    }

    @Override
    public Optional<DTO> findById(ID id) {
        Optional<T> entity = genericRepository.findById(id);
        return entity.isPresent() ? entity.map(item -> toDto(item)) : Optional.empty();
    }

    @Override
    public DTO save(C dto) {
        T entity = getEntity((DTO) dto);
        genericRepository.save(entity);
//        cacheService.evictCacheByName(clazz.getSimpleName());
        return mapper.toDTO(entity);
    }

    @Override
    public DTO update(U dto) {
        Optional<DTO> optionalT = this.findById((ID) dto.getId());
        if (optionalT.isPresent()) {
            T targetEntity = mapper.toEntity(optionalT.get());
            T entity = mapper.toEntity(targetEntity, (DTO) dto);
            entity = genericRepository.save(entity);

//            cacheService.evictCacheByName(clazz.getSimpleName());

            return mapper.toDTO(entity);
        }
        return null;
    }

    @Override
    public Boolean deleteById(ID id) {
        Optional<DTO> optionalT = this.findById(id);
        if (optionalT.isPresent()) {
            genericRepository.deleteById(id);
//            cacheService.evictCacheByName(clazz.getSimpleName());
            return true;
        }
        return false;
    }

    @Override
    public ResponseDTO query(R pageableRequest) {
        sort = Sort.by("initial");
        pageableRequest.getOrders().forEach(
                (order) -> {
                    if (pageableRequest.getSort().equals("DESC")) {
                        sort = sort.and(Sort.by(order).descending());
                    } else {
                        sort = sort.and(Sort.by(order));
                    }
                }
        );

        Predicate predicate = createPredicate(pageableRequest);
        Page<T> pageDta = genericRepository.findAll(predicate, PageRequest.of(pageableRequest.getStart(), pageableRequest.getSize(), sort));
        List<DTO> data = StreamSupport.stream(pageDta.getContent().spliterator(), false).map(item -> toDto(item)).collect(Collectors.toList());

        ResponseDTO responseDTO = new ResponseDTO<>();

        responseDTO.setSize(pageableRequest.getSize());
        responseDTO.setStart(pageableRequest.getStart());
        if (!pageableRequest.getOrders().isEmpty()) {
            responseDTO.setOrders(pageableRequest.getOrders());
            responseDTO.setSort(pageableRequest.getSort().equals("DESC") ? "DESC" : "ASC");
        }
        responseDTO.setTotal(pageDta.getTotalElements());
        responseDTO.setData(data);

        return responseDTO;
    }

    public abstract Predicate createPredicate(PageableRequest pageableRequest);

    private DTO toDto(T item) {
        return (DTO) mapper.toDTO(item);
    }

    private T getEntity(DTO dto) {
        return (T) mapper.toEntity(dto);
    }
}
