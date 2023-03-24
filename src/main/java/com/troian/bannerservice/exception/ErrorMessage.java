package com.troian.bannerservice.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorMessage {
    private int code;
    private String message;

    public ErrorMessage(Throwable exception, int code) {
        message = exception.getLocalizedMessage();
        this.code = code;
    }
}
