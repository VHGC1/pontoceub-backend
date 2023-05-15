package br.com.ceub.timesheet.exception.handlers;

import br.com.ceub.timesheet.exception.AppError;
import br.com.ceub.timesheet.exception.AppException;
import br.com.ceub.timesheet.exception.dto.ErrorResponseBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

public class ApiExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(AppException.class)
    public ResponseEntity handleAppException(AppException appException) {
        AppError error = appException.getError();
        return ResponseEntity.status(error.getStatus())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new ErrorResponseBase(error.getCode(), getMessage(error.getMessage())));
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.ENGLISH);
    }
}
