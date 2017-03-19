package com.jalalsoft.offer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by jalal.deen on 17/03/2017.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPriceException extends Throwable {
    public InvalidPriceException(String s) {
        super(s);
    }
}
