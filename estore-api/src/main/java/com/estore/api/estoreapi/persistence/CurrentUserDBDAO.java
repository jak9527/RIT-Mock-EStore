package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.CurrentUser;

/**
 * Implements the functionality for Database persistence for tracking the CurrentUser
 * through MongoDB.
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Andrew Bush (apb2471@rit.edu)
 */

@Component
public class CurrentUserDBDAO implements CurrentUserDAO {
    @Autowired
    CurrentUserRepository cuRepo;

    @Autowired
    UserRepository uRepo;

    /**
     * Generates the next id for a new {@Cart Cart cart}
     * 
     * @return The next id
     */
    private synchronized int getId(String username) {
        return uRepo.findByUsername(username).getId();
    }

    @Override
    public CurrentUser getCurrentUser() throws IOException {
        List<CurrentUser> users = cuRepo.findAll();
        if (users.size() == 1) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public CurrentUser setCurrentUser(CurrentUser user) throws IOException {
        List<CurrentUser> users = cuRepo.findAll();
        if (users.size() != 0){
            deleteUser(getCurrentUser());
        }
        user.setId(getId(user.getUsername()));
        cuRepo.insert(user);
        return user;
    }

    @Override
    public CurrentUser deleteUser(CurrentUser user) throws IOException {
        if (user == null){
            return null;
        }
        
        CurrentUser u = cuRepo.findByUsername(user.getUsername());
        if (u == null) {
            return null;
        }
        cuRepo.delete(u.getId());
        return u;
    }


    
}
