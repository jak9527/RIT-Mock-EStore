package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.User;

/**
 * Implements the functionality for Database persistence for Users
 * through MongoDB.
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Andrew Bush (apb2471@rit.edu)
 */

@Component
public class UserDBDAO implements UserDAO{

    @Autowired
    UserRepository userRepo;

    /**
     * Generates the next id for a new {@Cart Cart cart}
     * 
     * @return The next id
     */
    private synchronized int nextId() {
        List<User> uList = userRepo.findByOrderByIdDesc();
        if (uList.isEmpty()) {
            return 0;
        }
        int max = uList.get(0).getId();
        return max + 1;
    }

    @Override
    public User getUser(String username) throws IOException {
        return userRepo.findByUsername(username);
    }

    @Override
    public User createUser(User user) throws IOException {
        if (getUser(user.getUsername()) != null) {
            return null;
        }
        User newUser = new User(nextId(), user.getUsername());
        userRepo.insert(newUser);
        return newUser;
    }
}
