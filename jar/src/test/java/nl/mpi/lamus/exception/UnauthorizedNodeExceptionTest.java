/*
 * Copyright (C) 2013 Max Planck Institute for Psycholinguistics
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mpi.lamus.exception;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author guisil
 */
public class UnauthorizedNodeExceptionTest {
    
    public UnauthorizedNodeExceptionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void constructorWithTwoArguments() throws URISyntaxException {
        
        URI expectedURI = new URI(UUID.randomUUID().toString());
        String expectedUserID = "testUser@mpi";
        String expectedMessage = "Node with URI '" + expectedURI.toString() + "' is not writeable by user " + expectedUserID;
        
        UnauthorizedNodeException exception = new UnauthorizedNodeException(expectedURI, expectedUserID);
        
        assertNull("Cause should be null", exception.getCause());
        assertEquals("URI different from expected", expectedURI, exception.getNodeURI());
        assertEquals("User ID different from expected", expectedUserID, exception.getUserID());
        assertEquals("Message different from expected", expectedMessage, exception.getMessage());
    }
    
    @Test
    public void constructorWithThreeArguments() throws URISyntaxException {
        
        URI expectedURI = new URI(UUID.randomUUID().toString());
        String expectedUserID = "testUser@mpi";
        String expectedMessage = "Node with URI '" + expectedURI.toString() + "' is is not writeable by user " + expectedUserID;
        
        UnauthorizedNodeException exception = new UnauthorizedNodeException(expectedMessage, expectedURI, expectedUserID);
        
        assertNull("Cause should be null", exception.getCause());
        assertEquals("URI different from expected", expectedURI, exception.getNodeURI());
        assertEquals("User ID different from expected", expectedUserID, exception.getUserID());
        assertEquals("Message different from expected", expectedMessage, exception.getMessage());
    }
}