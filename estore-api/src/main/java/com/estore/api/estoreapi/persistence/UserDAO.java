package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.User;

/**
 * Defines the interface for User object persistence
 * 
 * @author Jacob Karvelis jak9527
 */
public interface UserDAO {

    /**
     * Retrieves a {@linkplain User user} with the given username
     * 
     * @param username The username of the {@link User user} to get
     * 
     * @return a {@link User user} object with the matching username
     * <br>
     * null if no {@link User user} with a matching name is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    User getUser(String username) throws IOException;

    /**
     * Creates and saves a {@linkplain User user}
     * 
     * @param user {@linkplain User user} object to be created and saved
     * <br>
     * The id of the User object is ignored and a new uniqe id is assigned
     *
     * @return new {@link User user} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    User createUser(User user) throws IOException;

}