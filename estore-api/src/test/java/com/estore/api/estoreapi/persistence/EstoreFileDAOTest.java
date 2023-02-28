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
import com.estore.api.estoreapi.persistence.ProductFileDAO;
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
public class EstoreFileDAOTest {
    ProductFileDAO productFileDAO;
    Product[] testProduct;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupProductFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testProduct = new Product[3];
        testProduct[0] = new Product(99,"Wi-Fire", 1, 1);
        testProduct[1] = new Product(100,"Galactic Agent", 2, 2);
        testProduct[2] = new Product(101,"Ice Gladiator", 3, 3);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the product array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Product[].class))
                .thenReturn(testProduct);
        productFileDAO = new ProductFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetProducts() {
        // Invoke
        Product[] products = productFileDAO.getProducts();

        // Analyze
        assertEquals(products.length,testProduct.length);
        for (int i = 0; i < testProduct.length;++i)
            assertEquals(products[i],testProduct[i]);
    }

    @Test
    public void testFindProducts() {
        // Invoke
        Product[] products = productFileDAO.findProducts("la");

        // Analyze
        assertEquals(products.length,2);
        assertEquals(products[0],testProduct[1]);
        assertEquals(products[1],testProduct[2]);
    }

    @Test
    public void testGetProduct() {
        // Invoke
        Product product = productFileDAO.getProduct(99);

        // Analzye
        assertEquals(product,testProduct[0]);
    }

    @Test
    public void testDeleteProduct() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> productFileDAO.deleteProduct(99),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test products array - 1 (because of the delete)
        // Because products attribute of ProductFileDAO is package private
        // we can access it directly
        assertEquals(productFileDAO.products.size(),testProduct.length-1);
    }

    @Test
    public void testCreateProduct() {
        // Setup
        Product item = new Product(102,"Wonder-Person", 4, 4);

        // Invoke
        Product result = assertDoesNotThrow(() -> productFileDAO.createProduct(item),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Product actual = productFileDAO.getProduct(item.getId());
        assertEquals(actual.getId(),item.getId());
        assertEquals(actual.getName(),item.getName());
    }

    @Test
    public void testUpdateProduct() {
        // Setup
        Product product = new Product(99,"Galactic Agent", 5, 5);

        // Invoke
        Product result = assertDoesNotThrow(() -> productFileDAO.updateProduct(product),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Product actual = productFileDAO.getProduct(product.getId());
        assertEquals(actual,product);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Product[].class));

        Product item = new Product(102,"Wi-Fire", 6, 6);

        assertThrows(IOException.class,
                        () -> productFileDAO.createProduct(item),
                        "IOException not thrown");
    }

    @Test
    public void testGetProductNotFound() {
        // Invoke
        Product product = productFileDAO.getProduct(98);

        // Analyze
        assertEquals(product,null);
    }

    @Test
    public void testDeleteProductNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> productFileDAO.deleteProduct(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(productFileDAO.products.size(),testProduct.length);
    }

    @Test
    public void testUpdateProductNotFound() {
        // Setup
        Product product = new Product(98,"Bolt", 10, 10);

        // Invoke
        Product result = assertDoesNotThrow(() -> productFileDAO.updateProduct(product),
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
        // from the ProductFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Product[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new ProductFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}