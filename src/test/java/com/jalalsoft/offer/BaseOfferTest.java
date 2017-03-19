package com.jalalsoft.offer;

/**
 * Created by jalal.deen on 14/03/2017.
 */
public abstract class BaseOfferTest {

    protected class MockSimpleOffer extends Offer {

        private long id;

        public MockSimpleOffer(String description, String currency, float price, long id, String [] conditions) {
            super(description, currency, price, conditions);
            this.id = id;
        }

        @Override
        public Long getOfferId(){
            return id;
        }

    }
}
