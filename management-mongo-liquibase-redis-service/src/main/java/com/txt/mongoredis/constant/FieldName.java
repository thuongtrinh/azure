package com.txt.mongoredis.constant;

public enum FieldName {
    CREATE_DATE("createDate"),
    POST_CODE("postCode"),
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
