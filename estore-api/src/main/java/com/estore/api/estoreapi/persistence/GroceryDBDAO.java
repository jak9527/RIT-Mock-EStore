package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.GroceryItem;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public class GroceryDBDAO implements GroceryDAO {

    @Autowired
    GroceryRepository groceryRepo;


    @Override
    public GroceryItem[] getGroceries() throws IOException {
        return groceryRepo.findAll().toArray(new GroceryItem[0]);
    }

    @Override
    public GroceryItem[] findGroceries(String containsText) throws IOException {
        return groceryRepo.findByName(containsText);
    }

    @Override
    public GroceryItem getGrocery(int id) throws IOException {
        Optional<GroceryItem> opt = groceryRepo.findById(Integer.toString(id));
        if(opt.isPresent()) { // Check if there was a value returned
            return opt.get();
        } else {
            return null;
        }
    }

    @Override
    public GroceryItem createGrocery(GroceryItem grocery) throws IOException {
        return groceryRepo.insert(grocery);
    }

    @Override
    public boolean deleteGrocery(int id) throws IOException {
        if (groceryRepo.existsById(Integer.toString(id))) {
            groceryRepo.deleteById(Integer.toString(id));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public GroceryItem updateGrocery(GroceryItem grocery) throws IOException {
        if(deleteGrocery(grocery.getId())) {
            groceryRepo.insert(grocery);
            return grocery;
        } else {
            return null;
        }
    }
    
}
