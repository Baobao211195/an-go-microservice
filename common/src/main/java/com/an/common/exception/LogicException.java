package com.an.common.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LogicException extends RuntimeException {
    private String code;
    private String message;

    public LogicException() {
        super();
    }

    public LogicException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
