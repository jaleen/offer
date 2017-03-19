package com.jalalsoft.offer;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Offer extends ResourceSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long offerId;
    @NotNull
    private String description = "";
    private String currency = "";

    @NotNull
    private float price = 0f;

    //@ElementCollection
    @OrderColumn
    private String[] conditions;

    private static final long serialVersionUID = 1L;

    public Offer(String description, String currency, float price, String[] conditions) {
        this.description = description;
        this.currency = currency;
        this.price = price;
        this.conditions = conditions;
    }

    public Offer() {
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public Long getOfferId() {
        return offerId;
    }
    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
}