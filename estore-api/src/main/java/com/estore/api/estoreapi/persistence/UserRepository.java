package com.estore.api.estoreapi.persistence;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.estore.api.estoreapi.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    public List<User> findByOrderByIdDesc();

    @Query("{username: ?0}")
    public User findByUsername(String username);

    @Query(value="{'_id' : ?0}", delete = true)
    public void delete(int id);
}
