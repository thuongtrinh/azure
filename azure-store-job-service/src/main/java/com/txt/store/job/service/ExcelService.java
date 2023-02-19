package com.txt.store.job.service;

import com.txt.store.job.dto.imports.DataImportDTO;

import java.io.InputStream;
import java.util.List;

public interface ExcelService {

    List<DataImportDTO> excelToDataImportDTO(InputStream inputStream);

    InputStream excelDeleteRow(InputStream isFile, List<Integer> listRow);
}
