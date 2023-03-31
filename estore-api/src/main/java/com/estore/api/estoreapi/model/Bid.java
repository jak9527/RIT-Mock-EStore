package com.estore.api.estoreapi.model;

/**
 * Represents an Bid
 * 
 * @author Jacob Karvelis jak9527
 */
public class Bid {
    private float bid;
    private String user;

    /**
     * Creates a bid with the give bid price and User who made it
     * @param bid Price of bid
     * @param user User who made the bid
     */
    public Bid(float bid, String user){
        this.bid = bid;
        this.user = user;
    }

    /**
     * Get the price of this bid
     * @return bid price
     */
    public float getBidPrice(){
        return bid;
    }

    /**
     * get the name of the user associated with this bid
     * @return the username of the user associated with this bid
     */
    public String getUserName(){
        return user;
    }
}
