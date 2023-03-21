package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.GroceryItem;
import com.estore.api.estoreapi.persistence.GroceryDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the GroceryItem resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author SWEN Faculty
 */

@RestController
@RequestMapping("grocery")
public class GroceryController {
    private static final Logger LOG = Logger.getLogger(GroceryController.class.getName());
    private GroceryDAO groceryDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param groceryDao The {@link GroceryDAO GroceryItem Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public GroceryController(GroceryDAO GroceryDao) {
        this.groceryDao = GroceryDao;
    }

    /**
     * Responds to the GET request for a {@linkplain GroceryItem grocery} for the given id
     * 
     * @param id The id used to locate the {@link GroceryItem grocery}
     * 
     * @return ResponseEntity with {@link GroceryItem grocery} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<GroceryItem> getGrocery(@PathVariable int id) {
        LOG.info("GET /grocery/" + id);
        try {
            GroceryItem grocery = groceryDao.getGrocery(id);
            if (grocery != null)
                return new ResponseEntity<GroceryItem>(grocery, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain GroceryItem grocerys}
     * 
     * @return ResponseEntity with array of {@link GroceryItem grocery} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<GroceryItem[]> getGrocerys() {
        LOG.info("GET /grocery");
        try {
            GroceryItem[] grocerys = groceryDao.getGroceries();
            return new ResponseEntity<GroceryItem[]>(grocerys, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain GroceryItem grocerys} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link GroceryItem grocerys}
     * 
     * @return ResponseEntity with array of {@link GroceryItem grocery} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all grocerys that contain the text "ma"
     * GET http://localhost:8080/grocery/?name=ma
     */
    @GetMapping("/")
    public ResponseEntity<GroceryItem[]> searchGrocerys(@RequestParam String name) {
        LOG.info("GET /grocery/?name="+name);
        try {
            GroceryItem[] grocerys = groceryDao.findGroceries(name);
            return new ResponseEntity<GroceryItem[]>(grocerys, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Creates a {@linkplain GroceryItem grocery} with the provided item object
     * 
     * @param grocery - The {@link GroceryItem grocery} to create
     * 
     * @return ResponseEntity with created {@link GroceryItem item} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link GroceryItem item} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<GroceryItem> createGrocery(@RequestBody GroceryItem grocery) {
        LOG.info("POST /grocery " + grocery);
        try {
            GroceryItem newGrocery = groceryDao.createGrocery(grocery);
            if( newGrocery == null ) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<GroceryItem>(newGrocery,
                         HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain GroceryItem grocery} with the provided {@linkplain GroceryItem grocery} object, if it exists
     * 
     * @param grocery The {@link GroceryItem grocery} to update
     * 
     * @return ResponseEntity with updated {@link GroceryItem grocery} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<GroceryItem> updateGrocery(@RequestBody GroceryItem grocery) {
        LOG.info("PUT /grocery " + grocery);
        try {
            GroceryItem updateGrocery = groceryDao.updateGrocery(grocery);
            if( updateGrocery == null) {
                return new ResponseEntity<GroceryItem>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<GroceryItem>(updateGrocery, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain GroceryItem grocery} with the given id
     * 
     * @param id The id of the {@link GroceryItem grocery} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<GroceryItem> deleteGrocery(@PathVariable int id) {
        LOG.info("DELETE /grocery/" + id);

        try {
            boolean delete = groceryDao.deleteGrocery(id);
            if( !delete ) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
