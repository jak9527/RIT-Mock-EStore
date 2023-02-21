package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.controller.EstoreController;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.ProductDAO;

/**
 * Test the Hero Controller class
 * 
 * @author SWEN Faculty
 */
@Tag("Controller-tier")
public class EstoreControllerTest {
    private EstoreController heroController;
    private ProductDAO mockHeroDAO;

    /**
     * Before each test, create a new HeroController object and inject
     * a mock Hero DAO
     */
    @BeforeEach
    public void setupHeroController() {
        mockHeroDAO = mock(ProductDAO.class);
        heroController = new EstoreController(mockHeroDAO);
    }

    @Test
    public void testGetHero() throws IOException {  // getHero may throw IOException
        // Setup
        Product hero = new Product(99,"Galactic Agent");
        // When the same id is passed in, our mock Hero DAO will return the Hero object
        when(mockHeroDAO.getProduct(hero.getId())).thenReturn(hero);

        // Invoke
        ResponseEntity<Product> response = heroController.getProduct(hero.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(hero,response.getBody());
    }

    @Test
    public void testGetHeroNotFound() throws Exception { // createHero may throw IOException
        // Setup
        int heroId = 99;
        // When the same id is passed in, our mock Hero DAO will return null, simulating
        // no hero found
        when(mockHeroDAO.getProduct(heroId)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = heroController.getProduct(heroId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetHeroHandleException() throws Exception { // createHero may throw IOException
        // Setup
        int heroId = 99;
        // When getHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockHeroDAO).getProduct(heroId);

        // Invoke
        ResponseEntity<Product> response = heroController.getProduct(heroId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all HeroController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateHero() throws IOException {  // createHero may throw IOException
        // Setup
        Product hero = new Product(99,"Wi-Fire");
        // when createHero is called, return true simulating successful
        // creation and save
        when(mockHeroDAO.createHero(hero)).thenReturn(hero);

        // Invoke
        ResponseEntity<Product> response = heroController.createHero(hero);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(hero,response.getBody());
    }

    @Test
    public void testCreateHeroFailed() throws IOException {  // createHero may throw IOException
        // Setup
        Product hero = new Product(99,"Bolt");
        // when createHero is called, return false simulating failed
        // creation and save
        when(mockHeroDAO.createHero(hero)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = heroController.createHero(hero);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateHeroHandleException() throws IOException {  // createHero may throw IOException
        // Setup
        Product hero = new Product(99,"Ice Gladiator");

        // When createHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockHeroDAO).createHero(hero);

        // Invoke
        ResponseEntity<Product> response = heroController.createHero(hero);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateHero() throws IOException { // updateHero may throw IOException
        // Setup
        Product hero = new Product(99,"Wi-Fire");
        // when updateHero is called, return true simulating successful
        // update and save
        when(mockHeroDAO.updateProduct(hero)).thenReturn(hero);
        ResponseEntity<Product> response = heroController.updateProduct(hero);
        hero.setName("Bolt");

        // Invoke
        response = heroController.updateProduct(hero);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(hero,response.getBody());
    }

    @Test
    public void testUpdateHeroFailed() throws IOException { // updateHero may throw IOException
        // Setup
        Product hero = new Product(99,"Galactic Agent");
        // when updateHero is called, return true simulating successful
        // update and save
        when(mockHeroDAO.updateProduct(hero)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = heroController.updateProduct(hero);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateHeroHandleException() throws IOException { // updateHero may throw IOException
        // Setup
        Product hero = new Product(99,"Galactic Agent");
        // When updateHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockHeroDAO).updateProduct(hero);

        // Invoke
        ResponseEntity<Product> response = heroController.updateProduct(hero);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetHeroes() throws IOException { // getHeroes may throw IOException
        // Setup
        Product[] heroes = new Product[2];
        heroes[0] = new Product(99,"Bolt");
        heroes[1] = new Product(100,"The Great Iguana");
        // When getHeroes is called return the heroes created above
        when(mockHeroDAO.getProducts()).thenReturn(heroes);

        // Invoke
        ResponseEntity<Product[]> response = heroController.getProducts();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(heroes,response.getBody());
    }

    @Test
    public void testGetHeroesHandleException() throws IOException { // getHeroes may throw IOException
        // Setup
        // When getHeroes is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockHeroDAO).getProducts();

        // Invoke
        ResponseEntity<Product[]> response = heroController.getProducts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchHeroes() throws IOException { // findHeroes may throw IOException
        // Setup
        String searchString = "la";
        Product[] heroes = new Product[2];
        heroes[0] = new Product(99,"Galactic Agent");
        heroes[1] = new Product(100,"Ice Gladiator");
        // When findHeroes is called with the search string, return the two
        /// heroes above
        when(mockHeroDAO.findProducts(searchString)).thenReturn(heroes);

        // Invoke
        ResponseEntity<Product[]> response = heroController.searchProducts(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(heroes,response.getBody());
    }

    @Test
    public void testSearchHeroesHandleException() throws IOException { // findHeroes may throw IOException
        // Setup
        String searchString = "an";
        // When createHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockHeroDAO).findProducts(searchString);

        // Invoke
        ResponseEntity<Product[]> response = heroController.searchProducts(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteHero() throws IOException { // deleteHero may throw IOException
        // Setup
        int heroId = 99;
        // when deleteHero is called return true, simulating successful deletion
        when(mockHeroDAO.deleteProduct(heroId)).thenReturn(true);

        // Invoke
        ResponseEntity<Product> response = heroController.deleteProduct(heroId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteHeroNotFound() throws IOException { // deleteHero may throw IOException
        // Setup
        int heroId = 99;
        // when deleteHero is called return false, simulating failed deletion
        when(mockHeroDAO.deleteProduct(heroId)).thenReturn(false);

        // Invoke
        ResponseEntity<Product> response = heroController.deleteProduct(heroId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteHeroHandleException() throws IOException { // deleteHero may throw IOException
        // Setup
        int heroId = 99;
        // When deleteHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockHeroDAO).deleteProduct(heroId);

        // Invoke
        ResponseEntity<Product> response = heroController.deleteProduct(heroId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
