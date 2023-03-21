package com.estore.api.estoreapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("groceryitems")
public class GroceryItem {

    @Id
    private int id;

    private String name;
    private int quantity;
    private String category;
    
    public GroceryItem(int id, String name, int quantity, String category) {
        super();
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.category = category;
    }

    /**
     * Retrieves the id of the product
    * @return The id of the product
    */
    public int getId() {return id;}
}   