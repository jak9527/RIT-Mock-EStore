package com.estore.api.estoreapi.model;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents an Bid
 * 
 * @author Jacob Karvelis jak9527
 */
public class Bid {
    @JsonProperty("bid") private float bid;
    @JsonProperty("user") private String user;

    /**
     * Creates a bid with the give bid price and User who made it
     * @param bid Price of bid
     * @param user User who made the bid
     */
    public Bid(@JsonProperty("bid") float bid, @JsonProperty("user") String user){
        this.bid = bid;
        this.user = user;
    }

    /**
     * Get the price of this bid
     * @return bid price
     */
    public float getBid(){
        return bid;
    }

    /**
     * get the name of the user associated with this bid
     * @return the username of the user associated with this bid
     */
    public String getUser(){
        return user;
    }

    @Override
    public String toString(){
        return this.user + " : " + this.bid;
    }
}
