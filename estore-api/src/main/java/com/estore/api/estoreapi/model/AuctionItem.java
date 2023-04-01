package com.estore.api.estoreapi.model;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.util.logging.Logger;

import org.apache.tomcat.jni.Local;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents an Auction Item
 * 
 * @author Jacob Karvelis jak9527
 */
public class AuctionItem {
    private static final Logger LOG = Logger.getLogger(Product.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Auction Item [id=%d, product=%s, end time=%s, bid=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("product") private Product product;
    @JsonProperty("endTime") private LocalDateTime endTime;
    @JsonProperty("maxBid") private Bid maxBid;

    /**
     * Create an auction item with the given id, product, and end time
     * @param id The id of the auction item
     * @param product The product for auction
     * @param endTime The time when this auction ends
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public AuctionItem(@JsonProperty("id") int id, @JsonProperty("product") Product product, @JsonProperty("endTime") LocalDateTime endTime, @JsonProperty("maxBid") Bid maxBid) {
        this.id = id;
        this.endTime = endTime;
        this.product = product;
        System.out.println(maxBid);
        this.maxBid = maxBid;
    }

    /**
     * Retrieves the id of the auction item
     * @return The id of the auction item
     */
    public int getId() {return id;}

    /**
     * Sets the product of the auction item - necessary for JSON object to Java object deserialization
     * @param Product The the product for this auction item
     */
    public void setProduct(Product product) {this.product = product;}

    /**
     * Retrieves the product from the listing
     * @return The product from the auction
     */
    public Product getProduct() {return this.product;}

    /**
     * Retrieves the end time of the auction
     * @return The end time of the auction
     */
    public LocalDateTime getEndTime() {return this.endTime;}

    /**
     * Sets the end time of the auction
     * @return void
     */
    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}

    /**
     * Gets the current max bid
     * @return The current max bid
     */
    public Bid getMaxBid(){
        return this.maxBid;
    }

    /**
     * Sets the current max bid to the given value
     * @return void
     */
    public void setMaxBid(Bid maxBid){
        this.maxBid = maxBid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return String.format(STRING_FORMAT,id,product,dtf.format(endTime),maxBid);
    }
}