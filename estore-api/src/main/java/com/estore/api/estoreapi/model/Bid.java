package com.estore.api.estoreapi.model;

public class Bid {
    private float bid;
    private User user;

    public Bid(float bid, User user){
        this.bid = bid;
        this.user = user;
    }

    public float getBidPrice(){
        return bid;
    }

    public String getUserName(){
        return user.getUsername();
    }
}
