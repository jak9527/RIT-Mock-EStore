package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.controller.UserController;
import com.estore.api.estoreapi.model.AuctionItem;
import com.estore.api.estoreapi.model.Bid;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.AuctionItemDAO;
import com.estore.api.estoreapi.persistence.UserDAO;


/**
 * Test the User Controller class
 * 
 * @author Ethan Meyers epm2875
 */
public class AuctionControllerTest{
    private AuctionController auctionController;
    private AuctionItemDAO mockAuctionDAO;

        /**
     * Before each test, create a new ProductController object and inject
     * a mock USer DAO
     */
    @BeforeEach
    public void setupAuctionController() {
        mockAuctionDAO = mock(AuctionItemDAO.class);
        try {
			auctionController = new AuctionController(mockAuctionDAO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Test
    public void testGetAuction() throws IOException{
        int expected_id = 1;
        String expected_username = "admin";
        Product expected_product = new Product(0, "testProduct", 2, 5, null);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        LocalDateTime expected_endTime = LocalDateTime.parse("2023-04-06-12:30:00", dtf);
        Bid old_bid = new Bid((float)5.0, expected_username);
        AuctionItem auction = new AuctionItem(expected_id, expected_product, expected_endTime, old_bid);
        when(mockAuctionDAO.getAuction()).thenReturn(auction);
        ResponseEntity<AuctionItem> response = auctionController.getCurrentAuction();
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(auction,response.getBody());
    }

    // @Test
    // public void testCreateAuction() throws IOException{
    //     int expected_id = 1;
    //     String expected_username = "admin";
    //     Product expected_product = new Product(0, "testProduct", 2, 5);
    //     DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
    //     LocalDateTime expected_endTime = LocalDateTime.parse("2023-04-06-12:30:00", dtf);
    //     Bid old_bid = new Bid((float)5.0, expected_username);
    //     when(mockAuctionDAO.createAuction(expected_id, expected_product, expected_endTime, old_bid)).thenReturn(new AuctionItem(expected_id, expected_product, expected_endTime, old_bid)); 
    //     ResponseEntity<AuctionItem> response = auctionController.createAuction(expected_product, expected_id, expected_username, (float)5.0, "2023-04-06-12:30:00");
    //     // assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    //     assertEquals(1, response.getBody().getId());
    // }

    // @Test
    // public void testGetUserHandleException() throws Exception {
    //     String username = "user302";
    //     doThrow(new IOException()).when(mockAuctionDAO).getUser(username);
    //     ResponseEntity<User> response = auctionController.getUser(username);
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    // }

    // @Test
    // public void TestCreateUser() throws IOException {
    //     User user = new User(9, "User22");
    //     when(mockAuctionDAO.createUser(user)).thenReturn(user);
    //     ResponseEntity<User> response = auctionController.createUser(user);
    //     assertEquals(HttpStatus.CREATED,response.getStatusCode());
    //     assertEquals(user,response.getBody());
    // }

    // @Test
    // public void testCreateUserFailed() throws IOException{
    //     User user = new User(9, "User22");
    //     when(mockAuctionDAO.createUser(user)).thenReturn(null);
    //     ResponseEntity<User> response = auctionController.createUser(user);
    //     assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    // }

    // @Test
    // public void testCreateUserHandleException() throws IOException{
    //     User user = new User(9, "User22");
    //     doThrow(new IOException()).when(mockAuctionDAO).createUser(user);
    //     ResponseEntity<User> response = auctionController.createUser(user);
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    // }
}