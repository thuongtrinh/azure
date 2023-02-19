package com.txt.mongoredis.constant;

public enum FieldName {
    CREATE_DATE("createDate"),
    BANK_CODE("bankCode"),
    STATUS("status");

    FieldName(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
