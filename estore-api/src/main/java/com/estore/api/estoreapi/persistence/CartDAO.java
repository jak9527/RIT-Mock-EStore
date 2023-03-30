package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.Cart;

/**
 * Defines the interface for Cart object persistence
 * 
 * @author Jacob Karvelis jak9527
 */
public interface CartDAO {
    /**
     * Retrieves all {@linkplain Product products in {@linkplain Cart}}
     * 
     * @param cId cart to get products from
     * 
     * @return An array of {@link Product product} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product[] getCartProducts(int cId) throws IOException;

    /**
     * Retrieves a {@linkplain Product product} with the given id from the cart
     * 
     * @param cId the id of the {@link Cart cart} to get
     * @param pId The id of the {@link Product product} to get
     * 
     * @return a {@link Product product} object with the matching id
     * <br>
     * null if no {@link Product product} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product getProduct(int cId, int pId) throws IOException;

    /**
     * Retrieves a {@linkplain Cart cart} with the given id
     * 
     * @param cId the id of the {@link Cart cart} to get
     * 
     * @return a {@link Cart cart} object with the matching id
     * <br>
     * null if no {@link Cart cart} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Cart getCart(int cId) throws IOException;

    /**
     * Adds a product to cart and saves a {@linkplain Product product}
     * 
     * @param product {@linkplain Product product} object to be created and saved
     * @param cId the id of the cart to add a product to
     *
     * @return new {@link Product product} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product addProduct(int cId, Product product) throws IOException;

    /**
     * Adds and saves a {@linkplain Cart cart}
     * 
     * @param cart
     *
     * @return new {@link Cart cart} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Cart addCart(Cart cart) throws IOException;
    
    /**
     * Increments the count of and saves a {@linkplain Product product}
     * 
     * @param cId id of product to update
     * @param pId id of product 
     * @param count amount to increment product by
     * 
     * @return updated {@link Product product} if successful, null if
     * {@link Product product} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Product updateProductCount(int cId, int pId, int count) throws IOException;

    /**
     * Removes a {@linkplain Product product} with the given id from the cart
     * 
     * @param cId The id of the {@link Cart cart} to remove from
     * @param pId The id of the {@link Product product} to remove
     * 
     * @return true if the {@link Product product} was removed
     * <br>
     * false if product with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean removeProduct(int cId, int pId) throws IOException;

    /**
     * Removes all {@linkplain Product products} in the {@linkplain Cart cart} with the given id
     * @param cId ID of cart to remove all products from 
     * @return true if all products were removed, false otherwise
     * @throws IOException
     */
    boolean removeAllProducts(int cId) throws IOException;

    /**
     * Updates a {@linkplain Cart cart} with regards to all given {@linkplain Product products}
     * 
     * @param cID The id of the Cart to update
     * @param products The array of products to update against
     * 
     * @return true if the cart was successfully updated
     * <br>
     * false if there were no products to update
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean updateCart(int cId, Product[] products) throws IOException;
}