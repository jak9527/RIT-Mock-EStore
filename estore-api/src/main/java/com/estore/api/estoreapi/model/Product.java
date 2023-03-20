package com.estore.api.estoreapi.model;

import java.util.logging.Logger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Product entity
 * 
 * @author SWEN Faculty
 */
@Document("products")
public class Product {
    private static final Logger LOG = Logger.getLogger(Product.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Item [id=%d, name=%s]";

    @Id
    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("price") private float price;
    @JsonProperty("quantity") private int quantity;

    /**
     * Create a product with the given id and name
     * @param id The id of the product
     * @param name The name of the product
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Product(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("price") float price, @JsonProperty("quantity") int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * Retrieves the id of the product
     * @return The id of the product
     */
    public int getId() {return id;}

    /**
     * Sets the name of the product - necessary for JSON object to Java object deserialization
     * @param name The name of the product
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the product
     * @return The name of the product
     */
    public String getName() {return name;}

    /**
     * Retrieves the price of the product
     * @return price of a product
     */
    public float getPrice() {return price;}

    /**
     * Retrieves the quantity of the product
     * @return The quantity of the product
     */
    public int getQuantity() {return quantity;}

    /**
     * Sets the quantity of the product
     * @return void
     */
    public void setQuantity(int quantity) {this.quantity = quantity;}

    /**
     * Sets the price of the product
     * @return void
     */
    public void setPrice(float price) {this.price = price;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name);
    }
}