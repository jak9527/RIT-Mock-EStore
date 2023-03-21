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

import com.estore.api.estoreapi.controller.UserController;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.UserDAO;


/**
 * Test the User Controller class
 * 
 * @author Ethan Meyers epm2875
 */
public class UserControllerTest{
    private UserController userController;
    private UserDAO mockUserDAO;

        /**
     * Before each test, create a new ProductController object and inject
     * a mock USer DAO
     */
    @BeforeEach
    public void setupUserController() {
        mockUserDAO = mock(UserDAO.class);
        userController = new UserController(mockUserDAO);

    }

    @Test
    public void testGetUser() throws IOException{
        User user = new User(9, "User22");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        ResponseEntity<User> response = userController.getUser(user.getUsername());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testGetUserNotFound() throws IOException{
        String username = "user404";
        when(mockUserDAO.getUser(username)).thenReturn(null); 
        ResponseEntity<User> response = userController.getUser(username);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetUserHandleException() throws Exception {
        String username = "user302";
        doThrow(new IOException()).when(mockUserDAO).getUser(username);
        ResponseEntity<User> response = userController.getUser(username);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void TestCreateUser() throws IOException {
        User user = new User(9, "User22");
        when(mockUserDAO.createUser(user)).thenReturn(user);
        ResponseEntity<User> response = userController.createUser(user);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testCreateUserFailed() throws IOException{
        User user = new User(9, "User22");
        when(mockUserDAO.createUser(user)).thenReturn(null);
        ResponseEntity<User> response = userController.createUser(user);
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateUserHandleException() throws IOException{
        User user = new User(9, "User22");
        doThrow(new IOException()).when(mockUserDAO).createUser(user);
        ResponseEntity<User> response = userController.createUser(user);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}