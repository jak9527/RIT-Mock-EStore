package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.estore.api.estoreapi.model.Cart;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.CartFileDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Product File DAO class
 * 
 * @author SWEN Faculty
 */
@Tag("Persistence-tier")
public class CartFileDAOTest {
    CartFileDAO cartFileDAO;
    Cart[] testCart;
    ObjectMapper mockObjectMapper;


    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupProductFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testCart = new Cart[3];

        Product product1 = new Product(1, "Golisano Trees", 10, 10);
        HashMap<Integer, Product> map1 = new HashMap<>() {{
            put(product1.getId(), product1);
        }};
        testCart[0] = new Cart(1, map1 );

        Product product2 = new Product(2, "The Sentinel", 100000, 1);
        HashMap<Integer, Product> map2 = new HashMap<>() {{
            put(product2.getId(), product2);
        }};
        testCart[1] = new Cart(2, map2);

        Product product3 = new Product(3, "RIT Bricks", 5, 8700000);
        HashMap<Integer, Product> map3 = new HashMap<>() {{
            put(product3.getId(), product3);
        }};
        testCart[2] = new Cart(3, map3);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the product array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Cart[].class))
                .thenReturn(testCart);
        cartFileDAO = new CartFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testRemoveProduct() {
        // Setup
        int initialCount = testCart[0].getProducts().size();

        // Invoke
        boolean result = assertDoesNotThrow(() -> cartFileDAO.removeProduct(1, 1),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test products array - 1 (because of the delete)
        // Because products attribute of ProductFileDAO is package private
        // we can access it directly
        assertEquals(cartFileDAO.carts.get(1).getProducts().size(), initialCount-1);
    }

    @Test
    public void testAddProduct() {
        // Setup
        Product item = new Product(102,"Wonder-Brick", 4, 4);

        // Invoke
        Product result = assertDoesNotThrow(() -> cartFileDAO.addProduct(1, item),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Product actual = cartFileDAO.getProduct(1, item.getId());
        assertEquals(actual.getId(),item.getId());
        assertEquals(actual.getName(),item.getName());
    }

    @Test
    public void testAddCart(){
        // Setup
        Product product = new Product(50, "Bricks", 10, 10);
        HashMap<Integer, Product> map = new HashMap<>() {{
            put(product.getId(), product);
        }};
        Cart cart = new Cart(99, map);

        // Invoke
        Cart result = assertDoesNotThrow(() -> cartFileDAO.addCart(cart), 
                            "Unexpected Exception Thrown");
        
        // Analyze
        assertNotNull(result);
        Cart actual = cartFileDAO.getCart(4);
        assertEquals(actual.getId(), 4);
        assertEquals(actual.getProducts(), cart.getProducts());
    }

    @Test
    public void testUpdateProductCount() {
        // Setup
        int count = 4;
        int initial = cartFileDAO.getProduct(1, 1).getQuantity();

        // Invoke
        Product result = assertDoesNotThrow(() -> cartFileDAO.updateProductCount(1, 1, count),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        int actual = cartFileDAO.getProduct(1, 1).getQuantity();
        assertEquals(actual, initial+count);
    }

    @Test
    public void testRemoveProductNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> cartFileDAO.removeProduct(2, 1),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(cartFileDAO.carts.get(1).getProducts().size(),testCart[0].getProducts().size());
    }

    @Test
    public void testUpdateProductNotFoundCount() {
        // Setup
        int count = 4;

        // Invoke
        Product result = assertDoesNotThrow(() -> cartFileDAO.updateProductCount(100, 100, count),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the CartFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Cart[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new CartFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}