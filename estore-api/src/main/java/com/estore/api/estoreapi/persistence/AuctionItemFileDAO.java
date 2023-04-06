package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.time.LocalDateTime;

import com.estore.api.estoreapi.model.AuctionItem;
import com.estore.api.estoreapi.model.Bid;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Auctions
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Jacob Karvelis jak9527
 */
@Component
public class AuctionItemFileDAO implements AuctionItemDAO {
    private static final Logger LOG = Logger.getLogger(AuctionItemFileDAO.class.getName());
    Map<Integer,AuctionItem> auctions;    // Provides a local cache of the Auction objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Auction
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new auction
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Auction File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public AuctionItemFileDAO(@Value("${currentAuction.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the auctions from the file
    }

    /**
     * Generates an array of {@linkplain AuctionItem auctions} from the tree map 
     * 
     * @return  The array of {@link AuctionItem auctions}, may be empty
     */
    private AuctionItem[] getAuctionArray() {
        ArrayList<AuctionItem> auctionArrayList = new ArrayList<>();

        for (AuctionItem auction : auctions.values()) {
            auctionArrayList.add(auction);
        }

        AuctionItem[] auctionArray = new AuctionItem[auctionArrayList.size()];
        auctionArrayList.toArray(auctionArray);
        return auctionArray;
    }

    /**
     * Saves the {@linkplain AuctionItem auctions} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link AuctionItem auctions} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        AuctionItem[] auctionArray = getAuctionArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), auctionArray);
        return true;
    }

    /**
     * Loads {@linkplain AuctionItem auctions} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        auctions = new TreeMap<>();
        nextId = -1;

        // Deserializes the JSON objects from the file into an array of auctions
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        AuctionItem[] auctionArray = objectMapper.readValue(new File(filename),AuctionItem[].class);

        // Add each auction to the tree map and keep track of the greatest id
        for (AuctionItem auction : auctionArray) {
            auctions.put(auction.getId(),auction);
            if (auction.getId() > nextId)
                nextId = auction.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    @Override
    public AuctionItem getAuction() throws IOException {
        synchronized(auctions){
            if(auctions.size()==1){
                return (AuctionItem) auctions.values().toArray()[0];
            } else {
                return null;
            }
        }
        // throw new UnsupportedOperationException("Unimplemented method 'getAuction'");
    }

    @Override
    public AuctionItem createAuction(int aId, Product product, LocalDateTime endTime, Bid maxBid) throws IOException {
        // TODO Auto-generated method stub
        synchronized(auctions){
            if(auctions.size() != 0){
                deleteAuction();
            }
            AuctionItem newAuction = new AuctionItem(aId, product, endTime, maxBid);
            auctions.put(aId, newAuction);
            save();
            return newAuction;
        }
        // throw new UnsupportedOperationException("Unimplemented method 'createAuction'");
    }

    // @Override
    // public AuctionItem updateAuction(Product product, LocalDateTime endTime, Bid maxBid) throws IOException {
    //     // TODO Auto-generated method stub
    //     synchronized(auctions){
    //         if(auctions.size() == 0){
    //             //no current auction
    //             return null;
    //         } else {
    //             AuctionItem[] auctionArray = getAuctionArray();
    //             auctionArray[0].setProduct(product); //update product
    //             auctionArray[0].setEndTime(endTime); //update end time
    //             auctionArray[0].setMaxBid(maxBid); //update max big
    //             //Actually change the array
    //             auctions.put(auctionArray[0].getId(), auctionArray[0]);
    //             save();
    //             return auctionArray[0];
    //         }
    //     }
    //     // throw new UnsupportedOperationException("Unimplemented method 'updateAuction'");
    // }

    @Override
    public boolean deleteAuction() throws IOException {
        // TODO Auto-generated method stub
        synchronized(auctions){
            if(auctions.size() == 0){
                //no current auction
                return false;
            } else {
                auctions.clear();
                save();
                return true;
            }
        }
        // throw new UnsupportedOperationException("Unimplemented method 'deleteAuction'");
    }

    @Override
    public boolean placeBid(String username, float bid) throws IOException {
        // TODO Auto-generated method stub
        synchronized(auctions){
            if(auctions.size() == 0){
                return false;
            } else {
                AuctionItem currentAuction = getAuction();
                if(currentAuction.getMaxBid().getBid() >= bid){
                    return false;
                } else {
                    currentAuction.setMaxBid(new Bid(bid, username));
                    auctions.put(currentAuction.getId(), currentAuction);
                    save();
                    return true;
                }
            }
        }
        // throw new UnsupportedOperationException("Unimplemented method 'placeBid'");
    }

    @Override
    public boolean auctionOver() throws IOException {
        // TODO Auto-generated method stub
        synchronized(auctions){
            if(getAuction() == null){
                return false;
            }
            return getAuction().getEndTime().isBefore(LocalDateTime.now());
        }
        // throw new UnsupportedOperationException("Unimplemented method 'auctionOver'");
    }

}
