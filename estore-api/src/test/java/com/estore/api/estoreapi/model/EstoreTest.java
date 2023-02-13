package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Product;

/**
 * The unit test suite for the Hero class
 * 
 * @author SWEN Faculty
 */
@Tag("Model-tier")
public class EstoreTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";

        // Invoke
        Product hero = new Product(expected_id,expected_name);

        // Analyze
        assertEquals(expected_id,hero.getId());
        assertEquals(expected_name,hero.getName());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "Wi-Fire";
        Product hero = new Product(id,name);

        String expected_name = "Galactic Agent";

        // Invoke
        hero.setName(expected_name);

        // Analyze
        assertEquals(expected_name,hero.getName());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String name = "Wi-Fire";
        String expected_string = String.format(Product.STRING_FORMAT,id,name);
        Product hero = new Product(id,name);

        // Invoke
        String actual_string = hero.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}