package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.User;

/**
 * Defines the interface for User instancing
 * 
 * @author Jacob Karvelis jak9527
 */
public interface CurrentUserDAO {

    /**
     * Retrieves the {@linkplain User user} currently logged in
     * 
     * 
     * @return the {@link User user} currently logged in
     * <br>
     * null if no {@link User user} is logged in
     * 
     * @throws IOException if an issue with underlying storage
     */
    User getCurrentUser() throws IOException;

    /**
     * Sets the current {@linkplain User user} and saves it
     * 
     * @param user {@linkplain User user} object to set
     * <br>
     *
     * @return new {@link User user} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    User setCurrentUser(User user) throws IOException;

    /**
     * Deletes a {@linkplain User user}
     * 
     * @param user {@linkplain User user} object to be created and saved
     * <br>
     * The id of the User object is ignored and a new uniqe id is assigned
     *
     * @return new {@link User user} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    User deleteUser(User user) throws IOException;

}