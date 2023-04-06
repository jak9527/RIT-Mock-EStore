package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

import com.estore.api.estoreapi.model.Product;

/**
 * The unit test suite for the Cart class
 * 
 * @author Zach Brown | zrb8768
 */
@Tag("Model-tier")
public class CartTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 1;
        Product product = new Product(1, "Golisano Trees", 10, 10, "");
        HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};


        // Invoke
        Cart cart = new Cart(expected_id, expected_products);


        // Analyze
        assertEquals(expected_id,cart.getId());
        assertEquals(expected_products,cart.getProducts());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 1;
        Product product = new Product(1, "Golisano Trees", 10, 10, "");
                HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};
        String expected_string = String.format(Cart.STRING_FORMAT,id, expected_products);
        Cart cart = new Cart(id, expected_products);

        // Invoke
        String actual_string = cart.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }

    @Test
    public void testGetTotal() {
        // Setup
        int expected_id = 1;
        Product product = new Product(1, "Golisano Trees", 10, 10);
        HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};


        // Invoke
        Cart cart = new Cart(expected_id, expected_products);


        // Analyze
        assertEquals(cart.getTotal(), product.getPrice() * product.getQuantity() );
    }
}