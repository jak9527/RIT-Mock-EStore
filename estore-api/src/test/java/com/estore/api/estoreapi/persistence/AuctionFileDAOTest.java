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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.estore.api.estoreapi.model.AuctionItem;
import com.estore.api.estoreapi.model.Bid;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.CurrentUserFileDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Current User File DAO class
 * 
 * @author Jacob Karvelis
 */
@Tag("Persistence-tier")
public class AuctionFileDAOTest {
    AuctionItemFileDAO auctionItemFileDAO;
    AuctionItem[] testAuction;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupUserFileDAO() throws IOException  {
        mockObjectMapper = mock(ObjectMapper.class);
        testAuction = new AuctionItem[1];
        int expected_id = 1;
        String expected_username = "admin";
        Product expected_product = new Product(0, "testProduct", 2, 5, null);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        LocalDateTime expected_endTime = LocalDateTime.parse("2023-04-06-12:30:00", dtf);
        Bid old_bid = new Bid((float)5.0, expected_username);
        testAuction[0] = new AuctionItem(expected_id, expected_product, expected_endTime, old_bid);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the product array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),AuctionItem[].class))
                .thenReturn(testAuction);
        auctionItemFileDAO = new AuctionItemFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public synchronized void testGetAuction() throws IOException{
        //setup
        

        // Invoke
        AuctionItem auction = auctionItemFileDAO.getAuction();

        // Analzye
        assertEquals(auction,testAuction[0]);
    }

    @Test
    public synchronized void testDeleteAuction() throws IOException{
        //setup
        // auctionItemFileDAO.setCurrentUser(testAuction[0]);

        // Invoke
        boolean deleted = auctionItemFileDAO.deleteAuction();

        // Analzye
        assertEquals(true,deleted);
    }

    @Test
    public synchronized void testDeleteAuctionNotFound() throws IOException{
        //setup

        // Invoke
        boolean deleted = auctionItemFileDAO.deleteAuction();
        deleted = auctionItemFileDAO.deleteAuction();

        // Analzye
        assertEquals(false,deleted);
    }

    @Test
    public void testCreateAuction() {
        // Setup
        int expected_id = 1;
        String expected_username = "admin";
        Product expected_product = new Product(0, "testProduct", 2, 5, null);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        LocalDateTime expected_endTime = LocalDateTime.parse("2023-04-06-12:30:00", dtf);
        Bid old_bid = new Bid((float)5.0, expected_username);
        // AuctionItem newAuction = new AuctionItem(expected_id, expected_product, expected_endTime, old_bid);

        // Invoke
        AuctionItem result = assertDoesNotThrow(() -> auctionItemFileDAO.createAuction(expected_id, expected_product, expected_endTime, old_bid));

        // Analyze
        assertNotNull(result);
        AuctionItem actual = assertDoesNotThrow(() -> auctionItemFileDAO.getAuction());
        assertEquals(actual.getId(),result.getId());
        assertEquals(actual.getProduct(),result.getProduct());
    }


    // @Test
    // public void testPlaceBid(){
    //     auctionItemFileDAO.getAuction();
    // }



    // @Test
    // public void testSaveException() throws IOException{
    //     doThrow(new IOException())
    //         .when(mockObjectMapper)
    //             .writeValue(any(File.class),any(User[].class));

    //     // User user = new User(102,"Fungi");
    //     int expected_id = 1;
    //     String expected_username = "admin";
    //     Product expected_product = new Product(0, "testProduct", 2, 5);
    //     DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
    //     LocalDateTime expected_endTime = LocalDateTime.parse("2023-04-06-12:30:00", dtf);
    //     Bid old_bid = new Bid((float)5.0, expected_username);

    //     assertThrows(IOException.class,
    //                     () -> auctionItemFileDAO.createAuction(expected_id, expected_product, expected_endTime, old_bid),
    //                     "IOException not thrown");
    // }

    // @Test
    // public void testGetUserNotFound() {
    //     // Invoke
    //     User user = auctionItemFileDAO.getCurrentUser();

    //     // Analyze
    //     assertEquals(user,null);
    // }


    // @Test
    // public void testConstructorException() throws IOException {
    //     // Setup
    //     ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
    //     // We want to simulate with a Mock Object Mapper that an
    //     // exception was raised during JSON object deseerialization
    //     // into Java objects
    //     // When the Mock Object Mapper readValue method is called
    //     // from the ProductFileDAO load method, an IOException is
    //     // raised
    //     doThrow(new IOException())
    //         .when(mockObjectMapper)
    //             .readValue(new File("doesnt_matter.txt"),User[].class);

    //     // Invoke & Analyze
    //     assertThrows(IOException.class,
    //                     () -> new CurrentUserFileDAO("doesnt_matter.txt",mockObjectMapper),
    //                     "IOException not thrown");
    // }
}