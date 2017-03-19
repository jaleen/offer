package com.jalalsoft.offer;

import com.jalalsoft.offer.dao.OfferDao;
import com.jalalsoft.offer.exception.InvalidOfferIdException;
import com.jalalsoft.offer.exception.InvalidPriceException;
import com.jalalsoft.offer.service.OfferService;
import com.jalalsoft.offer.service.SimpleOfferService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OfferTest extends BaseOfferTest{

    private OfferService offerService = null;

    @Mock
    private OfferDao offerDao;

    @Before
    public void setUp() throws Exception {
        offerService = new SimpleOfferService(offerDao);
    }

    @Test
    public void whenNewOfferAdded_thenANewOfferCreated() throws InvalidPriceException {

        String description = "AAA batteries for quick sale.";
        String currency = Currency.getInstance(Locale.UK).getCurrencyCode();
        float price = 1.5f;
        String [] conditions = {"condition 1", "condition2"};
        Offer offer = new Offer(description, currency, price, conditions);

        when(offerDao.save(any(Offer.class))).thenReturn(offer);
        offer = offerService.create(offer);


        assertNotNull("Offer isn't created.", offer);
        assertEquals("Offer description isn't created correctly.", description, offer.getDescription());
        assertEquals("Offer currency isn't created correctly.", currency, offer.getCurrency());
        assertEquals("Offer price isn't created correctly.", price, offer.getPrice(), 0.01);

    }



    @Test (expected = InvalidPriceException.class)
    public void givenPriceIsNotSet_whenNewOfferAdded_thenANewOfferShouldNotBeCreated() throws InvalidPriceException {

        String description = "AAA batteries for quick sale.";
        String currency = Currency.getInstance(Locale.UK).getCurrencyCode();

        //Assuming no product can have 0 price......
        float price = 0.0f;
        String [] conditions = {"condition 1", "condition2"};
        Offer offer = new Offer(description, currency, price, conditions);

        when(offerDao.save(any(Offer.class))).thenReturn(offer);
        offerService.create(offer);

        //fail("An exception should have been thrown When price is zero.");

    }
    @Test
    public void whenOfferDoesNotExist_thenOfferShouldNotBeFound() {

        long mockOffer = 9999;

        when(offerDao.findOne(mockOffer)).thenReturn(null);
        Offer offer = offerService.get(mockOffer);

        assertNull("An offer exist in the system which shouldn't be there.", offer);

    }

    @Test
    public void whenOfferExist_thenOfferShouldBeFound() throws InvalidPriceException {

        long mockOfferId = 9999;

        String description = "AAA batteries for quick sale.";
        String currency = Currency.getInstance(Locale.UK).getCurrencyCode();
        float price = 1.5f;
        String[] conditions = null;
        Offer expectedOffer = new MockSimpleOffer(description, currency, price, mockOfferId, conditions);

        when(offerDao.save(any(Offer.class))).thenReturn(expectedOffer);
        when(offerDao.findOne(mockOfferId)).thenReturn(expectedOffer);

        Offer offerAdded = offerService.create(expectedOffer);

        assertNotNull("Offer isn't created.", offerAdded);

        Offer actualOffer = offerService.get(offerAdded.getOfferId());

        assertNotNull("Offer isn't found.", actualOffer);
        assertEquals("Offer description isn't created correctly.", description, actualOffer.getDescription());
        assertEquals("Offer currency isn't created correctly.", currency, actualOffer.getCurrency());
        assertEquals("Offer price isn't created correctly.", price, actualOffer.getPrice(), 0.01);

        assertNotNull(actualOffer.getOfferId());
        assertTrue("Offer id isn't valid", actualOffer.getOfferId() == mockOfferId);

    }

    @Test
    public void whenOfferisDeleted_thenOfferShouldBeRemoved() throws InvalidPriceException {
        long mockOfferId = 9998;

        String description = "AAA batteries for quick sale.";
        String currency = Currency.getInstance(Locale.UK).getCurrencyCode();
        float price = 1.5f;
        Offer expectedOffer = new MockSimpleOffer(description, currency, price, mockOfferId, null);

        when(offerDao.save(any(Offer.class))).thenReturn(expectedOffer);
        when(offerDao.findOne(mockOfferId)).thenReturn(null);

        Offer offerAdded = offerService.create(expectedOffer);

        assertNotNull("Offer isn't created.", offerAdded);

        offerService.delete(mockOfferId);
        Offer actualOffer = offerService.get(offerAdded.getOfferId());

        assertNull("Offer isn't deleted successfully.", actualOffer);

    }

    @Test
    public void whenOfferModified_thenOfferShouldBeUpdated() throws InvalidPriceException, InvalidOfferIdException {

        long mockOfferId = 9997;

        String description = "AAA batteries for quick sale.";
        String currency = Currency.getInstance(Locale.UK).getCurrencyCode();
        float price = 1.5f;
        Offer expectedOffer = new MockSimpleOffer(description, currency, price, mockOfferId,null);

        when(offerDao.save(any(Offer.class))).thenReturn(expectedOffer);
        when(offerDao.findOne(mockOfferId)).thenReturn(expectedOffer);

        Offer offerAdded = offerService.create(expectedOffer);

        assertNotNull("Offer isn't created.", offerAdded);

        float updatedPrice = 2.0f;

        expectedOffer = new MockSimpleOffer(description, currency, updatedPrice, mockOfferId, null);
        when(offerDao.save(any(Offer.class))).thenReturn(expectedOffer);
        Offer offerUpdated = offerService.update(expectedOffer);

        assertNotNull("Offer isn't found.", offerUpdated);
        assertEquals("Offer price isn't updated correctly.", updatedPrice, offerUpdated.getPrice(), 0.01);

    }

    @Test (expected = InvalidOfferIdException.class)
    public void giveOfferIDIsMissng_whenOfferModified_thenShouldBeRejected() throws InvalidPriceException, InvalidOfferIdException {

        long mockOfferId = 9997;

        String description = "AAA batteries for quick sale.";
        String currency = Currency.getInstance(Locale.UK).getCurrencyCode();
        float price = 1.5f;
        Offer expectedOffer = new MockSimpleOffer(description, currency, price, mockOfferId,null);

        when(offerDao.save(any(Offer.class))).thenReturn(expectedOffer);
        when(offerDao.findOne(mockOfferId)).thenReturn(expectedOffer);

        Offer offerAdded = offerService.create(expectedOffer);

        assertNotNull("Offer isn't created.", offerAdded);

        float updatedPrice = 2.0f;

        expectedOffer = new MockSimpleOffer(description, currency, updatedPrice, 0, null);
        when(offerDao.save(any(Offer.class))).thenReturn(expectedOffer);
        offerService.update(expectedOffer);

        //fail("Expected to throw an exception saying id is missing. Exception was not thrown.");

    }

    @Test
    public void whenOffersExist_thenOffersShouldBeFound() throws InvalidPriceException {

        long mockOfferId = 9999;

        String description = "AAA batteries for quick sale.";
        String currency = Currency.getInstance(Locale.UK).getCurrencyCode();
        float price = 1.5f;
        String[] conditions = null;
        Offer expectedOffer = new MockSimpleOffer(description, currency, price, mockOfferId, conditions);

        List<Offer> offers = new ArrayList<>();
        offers.add(expectedOffer);
        when(offerDao.save(any(Offer.class))).thenReturn(expectedOffer);
        when(offerDao.findAll()).thenReturn(offers);

        Offer offerAdded = offerService.create(expectedOffer);

        assertNotNull("Offer isn't created.", offerAdded);

        Iterable<Offer> offerList = offerService.get();

        assertNotNull("Offers aren't found.", offers);

        assertTrue("Offer not found in iterator.", offerList.iterator().hasNext());

        Offer offer1 = offerList.iterator().next();
        assertTrue("Expected offer not found.", offer1.getDescription().equals(offerAdded.getDescription()));
    }

}