package com.jalalsoft.offer.service;

import com.jalalsoft.offer.exception.InvalidOfferIdException;
import com.jalalsoft.offer.exception.InvalidPriceException;
import com.jalalsoft.offer.Offer;
import com.jalalsoft.offer.dao.OfferDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jalal.deen on 11/03/2017.
 */
@Service
public class SimpleOfferService extends OfferService {

    @Autowired
    private final OfferDao offerDao;

    public SimpleOfferService(OfferDao offerDao) {

        this.offerDao = offerDao;
    }

    @Override
    public Offer create(Offer offer) throws InvalidPriceException {

        if (offer.getPrice() <= 0) {
            throw new InvalidPriceException("Price:" + offer.getPrice() + " is less than or equal to zero");
        }
        return offerDao.save(offer);
    }

    /**
     * @param offerId
     * @return true of deleted successfully. False if record not found.
     */
    @Override
    public boolean delete(long offerId) {

        offerDao.delete(offerId);

        return true;
    }

    @Override
    public Offer get(long offerId) {

        return this.offerDao.findOne(offerId);
    }

    @Override
    public Offer update(Offer offer) throws InvalidPriceException, InvalidOfferIdException {

        if(offer.getOfferId()<=0){
            throw new InvalidOfferIdException(offer.getOfferId());
        }
        if (offer.getPrice() <= 0) {
            throw new InvalidPriceException("Price:" + offer.getPrice() + " is less than or equal to zero");
        }
        Offer existingOffer = get(offer.getOfferId());
        if (existingOffer == null) {
            return null;
        }

        return offerDao.save(offer);
    }

    @Override
    public Iterable<Offer> get() {

        return offerDao.findAll();
    }
}
