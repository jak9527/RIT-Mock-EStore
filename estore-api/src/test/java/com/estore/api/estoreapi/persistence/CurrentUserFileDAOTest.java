package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.CurrentUserFileDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the User File DAO class
 * 
 * @author SWEN Faculty
 */
@Tag("Persistence-tier")
public class CurrentUserFileDAOTest {
    CurrentUserFileDAO currentUserFileDAO;
    User[] testUser;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupUserFileDAO() throws IOException  {
        mockObjectMapper = mock(ObjectMapper.class);
        testUser = new User[1];
        testUser[0] = new User(99,"user0");

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the product array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),User[].class))
                .thenReturn(testUser);
        currentUserFileDAO = new CurrentUserFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public synchronized void testGetUser() throws IOException{
        //setup
        currentUserFileDAO.setCurrentUser(testUser[0]);

        // Invoke
        User user = currentUserFileDAO.getCurrentUser();

        // Analzye
        assertEquals(user,testUser[0]);
    }

    @Test
    public synchronized void testDeleteUser() throws IOException{
        //setup
        currentUserFileDAO.setCurrentUser(testUser[0]);

        // Invoke
        User user = currentUserFileDAO.deleteUser(testUser[0]);

        // Analzye
        assertEquals(user,testUser[0]);
    }

    @Test
    public synchronized void testDeleteUserNotFound() throws IOException{
        //setup
        currentUserFileDAO.setCurrentUser(testUser[0]);

        // Invoke
        User user = currentUserFileDAO.deleteUser(new User(123, "Boop Bop"));

        // Analzye
        assertEquals(user,null);
    }

    @Test
    public void testSetUser() {
        // Setup
        User user = new User(102,"Wonder-Person");
        assertDoesNotThrow(() -> currentUserFileDAO.deleteUser(currentUserFileDAO.getCurrentUser()),
                                "Unexpected exception thrown");

        // Invoke
        User result = assertDoesNotThrow(() -> currentUserFileDAO.setCurrentUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = currentUserFileDAO.getCurrentUser();
        assertEquals(actual.getId(),user.getId());
        assertEquals(actual.getUsername(),user.getUsername());
    }

    @Test
    public void testReSetUser() {
        // Setup
        User user = new User(102,"BreadMan");

        // Invoke
        assertDoesNotThrow(() -> currentUserFileDAO.setCurrentUser(new User(102,"WonderMan")),
                                "Unexpected exception thrown");
        User result = assertDoesNotThrow(() -> currentUserFileDAO.setCurrentUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = currentUserFileDAO.getCurrentUser();
        assertEquals(actual.getId(),user.getId());
        assertEquals(actual.getUsername(),user.getUsername());
    }



    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(User[].class));

        User user = new User(102,"Fungi");

        assertThrows(IOException.class,
                        () -> currentUserFileDAO.setCurrentUser(user),
                        "IOException not thrown");
    }

    @Test
    public void testGetUserNotFound() {
        // Invoke
        User user = currentUserFileDAO.getCurrentUser();

        // Analyze
        assertEquals(user,null);
    }


    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the ProductFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),User[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new CurrentUserFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}