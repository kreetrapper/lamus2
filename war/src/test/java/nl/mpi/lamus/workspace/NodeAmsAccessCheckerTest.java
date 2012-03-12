/*
 * Copyright (C) 2012 Max Planck Institute for Psycholinguistics
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mpi.lamus.workspace;

import nl.mpi.corpusstructure.ArchiveObjectsDB;
import nl.mpi.corpusstructure.Node;
import nl.mpi.corpusstructure.NodeIdUtils;
import nl.mpi.lamus.ams.AmsBridge;
import nl.mpi.lamus.dao.WorkspaceDao;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import static org.junit.Assert.assertFalse;
import org.junit.*;

/**
 *
 * @author Guilherme Silva <guilherme.silva@mpi.nl>
 */
public class NodeAmsAccessCheckerTest {
    
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    private NodeAccessChecker nodeAccessChecker;
    @Mock private ArchiveObjectsDB mockArchiveObjectsDB;
    @Mock private AmsBridge mockAmsBridge;
    @Mock private WorkspaceDao mockWorkspaceDao;
    
    public NodeAmsAccessCheckerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        nodeAccessChecker = new NodeAmsAccessChecker(mockArchiveObjectsDB, mockAmsBridge, mockWorkspaceDao);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of canCreateWorkspace method, of class NodeAccessCheckerImpl.
     */
    @Test
    public void returnsFalseIfNodeIsExternal() {
        
        final String userID = "someUser";
        final int archiveNodeID = 10;
        final Node archiveNode = new Node(NodeIdUtils.TONODEID(archiveNodeID), 0, "cmdi", "someNode", "some node");
        
        context.checking(new Expectations() {{
            oneOf (mockArchiveObjectsDB).isOnSite(NodeIdUtils.TONODEID(archiveNodeID)); will(returnValue(false));
        }});
        
        boolean result = nodeAccessChecker.canCreateWorkspace(userID, archiveNodeID);
        assertFalse(result);
    }
    
    /**
     * Test of canCreateWorkspace method, of class NodeAccessCheckerImpl.
     */
    @Test
    public void returnsFalseIfNodeIsNotAccessibleToUser() {
        
        final String userID = "someUser";
        final int archiveNodeID = 10;
        
        context.checking(new Expectations() {{
            oneOf (mockArchiveObjectsDB).isOnSite(NodeIdUtils.TONODEID(archiveNodeID)); will(returnValue(true));
            oneOf (mockAmsBridge).hasWriteAccess(userID, archiveNodeID); will(returnValue(false));
        }});
        
        boolean result = nodeAccessChecker.canCreateWorkspace(userID, archiveNodeID);
        assertFalse(result);
    }
    
    /**
     * Test of canCreateWorkspace method, of class NodeAccessCheckerImpl.
     */
    @Test
    public void returnsFalseIfNodeIsLocked() {
        
        final String userID = "someUser";
        final int archiveNodeID = 10;
        
        context.checking(new Expectations() {{
            oneOf (mockArchiveObjectsDB).isOnSite(NodeIdUtils.TONODEID(archiveNodeID)); will(returnValue(true));
            oneOf (mockAmsBridge).hasWriteAccess(userID, archiveNodeID); will(returnValue(true));
            oneOf (mockWorkspaceDao).isNodeLocked(archiveNodeID); will(returnValue(true));
        }});
        
        boolean result = nodeAccessChecker.canCreateWorkspace(userID, archiveNodeID);
        assertFalse(result);
    }
}
