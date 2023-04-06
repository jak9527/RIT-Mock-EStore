package com.estore.api.estoreapi.persistence;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.estore.api.estoreapi.model.CurrentUser;

public interface CurrentUserRepository extends MongoRepository<CurrentUser, String> {
    public List<CurrentUser> findByOrderByIdDesc();
    
    @Query("{username: ?0}")
    public CurrentUser findByUsername(String username);

    @Query(value="{'_id' : ?0}", delete = true)
    public void delete(int id);
}
