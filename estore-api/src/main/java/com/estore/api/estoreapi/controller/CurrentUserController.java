package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.estore.api.estoreapi.model.CurrentUser;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.CurrentUserDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Handles the REST API requests for the User resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Jacob Karvelis jak9527
 */

 @RestController
 @RequestMapping("currentUser")
public class CurrentUserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private CurrentUserDAO currentUserDao;

        /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param currentUserDao The {@link UserDAO User Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public CurrentUserController(CurrentUserDAO currentUserDao) throws IOException{
        this.currentUserDao = currentUserDao;
        // currentUserDao.deleteUser(currentUserDao.getCurrentUser());
    }


    /**
     * Responds to the GET request for the current {@linkplain User user}
     * 
     * @return ResponseEntity with {@link User user} object and
     * HTTP status of OK if there is a current user<br>
     * ResponseEntity with HTTP status of NOT_FOUND if no user is logged in
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<User> getCurrentUser(){
        LOG.info("GET /currentUser");
        try{
            CurrentUser cUser = currentUserDao.getCurrentUser();
            if( cUser == null ) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            User user = new User(cUser.getId(), cUser.getUsername());
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Sets the {@linkplain User user} that is currently logged in
     * 
     * @param user - The {@link User user} to set as logged in
     * 
     * @return ResponseEntity with created {@link User user} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link User user} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<User> setCurrentUser(@RequestBody User user){
        LOG.info("POST /currentUser " + user);
        try {
            CurrentUser cUser = new CurrentUser(user.getId(), user.getUsername());
            CurrentUser newCUser = currentUserDao.setCurrentUser(cUser);
            if( newCUser == null ) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            return new ResponseEntity<User>(user, HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Logs out the current {@linkplain User user} by deleting them
     * 
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if no user is logged in<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("")
    public ResponseEntity<User> deleteUser() {
        LOG.info("DELETE /currentUser/");

        try {
            CurrentUser delete = currentUserDao.deleteUser(currentUserDao.getCurrentUser());
            if( delete == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}   
