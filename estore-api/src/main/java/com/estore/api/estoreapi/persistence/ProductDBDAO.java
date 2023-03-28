package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import java.util.UUID;

import com.estore.api.estoreapi.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for Database persistence for Products through MongoDB
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty
 * @author Andrew Bush (apb2471@rit.edu)
 */

@Component
public class ProductDBDAO implements ProductDAO {
    
    @Autowired
    ProductRepository productRepo;

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product[] getProducts() {
        return productRepo.findAll().toArray(new Product[0]);
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product[] findProducts(String containsText) {
        return productRepo.findByName(containsText);
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product getProduct(int id) {
        return productRepo.findById(id);
    }

    /**
    ** {@inheritDoc}
    */
    @Override
    public Product createProduct(Product item) throws IOException {
        item.setId(Math.abs(UUID.randomUUID().hashCode()));
        return productRepo.insert(item);
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product updateProduct(Product product) throws IOException {
        if(deleteProduct(product.getId())) {
            productRepo.insert(product);
            return product;
        } else {
            return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteProduct(int id) throws IOException {
        if (getProduct(id) != null) {
            productRepo.delete(id);
            return true;
        } else {
            return false;
        }
    }
}
