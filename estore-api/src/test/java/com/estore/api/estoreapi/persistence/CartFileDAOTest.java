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
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;

/**
 * Test the Product File DAO class
 * 
 * @author Zach Brown | zrb8768
 * @author Jacob Karvelis | jak9527
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

        Product product1 = new Product(1, "Golisano Trees", 10, 10, "");
        HashMap<Integer, Product> map1 = new HashMap<>() {{
            put(product1.getId(), product1);
        }};
        testCart[0] = new Cart(1, map1 );

        Product product2 = new Product(2, "The Sentinel", 100000, 1, "");
        HashMap<Integer, Product> map2 = new HashMap<>() {{
            put(product2.getId(), product2);
        }};
        testCart[1] = new Cart(2, map2);

        Product product3 = new Product(3, "RIT Bricks", 5, 8700000, "");
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
    public void testGetProductNotFound() {
        // Invoke
        Product product = cartFileDAO.getProduct(29, 5);
        Product Noproduct = cartFileDAO.getProduct(1, 5);

        // Analzye
        assertNull(product);
        assertNull(Noproduct);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Cart[].class));

        Product product1 = new Product(1, "Test", 1, 1, "");
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
        Product item = new Product(102,"Wonder-Brick", 4, 4, "");

        // Invoke
        Product result = assertDoesNotThrow(() -> cartFileDAO.addProduct(1, item),
                                "Unexpected exception thrown");

        Product no_result = assertDoesNotThrow(() -> cartFileDAO.addProduct(5, item),
                                "Unexpected exception thrown");
        // Analyze
        assertNotNull(result);
        assertNull(no_result);
        Product actual = cartFileDAO.getProduct(1, item.getId());
        assertEquals(actual.getId(),item.getId());
        assertEquals(actual.getName(),item.getName());
    }

    @Test
    public void testAddCart(){
        // Setup
        Product product = new Product(50, "Bricks", 10, 10, "");
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

    @Test
    public void testRemoveAllProducts() {
        // Setup
        int initialCount = testCart[0].getProducts().size();

        // Invoke
        boolean result = assertDoesNotThrow(() -> cartFileDAO.removeAllProducts(1),
                            "Unexpected exception thrown");
        
        boolean no_result = assertDoesNotThrow(() -> cartFileDAO.removeAllProducts(29),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        assertEquals(no_result, false);
        assertEquals(cartFileDAO.carts.get(1).getProducts().size(), initialCount - initialCount);
    }
    
    @Test
    public void testUpdateCart() {
        // Setup
        Product item1 = new Product(102,"Wonder-Brick", 4, 4);
        Product item2 = new Product(108, "Suspicious-Brick", 100, 1);
        Product item3 = new Product(105, "Stupendous Brick", 50, 1);
        Product item4 = new Product(1, "Golisano Trees", 10, 4);
        Product[] list = {item1, item2, item3,item4};
        

        Cart testCart = cartFileDAO.getCart(1);
        assertDoesNotThrow(() -> cartFileDAO.addProduct(1, new Product(106,"Wonder-Brick", 4, 4)), 
        "Unexpected exception thrown");

        // Invoke
        boolean result = assertDoesNotThrow(() -> cartFileDAO.updateCart(1, list),
                                "Unexpected exception thrown");

        boolean no_result = assertDoesNotThrow(() -> cartFileDAO.updateCart(40, list),
                                "Unexpected exception thrown");
        // Analyze
        
        assertEquals(result, true);
        assertEquals(no_result, false);
    }

    @Test
    public void testGetCartProducts() {

        // 
        Product product1 = new Product(1, "Golisano Trees", 10, 10);
        Product[] list = {product1};


        // Invoke
        Product[] result = assertDoesNotThrow(() -> cartFileDAO.getCartProducts(1),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result[0].getId(), list[0].getId());

    }
}