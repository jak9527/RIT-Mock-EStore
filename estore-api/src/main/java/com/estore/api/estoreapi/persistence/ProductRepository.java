package com.estore.api.estoreapi.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.estore.api.estoreapi.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{
    
    @Query("{name:*?0*}")
    public Product[] findByName(String name);
}
