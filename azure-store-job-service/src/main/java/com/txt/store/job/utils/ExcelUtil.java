package com.txt.store.job.utils;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.math.BigDecimal;

public class ExcelUtil {

    public static boolean isValidExcelFile(String fileName) {
        boolean result = false;
        String fileExt = getFileExtension(fileName);
        if (fileExt.equals("xls") || fileExt.equals("xlsx")) {
            result = true;
        }
        return result;
    }


    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public static Object getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        Object object = null;
        if (cell.getCellTypeEnum() == CellType.STRING) {
            object = cell.getStringCellValue();
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                object = cell.getDateCellValue();
            } else {
                object = new BigDecimal(cell.getNumericCellValue());
            }
        } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            object = cell.getBooleanCellValue();
        } else if (cell.getCellTypeEnum() == CellType.BLANK) {
            object = null;
        } else {
            object = null;
        }

        return object;
    }

}
