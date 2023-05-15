package br.com.ceub.timesheet.exception;

import org.springframework.http.HttpStatus;

public enum AppError {
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, 401, "invalid.credential"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 401, "invalid.token");

    private HttpStatus status;
    private Integer code;
    private String message;

    AppError(HttpStatus status, Integer code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
