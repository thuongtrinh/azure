package com.txt.store.job.constant;

public enum Status {

    SUCCESS("Success"),
    FAIL("Fail");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
