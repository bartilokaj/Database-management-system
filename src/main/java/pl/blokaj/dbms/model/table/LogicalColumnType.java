package pl.blokaj.dbms.model.table;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum describing logical column types
 */

public enum LogicalColumnType {
    INT64("INT64"),
    VARCHAR("VARCHAR");

    private final String value;

    LogicalColumnType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}

