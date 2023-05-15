package br.com.ceub.timesheet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EmailInvalidoException extends RuntimeException {
    public EmailInvalidoException(String s) {
        super(s);
    }
}
