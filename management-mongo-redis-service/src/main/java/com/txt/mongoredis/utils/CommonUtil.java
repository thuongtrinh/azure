package com.txt.mongoredis.utils;


import lombok.extern.slf4j.Slf4j;

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
            return "";
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

}
