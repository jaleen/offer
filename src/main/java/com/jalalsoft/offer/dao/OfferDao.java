package com.jalalsoft.offer.dao;

import com.jalalsoft.offer.Offer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OfferDao extends CrudRepository<Offer, Long>, PagingAndSortingRepository<Offer, Long> {}
