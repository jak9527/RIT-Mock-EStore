package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.GroceryItem;

/**
 * Defines the interface for GroceryItem object persistence
 * 
 * @author SWEN Faculty
 */
public interface GroceryDAO {
    /**
     * Retrieves all {@linkplain GroceryItem grocerys}
     * 
     * @return An array of {@link GroceryItem grocery} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    GroceryItem[] getGroceries() throws IOException;

    /**
     * Finds all {@linkplain GroceryItem grocerys} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link GroceryItem grocerys} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    GroceryItem[] findGroceries(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain GroceryItem grocery} with the given id
     * 
     * @param id The id of the {@link GroceryItem grocery} to get
     * 
     * @return a {@link GroceryItem grocery} object with the matching id
     * <br>
     * null if no {@link GroceryItem grocery} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    GroceryItem getGrocery(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain GroceryItem grocery}
     * 
     * @param grocery {@linkplain GroceryItem grocery} object to be created and saved
     * <br>
     * The id of the grocery object is ignored and a new uniqe id is assigned
     *
     * @return new {@link GroceryItem grocery} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    GroceryItem createGrocery(GroceryItem grocery) throws IOException;

    /**
     * Updates and saves a {@linkplain GroceryItem grocery}
     * 
     * @param {@link GroceryItem grocery} object to be updated and saved
     * 
     * @return updated {@link GroceryItem grocery} if successful, null if
     * {@link GroceryItem grocery} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    GroceryItem updateGrocery(GroceryItem grocery) throws IOException;

    /**
     * Deletes a {@linkplain GroceryItem grocery} with the given id
     * 
     * @param id The id of the {@link GroceryItem grocery}
     * 
     * @return true if the {@link GroceryItem grocery} was deleted
     * <br>
     * false if grocery with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteGrocery(int id) throws IOException;
}