package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.imageio.IIOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.controller.CurrentUserController;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.CurrentUserDAO;


/**
 * Test the Current User Controller class
 * 
 * @author Jacob Karvelis
 */
public class CurrentUserControllerTest{
    private CurrentUserController currentUserController;
    private CurrentUserDAO mockCurrentUserDAO;

        /**
     * Before each test, create a new ProductController object and inject
     * a mock Product DAO
     */
    @BeforeEach
    public void setupUserController() throws IOException {
        mockCurrentUserDAO = mock(CurrentUserDAO.class);
        currentUserController = new CurrentUserController(mockCurrentUserDAO);

    }

    @Test
    public void testGetCurrentUser() throws IOException{
        User user = new User(9, "User22");
        when(mockCurrentUserDAO.getCurrentUser()).thenReturn(user);
        ResponseEntity<User> response = currentUserController.getCurrentUser();
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testGetCurrentUserNotFound() throws IOException{
        String username = "user404";
        when(mockCurrentUserDAO.getCurrentUser()).thenReturn(null); 
        ResponseEntity<User> response = currentUserController.getCurrentUser();
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetCurrentUserHandleException() throws Exception {
        String username = "user302";
        doThrow(new IOException()).when(mockCurrentUserDAO).getCurrentUser();
        ResponseEntity<User> response = currentUserController.getCurrentUser();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void TestSetCurrentUser() throws IOException {
        User user = new User(9, "User22");
        when(mockCurrentUserDAO.setCurrentUser(user)).thenReturn(user);
        ResponseEntity<User> response = currentUserController.setCurrentUser(user);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testSetCurrentUserFailed() throws IOException{
        User user = new User(9, "User22");
        when(mockCurrentUserDAO.setCurrentUser(user)).thenReturn(null);
        ResponseEntity<User> response = currentUserController.setCurrentUser(user);
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testSetCurrentUserHandleException() throws IOException{
        User user = new User(9, "User22");
        doThrow(new IOException()).when(mockCurrentUserDAO).setCurrentUser(user);
        ResponseEntity<User> response = currentUserController.setCurrentUser(user);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void TestDeleteUser() throws IOException {
        User user = new User(9, "User22");
        when(mockCurrentUserDAO.deleteUser(user)).thenReturn(user);
        when(mockCurrentUserDAO.getCurrentUser()).thenReturn(user);
        ResponseEntity<User> response = currentUserController.deleteUser();
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException{
        User user = new User(9, "User22");
        when(mockCurrentUserDAO.deleteUser(user)).thenReturn(null);
        ResponseEntity<User> response = currentUserController.deleteUser();
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteUserHandleException() throws IOException{
        User user = new User(9, "User22");
        when(mockCurrentUserDAO.getCurrentUser()).thenReturn(user);
        doThrow(new IOException()).when(mockCurrentUserDAO).deleteUser(user);
        ResponseEntity<User> response = currentUserController.deleteUser();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}