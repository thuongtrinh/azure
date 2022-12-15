package com.txt.store.job.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResponseStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("code")
    private Integer code = null;

    @JsonProperty("message")
    private String message = null;

    @JsonProperty("errors")
    private List<Error> errors = null;

    public ResponseStatus code(Integer code) {
        this.code = code;
        return this;
    }

    @Schema(description = "")
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ResponseStatus message(String message) {
        this.message = message;
        return this;
    }

    @Schema(description = "")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseStatus errors(List<Error> errors) {
        this.errors = errors;
        return this;
    }

    public ResponseStatus addErrorsItem(Error errorsItem) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(errorsItem);
        return this;
    }

    @Schema(description = "")
    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResponseStatus responseStatus = (ResponseStatus) o;
        return Objects.equals(this.code, responseStatus.code) &&
                Objects.equals(this.message, responseStatus.message) &&
                Objects.equals(this.errors, responseStatus.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, errors);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ResponseStatus {\n");

        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
