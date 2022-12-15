package com.txt.store.job.utils;

import com.txt.store.job.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
public class CommonUtil {

    private static String DECIMAL_FORMAT = "^-?([0]{1}\\.{1}[0-9]+|[1-9]{1}[0-9]*\\.{1}[0-9]+|[0-9]+|0)$";
    private static String NUMBER_FORMAT = "^[0-9]*$";


    public static boolean isEmptyValue(String value) {
        return org.apache.commons.lang3.StringUtils.isBlank(value)
                || "null".equalsIgnoreCase(value);
    }

    public static String toEmpty(Object obj) {
        if (obj == null || "null".equals(obj)) {
            return Constant.BLANK;
        }
        return obj.toString();
    }

    public static boolean validateDecimal(Object number) {
        try {
            if (number != null && String.valueOf(number).matches(DECIMAL_FORMAT)) {
                return true;
            }
        } catch (Exception e) {
            log.error("Error validateDecimal: " + e.getMessage());
        }
        return false;
    }

    public static boolean validateNumber(String number) {
        if (number != null && number.matches(NUMBER_FORMAT)) {
            return true;
        }
        return false;
    }

    public static LocalDate formatDateRule(Object object) {
        LocalDate localDate = null;
        //Date Rule of file import: mm/dd/yyyy or yyyy/mm/dd
        if (object instanceof BigDecimal) {
            return null;
        } else if (object instanceof String) {
            localDate = DateUtil.parseLocalDate(String.valueOf(object), DateUtil.MM_DD_YYYY);
            if (ObjectUtils.isEmpty(localDate)) {
                localDate = DateUtil.parseLocalDate(String.valueOf(object), DateUtil.YYYY_MM_DD);
            }
        } else {
            localDate = DateUtil.parseLocalDate(object);
        }
        return localDate;
    }

}
