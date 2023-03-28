package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.logging.Logger;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.Cart;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Products
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Jacob Karvelis jak9527
 */
@Component
public class CartFileDAO implements CartDAO {
    private static final Logger LOG = Logger.getLogger(ProductFileDAO.class.getName());
    Map<Integer, Cart> carts;// Provides a local cache of cart objects
                                            // So that we don't need to read from the file
                                            // each time
    Map<Integer,Product> products;   // Provides a local cache of the product objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Product
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new product
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Cart File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public CartFileDAO(@Value("${carts.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the products from the file
    }

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
     * Generates an array of {@linkplain Product product} from the tree map
     * 
     * @return  The array of {@link Product product}, may be empty
     */
    private Product[] getProductsArray(int cId) {
        ArrayList<Product> productArrayList = new ArrayList<>();

        for (Product product : carts.get(cId).getProducts().values()) {
                productArrayList.add(product);
        }

        Product[] productArray = new Product[productArrayList.size()];
        productArrayList.toArray(productArray);
        return productArray;
    }

    /**
     * Generates an array of {@linkplain Cart carts} from the tree map
     * 
     * @return  The array of {@link Cart carts}, may be empty
     */
    private Cart[] getCartsArray(){
        ArrayList<Cart> cartArrayList = new ArrayList<>();

        for (Cart cart : carts.values()) {
            cartArrayList.add(cart); 
        }

        Cart[] cartArray = new Cart[cartArrayList.size()];
        cartArrayList.toArray(cartArray);
        return cartArray;
    }

    /**
     * Saves the {@linkplain Cart cart} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Cart cart} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Cart[] cartArray = getCartsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),cartArray);
        return true;
    }

    /**
     * Loads {@linkplain Cart cart} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        carts = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of products
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Cart[] cartArray = objectMapper.readValue(new File(filename),Cart[].class);
        
        // Add each product to the tree map and keep track of the greatest id
        for (Cart cart : cartArray) {
            carts.put(cart.getId(), cart);
            if (cart.getId() > nextId)
                nextId = cart.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product[] getCartProducts(int cId) {
        synchronized(carts) {
            return getProductsArray(cId);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product getProduct(int cId, int pId) {
        synchronized(carts) {
            if (carts.containsKey(cId))
                if(carts.get(cId).getProducts().containsKey(pId))
                    return carts.get(cId).getProducts().get(pId);
                else
                    return null;
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Cart getCart(int cId) {
        synchronized(carts) {
            if (carts.containsKey(cId))
                return carts.get(cId);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product addProduct(int cId, Product item) throws IOException {
        synchronized(carts) {
            if(carts.containsKey(cId)==false){
                return null; //cart does not exist
            }
            carts.get(cId).getProducts().put(item.getId(), item);
            //add the new item to the given cart
            save(); // may throw an IOException
            return item;
        }
    }
    
    /**
    ** {@inheritDoc}
     */
    @Override
    public Cart addCart(Cart cart) throws IOException{
        synchronized(carts){
            // We create a new cart object because the id field is immutable
            // and we need to assign the next unique id
            Cart newCart = new Cart(nextId(), cart.getProducts());
            carts.put(newCart.getId(), newCart);
            save(); // may throw an IOException
            return newCart;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product updateProductCount(int cId, int pId, int count) throws IOException {
        synchronized(carts) {
            if (carts.containsKey(cId) == false) {
                return null;  // cart does not exist
            }
            if (carts.get(cId).getProducts().containsKey(pId) == false){
                return null; //product not in cart
            }
            
            Product currentProduct = carts.get(cId).getProducts().get(pId);
            currentProduct.setQuantity(currentProduct.getQuantity()+count);
            // Change the quantity of the product in cart by count amount.
            // Its ugly but I don't know how to make it prettier
            save(); // may throw an IOException
            return carts.get(cId).getProducts().get(pId);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean removeProduct(int cId, int pId) throws IOException {
        synchronized(carts) {
            if (carts.containsKey(cId)) {
                if(carts.get(cId).getProducts().containsKey(pId)){
                    carts.get(cId).getProducts().remove(pId);
                    return save();
                }
                return false;
            }
            else
                return false;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean removeAllProducts(int cId){
        synchronized(carts){
            if(carts.containsKey(cId)){
                carts.get(cId).getProducts().clear();
                if(carts.get(cId).getProducts().size() == 0){
                    return true;
                } else {
                    return false;
                }
            } else{
                return false;
            }
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean updateCart(int cId, Product[] products) throws IOException {
        synchronized(carts){
            Cart currentCart = carts.get(cId);
            HashMap<Integer, Product> productsInCart = currentCart.getProducts();
            //match for all products in the store that are in the cart
            for(Product prod : products){
                //if this cart has a product with the id of the current product
                if(currentCart.getProducts().containsKey(prod.getId())){
                    Product currentProduct = currentCart.getProducts().get(prod.getId());
                    //if the quantity in cart is greater than stock, clamp it
                    if(currentProduct.getQuantity() > prod.getQuantity()){
                        if(prod.getQuantity()==0){
                            //if there are none in stock, remove it from the cart
                            removeProduct(cId, prod.getId());
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
            List<Product> prodList = Arrays.asList(products);
            int[] idsToRemove = new int[productsInCart.size()];
            int i = 0;
            for(Product prod : productsInCart.values()){
                //if it is, add its id to out list of ids to remove
                if(!prodList.contains(prod)){
                    idsToRemove[i] = prod.getId();
                    ++i;
                }
            }
            //remove them
            for(int j : idsToRemove){
                removeProduct(cId, j);
            }
        }
        return true; //stubbed
    }

    /**
     * MIGHT NOT NEED THIS, IN FACT PROBBY WONT
     * Helper for update cart, will take an array of {@linkplain Product products} as well as a cart id}
     * And update all products in the product array that are in the cart, removing any from the cart that
     * are not in the product array
     * 
     * @param cId The id of the cart to update
     * @param products The array of products to update from
     * 
     * @return true if the update was successful
     * <br>
     * false if it was not
     * 
     * @throws IOException if the underlying storage could not be accessed
     */
    // public boolean updateCartHelper(int cId, Product[] products){
    //     return true; //stubbed
    // }
}
