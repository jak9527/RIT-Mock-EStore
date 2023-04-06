package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.Bid;

/**
 * The unit test suite for the Product class
 * 
 * @author SWEN Faculty
 */
@Tag("Model-tier")
public class AuctionItemTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 1;
        String expected_username = "admin";
        Product expected_product = new Product(0, "testProduct", 2, 5);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        LocalDateTime expected_endTime = LocalDateTime.parse("2023-04-06-12:30:00", dtf);
        Bid expected_bid = new Bid((float)5.0, expected_username);

        // Invoke
        AuctionItem auctionItem = new AuctionItem(expected_id, expected_product, expected_endTime, expected_bid);

        // Analyze
        assertEquals(expected_id,auctionItem.getId());
        assertEquals(expected_product,auctionItem.getProduct());
        assertEquals(expected_endTime, auctionItem.getEndTime());
        assertEquals(expected_bid, auctionItem.getMaxBid());
    }

    @Test
    public void testSetProduct() {
        // Setup
        int expected_id = 1;
        String expected_username = "admin";
        Product old_product = new Product(0, "testProduct", 2, 5);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        LocalDateTime expected_endTime = LocalDateTime.parse("2023-04-06-12:30:00", dtf);
        Bid expected_bid = new Bid((float)5.0, "admin");

        Product expected_product = new Product(1, "expected", 5, 1);

        // Invoke
        AuctionItem auctionItem = new AuctionItem(expected_id, old_product, expected_endTime, expected_bid);
        auctionItem.setProduct(expected_product);

        // Analyze
        assertEquals(expected_product,auctionItem.getProduct());
    }


}