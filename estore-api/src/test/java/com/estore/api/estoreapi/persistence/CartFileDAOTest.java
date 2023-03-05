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

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.Cart;
import com.estore.api.estoreapi.persistence.ProductFileDAO;
import com.estore.api.estoreapi.persistence.CartFileDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

/**
 * Test the Product File DAO class
 * 
 * @author Zach Brown | zrb8768
 */
@Tag("Persistence-tier")
public class CartFileDAOTest {
    ProductFileDAO productFileDAO;
    CartFileDAO cartFileDAO;
    Cart[] testCart;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupCartFileDAO() throws IOException {
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
    public void testGetProduct() {
        // Invoke
        Product product = cartFileDAO.getProduct(1, 1);

        // Analzye
        assertEquals(product, testCart[0].getProducts().get(1) );
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Cart[].class));

        Product product1 = new Product(1, "Test", 1, 1);
        HashMap<Integer, Product> map1 = new HashMap<>() {{
            put(product1.getId(), product1);
        }};

        Cart cart = new Cart(102, map1);

        assertThrows(IOException.class,
                        () -> cartFileDAO.addCart(cart),
                        "IOException not thrown");
    }

    @Test
    public void testGetCartNotFound() {
        // Invoke
        Cart cart = cartFileDAO.getCart(98);

        // Analyze
        assertEquals(cart,null);
    }

}