package pl.blokaj.dbms.model.query;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum describing possible query statuses.
 */
public enum QueryStatus {

    CREATED("CREATED"),
    PLANNING("PLANNING"),
    RUNNING("RUNNING"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED");

    private final String value;

    QueryStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}

