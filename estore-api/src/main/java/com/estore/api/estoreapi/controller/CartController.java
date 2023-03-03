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

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.ProductDAO;
import com.estore.api.estoreapi.model.Cart;
import com.estore.api.estoreapi.persistence.CartDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the Product resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author SWEN Faculty
 */

@RestController
@RequestMapping("carts")
public class CartController {
    private static final Logger LOG = Logger.getLogger(EstoreController.class.getName());
    private CartDAO cartDao;
    private ProductDAO productDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param productDao The {@link ProductDAO Product Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public CartController(ProductDAO productDao, CartDAO cartDao) {
        this.productDao = productDao;
        this.cartDao= cartDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Product product} for the given id
     * 
     * @param id The id used to locate the {@link Product product}
     * 
     * @return ResponseEntity with {@link Product product} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable int id) {
        LOG.info("GET /carts/" + id);
        try {
            Cart cart = cartDao.getCart(id);
            if (cart != null)
                return new ResponseEntity<Cart>(cart, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Creates a {@linkplain Product product} with the provided item object
     * 
     * @param product - The {@link Product product} to create
     * 
     * @return ResponseEntity with created {@link Product item} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Product item} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        LOG.info("POST /cart " + cart);
        try {
            Cart newCart = cartDao.addCart(cart);
            if( newCart == null ) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<Cart>(newCart,
                         HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Product product} with the provided {@linkplain Product product} object, if it exists
     * 
     * @param product The {@link Product product} to update
     * 
     * @return ResponseEntity with updated {@link Product product} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{cId}")
    public ResponseEntity<Product> addProductToCart(@RequestBody Product product, @PathVariable int cId) {
        LOG.info("PUT /cart " + product);
        try {
            Product addProduct = cartDao.addProduct(cId, product);
            if( addProduct == null) {
                return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Product>(addProduct, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Product product} with the provided {@linkplain Product product} object, if it exists
     * 
     * @param product The {@link Product product} to update
     * 
     * @return ResponseEntity with updated {@link Product product} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{cId}/{pId}/{count}")
    public ResponseEntity<Product> updateProductCount(@PathVariable int cId, @PathVariable int pId, @PathVariable int count) {
        LOG.info("PUT /cart/product/count " + count);
        try {
            Product updateProduct = cartDao.updateProductCount(cId, pId, count);
            if( updateProduct == null) {
                return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
            }
            if(updateProduct.getQuantity()==0){
                removeProduct(cId, pId);
            }
            return new ResponseEntity<Product>(updateProduct, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Product product} with the given id
     * 
     * @param id The id of the {@link Product product} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{cId}/{pId}")
    public ResponseEntity<Product> removeProduct(@PathVariable int cId, @PathVariable int pId) {
        LOG.info("DELETE /cart/" + pId);

        try {
            boolean delete = cartDao.removeProduct(cId, pId);
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
