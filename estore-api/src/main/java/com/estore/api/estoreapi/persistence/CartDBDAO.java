package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Cart;
import com.estore.api.estoreapi.model.Product;

/**
 * Implements the functionality for Database persistence for Shopping Carts
 * through MongoDB.
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Andrew Bush (apb2471@rit.edu)
 */

@Component
public class CartDBDAO implements CartDAO{

    @Autowired
    CartRepository cartRepo;

    @Autowired
    ProductRepository productRepo;

    private static int nextId = 0;  // The next Id to assign to a new cart

    /**
     * Generates the next id for a new {@Cart Cart cart}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product[] getCartProducts(int cId) throws IOException {
        Cart c = getCart(cId);
        if(c == null) {
            return null;
        }
        HashMap<Integer, Product> productsHM =  c.getProducts();
        Product[] pList = new Product[productsHM.size()];
        int i = 0;
        for (Product p : productsHM.values()) {
            pList[i] = p;
            i++;
        }

        return pList;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product getProduct(int cId, int pId) throws IOException {
        Product[] pList = getCartProducts(cId);
        for (Product p : pList) {
            if (p.getId() == pId) {
                return p;
            }
        }
        return null;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Cart getCart(int cId) throws IOException {
        return cartRepo.findById(cId);
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product addProduct(int cId, Product product) throws IOException {
        Cart c = getCart(cId);
        if (c == null) {
            return null;
        }
        cartRepo.delete(cId);
        c.addProduct(product);
        cartRepo.insert(c);
        return product;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Cart addCart(Cart cart) throws IOException {
        cart.setId(nextId());
        cartRepo.insert(cart);
        return cart;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product updateProductCount(int cId, int pId, int count) throws IOException {
        Product p = productRepo.findById(pId);
        if (p == null) {
            return null;
        }
        p.setQuantity(p.getQuantity() + 1);
        Cart c = getCart(cId);
        c.getProducts().replace(pId, p);
        cartRepo.delete(cId);
        cartRepo.insert(c);
        return p;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean removeProduct(int cId, int pId) throws IOException {
        Cart c = getCart(cId);
        HashMap<Integer, Product> productsHM =  c.getProducts();
        if(productsHM.remove(pId) == null) {
            return false;
        }
        cartRepo.delete(cId);
        cartRepo.insert(c);
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean removeAllProducts(int cId) throws IOException {
        Cart c = getCart(cId);
        if (c == null) {
            return false;
        }
        HashMap<Integer, Product> productsHM =  c.getProducts();
        productsHM.clear();
        cartRepo.delete(cId);
        cartRepo.insert(c);
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean updateCart(int cId, Product[] products) throws IOException {
        Cart c = getCart(cId);
        if (c == null) {
            return false;
        }
        HashMap<Integer, Product> productsHM =  c.getProducts();

        //match for all products in the store that are in the cart
        for(Product prod : products) {
            int pId = prod.getId();
            //if this cart has a product with the id of the current product
            if (productsHM.containsKey(pId)) {
                Product currentProduct = productsHM.get(pId);
                //if the quantity in cart is greater than stock, clamp it
                if(currentProduct.getQuantity() > prod.getQuantity()) {
                    if(prod.getQuantity() == 0) {
                        //if there are none in stock, remove it from the cart
                        removeProduct(cId, pId);
                    } else{
                        currentProduct.setQuantity(prod.getQuantity());
                    }
                }
                //reset the price
                currentProduct.setPrice(prod.getPrice());
                //reset the name
                currentProduct.setName(prod.getName());
            }
        }
        //find products in the cart that are not in the store anymore
        
        HashSet<Integer> idsInStore = new HashSet<>();
        HashSet<Integer> idsToRemoveSet = new HashSet<>();
        for(int i = 0; i < products.length; i++){
            idsInStore.add(products[i].getId());
        }
        
        for(Product prod : productsHM.values()){
            if(!idsInStore.contains(prod.getId())){
                idsToRemoveSet.add(prod.getId());
            }
        }

        for(Integer id : idsToRemoveSet){
            removeProduct(cId, id);
        }
        

        return true;
    }
    
}
