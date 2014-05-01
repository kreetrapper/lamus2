/*
 * Copyright (C) 2014 Max Planck Institute for Psycholinguistics
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
package nl.mpi.lamus.workspace.replace.implementation;

import nl.mpi.lamus.workspace.model.WorkspaceNode;
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
public class LamusNodeReplaceExplorerTest {
    
    public LamusNodeReplaceExplorerTest() {
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

    /**
     * Test of exploreReplace method, of class LamusNodeReplaceExplorer.
     */
    @Test
    public void testExploreReplace() {
        System.out.println("exploreReplace");
        WorkspaceNode oldNode = null;
        WorkspaceNode newNode = null;
        LamusNodeReplaceExplorer instance = new LamusNodeReplaceExplorer();
        instance.exploreReplace(oldNode, newNode);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}