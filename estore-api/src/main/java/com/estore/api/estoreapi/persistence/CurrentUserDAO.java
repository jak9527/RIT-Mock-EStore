package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.CurrentUser;

/**
 * Defines the interface for CurrentUser instancing
 * 
 * @author Jacob Karvelis jak9527
 */
public interface CurrentUserDAO {

    /**
     * Retrieves the {@linkplain CurrentUser user} currently logged in
     * 
     * 
     * @return the {@link CurrentUser user} currently logged in
     * <br>
     * null if no {@link CurrentUser user} is logged in
     * 
     * @throws IOException if an issue with underlying storage
     */
    CurrentUser getCurrentUser() throws IOException;

    /**
     * Sets the current {@linkplain CurrentUser user} and saves it
     * 
     * @param user {@linkplain CurrentUser user} object to set
     * <br>
     *
     * @return new {@link CurrentUser user} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    CurrentUser setCurrentUser(CurrentUser user) throws IOException;

    /**
     * Deletes a {@linkplain CurrentUser user}
     * 
     * @param user {@linkplain CurrentUser user} object to be created and saved
     * <br>
     * The id of the CurrentUser object is ignored and a new uniqe id is assigned
     *
     * @return new {@link CurrentUser user} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    CurrentUser deleteUser(CurrentUser user) throws IOException;

}