package com.estore.api.estoreapi.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.estore.api.estoreapi.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{
    
    @Query("{name: { $regex: /(.*)?0(.*)/ }}")
    public Product[] findByName(String name);

    @Query("{_id: ?0}")
    public Product findById(int id);

    @Query(value="{'_id' : ?0}", delete = true)
    public void delete(int id);
}
