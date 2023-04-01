package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.estore.api.estoreapi.model.AuctionItem;
import com.estore.api.estoreapi.model.Bid;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.AuctionItemDAO;
import com.fasterxml.jackson.databind.util.ArrayBuilders.BooleanBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Handles the REST API requests for the User resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Jacob Karvelis jak9527
 */

 @RestController
 @RequestMapping("auction")
public class AuctionController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private AuctionItemDAO auctionDao;

        /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param auctionDao The {@link AuctionItemDAO auction Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public AuctionController(AuctionItemDAO auctionDao) throws IOException{
        this.auctionDao = auctionDao;
    }


    /**
     * Responds to the GET request for the currently
     * running {@linkplain AuctionItem auction}
     * 
     * @return ResponseEntity with {@link AuctionItem auction} object and
     * HTTP status of OK if there is a current auction<br>
     * ResponseEntity with HTTP status of NOT_FOUND if no auction is runnning
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<AuctionItem> getCurrentAuction(){
        LOG.info("GET /auction");
        try{
            AuctionItem auction = auctionDao.getAuction();
            if( auction == null ) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<AuctionItem>(auction, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new {@linkplain AuctionItem auction} and changes the current auction to it
     * 
     * @param product The product to auction off
     * @param username The userName to set as the max bid
     * @param bid The max bid cost value as a string
     * @param endDateTime the end datetime as a string in the format
     * yyyy-MM-dd hh:mm:ss
     * 
     * @return ResponseEntity with created {@link AuctionItem auction} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link AuctionItem auction} conflict occurs<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/{aId}/{username}/{bid}/{endDateTime}")
    public ResponseEntity<AuctionItem> createAuction(@RequestBody Product product, @PathVariable int aId, 
    @PathVariable String username, @PathVariable float bid, @PathVariable String endDateTime){
        LOG.info("POST /auction/" + aId + "/" + username + "/" + bid + "/" + endDateTime + " " + product );
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            LocalDateTime endDate = LocalDateTime.parse(endDateTime, dtf);

            Bid maxBid = new Bid(bid, username);

            AuctionItem newAuction = auctionDao.createAuction(aId, product, endDate, maxBid);
            if( newAuction == null ) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<AuctionItem>(newAuction,HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the currently running {@linkplain AuctionItem auction}
     * 
     * @param product The product to replace the current product with
     * @param username The username of the user to replace the current max bid with
     * <br> this will likely be either the current one, or admin if they are resetting it
     * @param bid The price of the updated max bid. Either the current max, or whatever
     * <br> the desired minimum bid is for a new auction if the admin is making one
     * @param endDateTime The new end time in yyyy-MM-dd hh:mm:ss format
     * 
     * @return ResponseEntity with updated {@link AuctionItem auction} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link AuctionItem auction} conflict occurs<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{username}/{bid}/{endDateTime}")
    public ResponseEntity<AuctionItem> updateAuction(@RequestBody Product product,
    @PathVariable String username, @PathVariable float bid, @PathVariable String endDateTime){
        LOG.info("PUT /auction/" + username + "/" + bid + "/" + endDateTime + " " + product );
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            LocalDateTime endDate = LocalDateTime.parse(endDateTime, dtf);

            Bid maxBid = new Bid(bid, username);

            AuctionItem newAuction = auctionDao.updateAuction(product, endDate, maxBid);
            if( newAuction == null ) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<AuctionItem>(newAuction,HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes the currently running {@linkplain AuctionItem auction}
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if no user is logged in<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteAuction() {
        LOG.info("DELETE /auction/");

        try {
            boolean delete = auctionDao.deleteAuction();
            if(!delete) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Attempts to place a new {@linkplain Bid bid} on the currently running
     * {@linkplain AuctionItem auction} 
     * 
     * @param username The username of the user placing the auction
     * @param bid The dollar amount of the bid
     * @return ResponseEntity HTTP status of OK if the bid was placed<br>
     * ResponseEntity with HTTP status of CONFLICT if the bid was too low<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{username}/{bid}")
    public ResponseEntity<Bid> placeBid(@PathVariable String username, @PathVariable float bid){
        LOG.info("PUT /auction/" + username + "/" + bid );
        try {

            boolean bidPlaced = auctionDao.placeBid(username, bid);
            if( !bidPlaced ) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<Bid>(new Bid(bid, username),HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}   
