package com.txt.store.job.service;

import com.txt.store.job.dto.imports.DataImportDTO;
import com.txt.store.job.dto.submit.DataSubmitDTO;
import com.txt.store.job.entities.mongo.DataImport;

import java.util.List;

public interface CommonService {

    DataSubmitDTO getDataSubmitDTO(String objectId);

    List<DataSubmitDTO> getListDataSubmitDTO();

    void saveDataImport(List<DataImportDTO> dtos);

}
