package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Product;

/**
 * Defines the interface for Cart object persistence
 * 
 * @author SWEN Faculty
 */
public interface CartDAO {
    /**
     * Retrieves all {@linkplain Product products in {@linkplain Cart}}
     * 
     * @return An array of {@link Product product} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product[] getCartProducts() throws IOException;

    /**
     * Retrieves a {@linkplain Product product} with the given id from the cart
     * 
     * @param id The id of the {@link Product product} to get
     * 
     * @return a {@link Product product} object with the matching id
     * <br>
     * null if no {@link Product product} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product getProduct(int id) throws IOException;

    /**
     * Adds a product to cart and saves a {@linkplain Product product}
     * 
     * @param product {@linkplain Product product} object to be created and saved
     * <br>
     * The id of the product object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Product product} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product addProduct(Product product) throws IOException;

    /**
     * Increments the count of and saves a {@linkplain Product product}
     * 
     * @param int id of product to update
     * 
     * @return updated {@link Product product} if successful, null if
     * {@link Product product} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Product updateProductCount(int id, int count) throws IOException;

    /**
     * Removes a {@linkplain Product product} with the given id from the cart
     * 
     * @param id The id of the {@link Product product}
     * 
     * @return true if the {@link Product product} was removed
     * <br>
     * false if product with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean removeProduct(int id) throws IOException;
}