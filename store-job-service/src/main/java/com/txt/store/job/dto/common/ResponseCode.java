package com.txt.store.job.dto.common;

import org.apache.commons.lang3.StringUtils;

public enum ResponseCode {

    FAILED("500", "FAILED"),
    SUCCESSFUL("200", "SUCCESS"),
    API_FAILED("444", "Call API failed"),
    E001("001", "Request parameter invalid"),
    KEY_E001("E001", "File invalid"),
    KEY_E002("E002", "Data excel is over limit"),
    KEY_E003("E003", "Not found template header"),
    KEY_E004("E004", "Error header of excel file"),
    KEY_E005("E005", "Reading the excel file has been error"),
    KEY_E006("E006", "Error format name of excel file"),
    KEY_E007("E007", "Data not found"),
    KEY_E008("E008", "Upload file to azure store failed"),
    KEY_E009("E009", "Error get file info in azure store"),
    KEY_E010("E010", "Error get data from azure store");

    private final String code;
    private final String description;

    ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ResponseCode fromValue(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (ResponseCode e : ResponseCode.values()) {
            if (code.equalsIgnoreCase(e.code)) {
                return e;
            }
        }

        return null;
    }
}
