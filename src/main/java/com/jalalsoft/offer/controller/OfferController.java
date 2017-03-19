package com.jalalsoft.offer.controller;

import com.jalalsoft.offer.Offer;
import com.jalalsoft.offer.exception.InvalidOfferIdException;
import com.jalalsoft.offer.exception.InvalidPriceException;
import com.jalalsoft.offer.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController("/offers")
public class OfferController {


    @Autowired
    OfferService offerService;


    @GetMapping("/offers/{id}")
    public ResponseEntity<Offer> get(@PathVariable("id") Long id) {

        Offer offerFound = offerService.get(id);

        if (offerFound == null) {
            return new ResponseEntity("No offer found for ID " + id, HttpStatus.NOT_FOUND);
        }

        offerFound.add(linkTo(methodOn(OfferController.class).get(id)).withSelfRel());

        return new ResponseEntity<>(offerFound, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity post(@RequestBody Offer offer) {

        try {
            Offer offerUpdated = offerService.create(offer);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(offerUpdated.getOfferId()).toUri();

            return ResponseEntity.created(location).build();
        } catch (InvalidPriceException e) {
            return new ResponseEntity("Price isn't set. It should be more than zero.", HttpStatus.BAD_REQUEST);

        }

    }

    @PutMapping("/offers/{id}")
    public ResponseEntity<Offer> put(@PathVariable Long id, @RequestBody Offer offer) throws InvalidOfferIdException {
        try {
            offer.setOfferId(id);
            Offer updatedOffer = offerService.update(offer);
            if (updatedOffer == null) {
                return new ResponseEntity("No offer found for ID:" + offer.getOfferId(), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (InvalidPriceException e) {
            return new ResponseEntity("Price must be set and should be more than zero.", HttpStatus.BAD_REQUEST);
        }


    }

    @DeleteMapping("/offers/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        boolean deleted = offerService.delete(id);

        if (!deleted) {
            return new ResponseEntity("No Offer found for ID: " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(id, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/offers")
    public ResponseEntity getOffers() {

        Iterable<Offer> offers = offerService.get();

        List<Offer> targetOffers = new ArrayList<>();
        offers.forEach(targetOffers::add);

        targetOffers.forEach(offer -> offer.add(linkTo(methodOn(OfferController.class).get(offer.getOfferId())).withSelfRel()));

        return new ResponseEntity(targetOffers, HttpStatus.OK);
    }
}

