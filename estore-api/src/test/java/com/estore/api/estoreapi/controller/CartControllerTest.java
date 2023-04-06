package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.controller.CartController;
import com.estore.api.estoreapi.model.Cart;
import com.estore.api.estoreapi.persistence.CartDAO;
import com.estore.api.estoreapi.persistence.ProductDAO;
import com.estore.api.estoreapi.model.Product;

import java.util.HashMap;

/**
 * Test the Product Controller class
 * 
 * @author Jacob Karvelis
 */
@Tag("Controller-tier")
public class CartControllerTest {
    private CartController cartController;
    private CartDAO mockCartDAO;
    private ProductDAO mockProductDAO;

    /**
     * Before each test, create a new CartController object and inject
     * a mock Cart DAO
     */
    @BeforeEach
    public void setupCartController() {
        mockCartDAO = mock(CartDAO.class);
        mockProductDAO = mock(ProductDAO.class);
        cartController = new CartController(mockCartDAO, mockProductDAO);

    }

    @Test
    public void testGetCart() throws IOException {  // getProduct may throw IOException
        // Setup
        Product product = new Product(1, "Golisano Trees", 10, 10, "");
        HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};
        Cart cart = new Cart(99, expected_products);
        // When the same id is passed in, our mock Product DAO will return the Product object
        when(mockCartDAO.getCart(cart.getId())).thenReturn(cart);

        // Invoke
        ResponseEntity<Cart> response = cartController.getCart(cart.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(cart,response.getBody());
    }

    @Test
    public void testGetCartNotFound() throws Exception { // getCart may throw IOException
        // Setup
        int cartId = 99;
        // When the same id is passed in, our mock Cart DAO will return null, simulating
        // no cart found
        when(mockCartDAO.getCart(cartId)).thenReturn(null);

        // Invoke
        ResponseEntity<Cart> response = cartController.getCart(cartId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetCartHandleException() throws Exception { // getCart may throw IOException
        // Setup
        int cartId = 99;
        // When getCart is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).getCart(cartId);

        // Invoke
        ResponseEntity<Cart> response = cartController.getCart(cartId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all CartController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateCart() throws IOException {  // createCart may throw IOException
        // Setup
        Product product = new Product(1, "Golisano Trees", 10, 10, "");
        HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};
        Cart cart = new Cart(99, expected_products);
        // when addCart is called, return true simulating successful
        // creation and save
        when(mockCartDAO.addCart(cart)).thenReturn(cart);

        // Invoke
        ResponseEntity<Cart> response = cartController.addCart(cart);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(cart,response.getBody());
    }

    @Test
    public void testCreateCartFailed() throws IOException {  // createCart may throw IOException
        // Setup
        Product product = new Product(1, "Golisano Trees", 10, 10, "");
        HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};
        Cart cart = new Cart(99, expected_products);
        // when createCart is called, return false simulating failed
        // creation and save
        when(mockCartDAO.addCart(cart)).thenReturn(null);

        // Invoke
        ResponseEntity<Cart> response = cartController.addCart(cart);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateCartHandleException() throws IOException {  // createCart may throw IOException
        // Setup
        Product product = new Product(1, "Golisano Trees", 10, 10, "");
        HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};
        Cart cart = new Cart(99, expected_products);

        // When createCart is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).addCart(cart);

        // Invoke
        ResponseEntity<Cart> response = cartController.addCart(cart);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testAddProductToCart() throws IOException { // updateCart may throw IOException
        // Setup
        Product product = new Product(1, "Golisano Trees", 10, 10, "");
        HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};
        Cart cart = new Cart(99, expected_products);
        Product newProduct = new Product(2, "Bricks", 5, 8, "");
        // when updateCart is called, return true simulating successful
        // update and save
        when(mockCartDAO.addProduct(cart.getId(), newProduct)).thenReturn(newProduct);
        ResponseEntity<Product> response = cartController.addProductToCart(cart.getId(), newProduct);

        // Invoke
        response = cartController.addProductToCart(cart.getId(), newProduct);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(newProduct,response.getBody());
    }

    @Test
    public void testAddProductToCartFailed() throws IOException { // updateCart may throw IOException
        // Setup
        Product product = new Product(1, "Golisano Trees", 10, 10, "");
        HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};
        Cart cart = new Cart(99, expected_products);
        Product newProduct = new Product(2, "Bricks", 5, 8, "");
        // when updateCart is called, return true simulating successful
        // update and save
        when(mockCartDAO.addProduct(cart.getId(), newProduct)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = cartController.addProductToCart(cart.getId(), newProduct);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testAddProductToCartHandleException() throws IOException { // updateCart may throw IOException
        // Setup
        Product product = new Product(1, "Golisano Trees", 10, 10, "");
        HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};
        Cart cart = new Cart(99, expected_products);
        Product newProduct = new Product(2, "Bricks", 5, 8, "");
        // When updateCart is called on the Mock Pr DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).addProduct(cart.getId(), newProduct);

        // Invoke
        ResponseEntity<Product> response = cartController.addProductToCart(cart.getId(), newProduct);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateProductCount() throws IOException { // updateCart may throw IOException
        // Setup
        Product product = new Product(1, "Golisano Trees", 10, 10, "");
        HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};
        Cart cart = new Cart(99, expected_products);
        // when updateCart is called, return true simulating successful
        // update and save
        when(mockCartDAO.updateProductCount(cart.getId(), 1, 5)).thenReturn(product);
        ResponseEntity<Product> response = cartController.updateProductCount(cart.getId(), 1, 5);

        // Invoke
        response = cartController.updateProductCount(cart.getId(), 1, 5);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(product,response.getBody());
    }

    @Test
    public void testUpdateProductCountFailed() throws IOException { // updateCart may throw IOException
        // Setup
        Product product = new Product(1, "Golisano Trees", 10, 10, "");
        HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};
        Cart cart = new Cart(99, expected_products);
        // when updateCart is called, return true simulating successful
        // update and save
        when(mockCartDAO.updateProductCount(cart.getId(), 1, 5)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = cartController.updateProductCount(cart.getId(), 1, 5);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateProductCountHandleException() throws IOException { // updateCart may throw IOException
        // Setup
        Product product = new Product(1, "Golisano Trees", 10, 10, "");
        HashMap<Integer, Product> expected_products = new HashMap<>() {{
            put(product.getId(), product);
        }};
        Cart cart = new Cart(99, expected_products);
        // When updateCart is called on the Mock Pr DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).updateProductCount(cart.getId(), 1, 10);

        // Invoke
        ResponseEntity<Product> response = cartController.updateProductCount(cart.getId(), 1, 10);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testRemoveProduct() throws IOException { // deleteCart may throw IOException
        // Setup
        int cartId = 99;
        int productId = 99;
        // when deleteCart is called return true, simulating successful deletion
        when(mockCartDAO.removeProduct(cartId, productId)).thenReturn(true);

        // Invoke
        ResponseEntity<Product> response = cartController.removeProduct(cartId, productId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testRemoveProductNotFound() throws IOException { // deleteCart may throw IOException
        // Setup
        int cartId = 99;
        int productId = 99;
        // when deleteCart is called return false, simulating failed deletion
        when(mockCartDAO.removeProduct(cartId, productId)).thenReturn(false);

        // Invoke
        ResponseEntity<Product> response = cartController.removeProduct(cartId, productId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testRemoveProductHandleException() throws IOException { // deleteCart may throw IOException
        // Setup
        int cartId = 99;
        int productId = 99;
        // When deleteCart is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).removeProduct(cartId, productId);

        // Invoke
        ResponseEntity<Product> response = cartController.removeProduct(cartId, productId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
