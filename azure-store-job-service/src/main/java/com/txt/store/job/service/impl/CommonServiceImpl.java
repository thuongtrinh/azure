package com.txt.store.job.service.impl;

import com.txt.store.job.dto.imports.DataImportDTO;
import com.txt.store.job.dto.submit.DataSubmitDTO;
import com.txt.store.job.entities.mongo.DataImport;
import com.txt.store.job.repository.mongo.DataImportRepository;
import com.txt.store.job.service.CommonService;
import com.txt.store.job.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    private DataImportRepository dataImportRepository;

    @Override
    public DataSubmitDTO getDataSubmitDTO(String objectId) {
        DataSubmitDTO dataSubmitDTO = null;

        if (StringUtils.isBlank(objectId)) {
            return dataSubmitDTO;
        }

        DataImport dataImport = dataImportRepository.findBy_id(new ObjectId(objectId));
        if (ObjectUtils.isNotEmpty(dataImport)) {
            dataSubmitDTO = new DataSubmitDTO();
            BeanUtils.copyProperties(dataImport, dataSubmitDTO);
            dataSubmitDTO.setObjectId(dataImport.get_id().toString());
        }

        return dataSubmitDTO;
    }

    @Override
    public List<DataSubmitDTO> getListDataSubmitDTO() {
        List<DataSubmitDTO> dtoList = new ArrayList<>();
        List<DataImport> dataImports = dataImportRepository.findAll();
        if (ObjectUtils.isEmpty(dataImports)) {
            return dtoList;
        }

        for (DataImport data : dataImports) {
            DataSubmitDTO manualDTO = new DataSubmitDTO();
            BeanUtils.copyProperties(data, manualDTO);
            manualDTO.setObjectId(data.get_id().toString());
            dtoList.add(manualDTO);
        }

        return dtoList;
    }

    @Override
    public void saveDataImport(List<DataImportDTO> dtos) {
        for (DataImportDTO importDTO : dtos) {
            DataImport dataImport = new DataImport();
            BeanUtils.copyProperties(importDTO, dataImport);
            dataImport.setCreateDate(LocalDateTime.now());
            dataImport.setCreateBy("USER");
        }
    }
}
