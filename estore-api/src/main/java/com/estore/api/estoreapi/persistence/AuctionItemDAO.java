package com.estore.api.estoreapi.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;

import java.io.IOException;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.AuctionItem;
import com.estore.api.estoreapi.model.Bid;

/**
 * Defines the interface for Auction Item object persistence
 * 
 * @author Jacob Karvelis jak9527
 */
public interface AuctionItemDAO {

    /**
     * Retrieves the {@linkplain AuctionItem auction} currently running
     * 
     * @return a {@link AuctionItem auction} representing the currently running auction
     * <br>
     * null if no {@link AuctionItem auction} is currently running
     * 
     * @throws IOException if an issue with underlying storage
     */
    AuctionItem getAuction() throws IOException;

    /**
     * Creates and saves a {@linkplain AuctionItem auction}
     * 
     * @param aId the id for the auction to be created
     * @param product {@linkplain Product product} object to be auctioned
     * @param endTime the time the auction will close
     * @param maxBid the {@linkplain Bid bid} that is the max for this auction
     *
     * @return new {@link AuctionItem auction} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    AuctionItem createAuction(int aId, Product product, LocalDateTime endTime, Bid maxBid) throws IOException;

    // /**
    //  * Updates and saves the currently running {@linkplain AuctionItem auction}
    //  * 
    //  * @param product {@linkplain Product product} object to replace the current one
    //  * @param endTime the new time the auction will close
    //  * @param maxBid the new max bid
    //  * 
    //  * @return updated {@link AuctionItem auction} if successful, null if
    //  * {@link AuctionItem auction} is not currently running
    //  * 
    //  * @throws IOException if underlying storage cannot be accessed
    //  */
    // AuctionItem updateAuction(Product product, LocalDateTime endTime, Bid maxBid) throws IOException;

    /**
     * Deletes the currently running {@linkplain AuctionItem auction}
     * 
     * @return true if the {@link AuctionItem auction} was deleted
     * <br>
     * false if no auction is currently runnning
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteAuction() throws IOException;

    /**
     * Place a bid on the currently running auction
     * 
     * @param username The username of the user that placed this bid
     * @param bid The price they wanted to bid
     * @return true if the bid was placed
     * <br>
     * false if the bid was not placed due to being lower than the max bid
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean placeBid(String username, float bid) throws IOException;

    /**
     * Check if the currently running {@linkplain Aution auction}
     * is past its end time.
     * 
     * @return true if the auction is past its end time
     * <br>
     * false if it is not past the current auctions end or there 
     * is no current auction
     * @throws IOException
     */
    boolean auctionOver() throws IOException;

}