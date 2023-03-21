package com.estore.api.estoreapi.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.estore.api.estoreapi.model.GroceryItem;

public interface GroceryRepository extends MongoRepository<GroceryItem, String> {
    
    @Query("{name:*?0*}")
    public GroceryItem[] findByName(String name);
}
