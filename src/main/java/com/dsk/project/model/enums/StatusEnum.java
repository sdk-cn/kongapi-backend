package com.dsk.project.model.enums;


public enum StatusEnum {
    OPEN(1,"上线"),
    CLOSED(0,"关闭");

    private final Integer status;

    private final String description;

    StatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
