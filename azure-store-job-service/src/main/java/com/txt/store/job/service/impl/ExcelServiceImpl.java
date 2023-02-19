package com.txt.store.job.service.impl;

import com.txt.store.job.constant.Constant;
import com.txt.store.job.dto.imports.DataImportDTO;
import com.txt.store.job.service.ExcelService;
import com.txt.store.job.utils.CommonUtil;
import com.txt.store.job.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    @Override
    public List<DataImportDTO> excelToDataImportDTO(InputStream isFile) {
        log.info("Process reading excel import");
        List<DataImportDTO> tutorials = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(isFile);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            int limitRow = Constant.LIMIT;
            int index = -1;

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                int rowNumber = currentRow.getRowNum();
                index++;

                // Pass header_manual, it is fixed in the order: receivedDate, expiredDate, amount, keyNo, content, idNo
                Iterator<Cell> cellsInRow = currentRow.iterator();
                if (rowNumber == 0) {
                    continue;
                }

                DataImportDTO tutorial = new DataImportDTO();
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    int cellIdx = currentCell.getColumnIndex();

                    Object cellValue = ExcelUtil.getCellValue(currentCell);

                    if (cellIdx == 0) {
                        tutorial.setReceivedDate(CommonUtil.formatDateRule(cellValue));
                    }
                    else if (cellIdx == 1) {
                        tutorial.setExpiredDate(CommonUtil.formatDateRule(cellValue));
                    }
                    else if (cellIdx == 2) {
                        if (ObjectUtils.isNotEmpty(cellValue) && CommonUtil.validateDecimal(cellValue)) {
                            tutorial.setAmount(new BigDecimal(String.valueOf(cellValue)));
                        }
                    }
                    else if (cellIdx == 3) {
                        tutorial.setKeyNo(CommonUtil.toEmpty(cellValue).trim());
                    }
                    else if (cellIdx == 4) {
                        tutorial.setContent(CommonUtil.toEmpty(cellValue).trim());
                    }
                    else if (cellIdx == 5) {
                        tutorial.setIdNo(CommonUtil.toEmpty(cellValue).trim());
                    }
                }

                tutorials.add(tutorial);

                if (index > limitRow) {
                    break;
                }
            }

            workbook.close();

        } catch (Exception e) {
            log.error("Fail to parse Excel file manual: " + e);
            throw new RuntimeException("Fail to parse Excel file manual: " + e);
        }

        return tutorials;
    }

    @Override
    public InputStream excelDeleteRow(InputStream isFile, List<Integer> listRow) {
        log.info("Process excelDeleteRow");

        InputStream inputStream = null;
        try {
            Workbook workbook = WorkbookFactory.create(isFile);
            Sheet sheet = workbook.getSheetAt(0);

            for (int rowIndex : listRow) {
                Row row = sheet.getRow(rowIndex);
                sheet.removeRow(row);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            byte[] barray = bos.toByteArray();
            inputStream = new ByteArrayInputStream(barray);
            workbook.close();
        } catch (Exception e) {
            log.error("Fail excelDeleteRow: " + e);
        }

        return inputStream;
    }
}
