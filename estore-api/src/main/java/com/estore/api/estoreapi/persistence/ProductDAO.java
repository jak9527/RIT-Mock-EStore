package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Product;

/**
 * Defines the interface for Hero object persistence
 * 
 * @author SWEN Faculty
 */
public interface ProductDAO {
    /**
     * Retrieves all {@linkplain Product heroes}
     * 
     * @return An array of {@link Product hero} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product[] getHeroes() throws IOException;

    /**
     * Finds all {@linkplain Product heroes} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Product heroes} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product[] findHeroes(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain Product hero} with the given id
     * 
     * @param id The id of the {@link Product hero} to get
     * 
     * @return a {@link Product hero} object with the matching id
     * <br>
     * null if no {@link Product hero} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product getHero(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Product item}
     * 
     * @param item {@linkplain Product item} object to be created and saved
     * <br>
     * The id of the hero object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Product item} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product createProduct(Product item) throws IOException;

    /**
     * Updates and saves a {@linkplain Product item}
     * 
     * @param {@link Product item} object to be updated and saved
     * 
     * @return updated {@link Product item} if successful, null if
     * {@link Product item} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Product updateHero(Product hero) throws IOException;

    /**
     * Deletes a {@linkplain Product hero} with the given id
     * 
     * @param id The id of the {@link Product hero}
     * 
     * @return true if the {@link Product hero} was deleted
     * <br>
     * false if hero with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteHero(int id) throws IOException;
}
