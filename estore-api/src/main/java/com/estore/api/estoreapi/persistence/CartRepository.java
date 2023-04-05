package com.estore.api.estoreapi.persistence;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.estore.api.estoreapi.model.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {

    @Query("{_id: ?0}")
    public Cart findById(int cId);

    @Query(value="{'_id' : ?0}", delete = true)
    public void delete(int cId);

    public List<Cart> findByOrderByIdDesc();
}
