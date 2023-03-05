package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.User;

/**
 * The unit test suite for the Product class
 * 
 * @author SWEN Faculty
 */
@Tag("Model-tier")
public class UserTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_username = "Wi-Fire";

        // Invoke
        User user = new User(expected_id,expected_username);

        // Analyze
        assertEquals(expected_id,user.getId());
        assertEquals(expected_username,user.getUsername());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String username = "Wi-Fire";
        User user = new User(id,username);

        String expected_name = "Galactic Agent";

        // Invoke
        user.setUsername(expected_name);

        // Analyze
        assertEquals(expected_name,user.getUsername());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String username = "Wi-Fire";
        String expected_string = String.format(User.STRING_FORMAT,id,username);
        User user = new User(id,username);

        // Invoke
        String actual_string = user.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}