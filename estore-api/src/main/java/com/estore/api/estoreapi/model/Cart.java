package com.estore.api.estoreapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Cart entity
 * 
 * @author Jacob Karvelis jak9527
 */
@Document("carts")
public class Cart {

    // Package private for tests
    static final String STRING_FORMAT = "Cart [id=%d, products=%s]";

    @Id
    @JsonProperty("id") private int id;
    
    private int total;
    @JsonProperty("products") private HashMap<Integer, Product> products; //map product id to product

    /**
     * Create a Cart with the given id
     * @param id The id of the cart
     * t
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    // public Cart(@JsonProperty("id") int id) {
    //     this.id = id;
    //     this.products = new HashMap<>();
    // }

    public Cart(@JsonProperty("id") int id, @JsonProperty("products") HashMap<Integer, Product> products) {
        this.id = id;
        this.products = products;
        this.total = getTotal();
    }

    /**
     * Retrieves the id of the cart
     * @return The id of the cart
     */
    public int getId() {return id;}

    /**
     * Retrieves the map of products
     * @return the map of products
     */
    public HashMap<Integer, Product> getProducts(){
        return products;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,products);
    }

    public int getTotal() {
        total = 0;
        for( Product product : this.products.values()) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }
}