package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Product;

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
     * Creates and saves a {@linkplain Product product}
     * 
     * @param product {@linkplain Product product} object to be created and saved
     * <br>
     * The id of the product object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Product product} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product createProduct(Product product) throws IOException;

    /**
     * Updates and saves a {@linkplain Product product}
     * 
     * @param {@link Product product} object to be updated and saved
     * 
     * @return updated {@link Product product} if successful, null if
     * {@link Product product} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Product updateProduct(Product product) throws IOException;

    /**
     * Deletes a {@linkplain Product product} with the given id
     * 
     * @param id The id of the {@link Product product}
     * 
     * @return true if the {@link Product product} was deleted
     * <br>
     * false if product with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteProduct(int id) throws IOException;
}