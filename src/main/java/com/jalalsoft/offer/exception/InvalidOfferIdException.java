package com.jalalsoft.offer.exception;

/**
 * Created by jalal.deen on 19/03/2017.
 */
public class InvalidOfferIdException extends Throwable {
    public InvalidOfferIdException(long id) {

        super("Invalid Offer id:"+id+". Offer Id must be greater than zero.");
    }
}
