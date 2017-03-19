package com.jalalsoft.offer.service;

import com.jalalsoft.offer.exception.InvalidOfferIdException;
import com.jalalsoft.offer.exception.InvalidPriceException;
import com.jalalsoft.offer.Offer;

/**
 * Created by jalal.deen on 11/03/2017.
 */
public abstract class OfferService {

    public abstract Offer create(Offer offer) throws InvalidPriceException;

    public abstract boolean delete(long offerId);

    public abstract Offer get(long offerId);

    public abstract Offer update(Offer expectedOffer) throws InvalidPriceException, InvalidOfferIdException;

    public abstract Iterable<Offer> get() ;

}
