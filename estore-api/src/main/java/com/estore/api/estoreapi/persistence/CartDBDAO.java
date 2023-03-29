package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Cart;
import com.estore.api.estoreapi.model.Product;

public class CartDBDAO implements CartDAO {

    @Override
    public Product[] getCartProducts(int cId) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCartProducts'");
    }

    @Override
    public Product getProduct(int cId, int pId) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProduct'");
    }

    @Override
    public Cart getCart(int cId) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCart'");
    }

    @Override
    public Product addProduct(int cId, Product product) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addProduct'");
    }

    @Override
    public Cart addCart(Cart cart) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addCart'");
    }

    @Override
    public Product updateProductCount(int cId, int pId, int count) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProductCount'");
    }

    @Override
    public boolean removeProduct(int cId, int pId) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeProduct'");
    }

    @Override
    public boolean removeAllProducts(int cId) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeAllProducts'");
    }

    @Override
    public boolean updateCart(int cId, Product[] products) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCart'");
    }
    
}
