package com.estore.api.estoreapi.persistence;

import java.time.LocalDateTime;

import java.io.IOException;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.AuctionItem;

/**
 * Defines the interface for Auction Item object persistence
 * 
 * @author Jacob Karvelis jak9527
 */
public interface AuctionItemDAO {

    /**
     * Retrieves the {@linkplain Product product} from the currently running auction
     * 
     * @return a {@link Product product} object from the current auction
     * <br>
     * null if no {@link AuctionItem auction} is currently running
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product getProduct() throws IOException;

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
     * @param product {@linkplain Product product} object to be auctioned
     * @param endTime the time the auction will close
     * <br>
     *
     * @return new {@link AuctionItem auction} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    AuctionItem createAuction(Product product, LocalDateTime endTime) throws IOException;

    /**
     * Updates and saves a {@linkplain AuctionItem auction}
     * 
     * @param aId the id of the auction to update
     * @param product {@linkplain Product product} object to replace the current one
     * @param endTime the new time the auction will close
     * 
     * @return updated {@link AuctionItem auction} if successful, null if
     * {@link Product product} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Product updateAuction(int aId, Product product, LocalDateTime endTime) throws IOException;

    /**
     * Deletes a {@linkplain AuctionItem auction} with the given id
     * 
     * @param id The id of the {@link AuctionItem auction}
     * 
     * @return true if the {@link AuctionItem auction} was deleted
     * <br>
     * false if auction with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteAuction(int id) throws IOException;

}