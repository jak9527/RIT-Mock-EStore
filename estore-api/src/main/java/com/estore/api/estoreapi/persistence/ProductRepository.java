package com.estore.api.estoreapi.persistence;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.estore.api.estoreapi.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{
    @Query("{name: '?0'}")
    Product findProductByName(String name);

    @Query(value="{id:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
    List<Product> findAll(int id);

    public long count();
}
