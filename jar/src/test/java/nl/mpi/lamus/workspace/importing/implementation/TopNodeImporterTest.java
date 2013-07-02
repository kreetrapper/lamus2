/*
 * Copyright (C) 2013 Max Planck Institute for Psycholinguistics
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
package nl.mpi.lamus.workspace.importing.implementation;

import nl.mpi.corpusstructure.ArchiveObjectsDB;
import nl.mpi.lamus.dao.WorkspaceDao;
import nl.mpi.lamus.filesystem.WorkspaceFileHandler;
import nl.mpi.lamus.workspace.factory.WorkspaceNodeFactory;
import nl.mpi.lamus.workspace.factory.WorkspaceNodeLinkFactory;
import nl.mpi.lamus.workspace.factory.WorkspaceParentNodeReferenceFactory;
import nl.mpi.lamus.workspace.importing.NodeDataRetriever;
import nl.mpi.lamus.workspace.importing.WorkspaceFileImporter;
import nl.mpi.lamus.workspace.importing.WorkspaceNodeExplorer;
import nl.mpi.lamus.workspace.importing.WorkspaceNodeLinker;
import nl.mpi.lamus.workspace.model.Workspace;
import nl.mpi.metadata.api.MetadataAPI;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author Guilherme Silva <guilherme.silva@mpi.nl>
 */
public class TopNodeImporterTest {
    
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    private TopNodeImporter topNodeImporter;
    
    @Mock MetadataNodeImporter mockMetadataNodeImporter;
    
    @Mock ArchiveObjectsDB mockAoDB;
    @Mock WorkspaceDao mockWsDao;
    @Mock MetadataAPI metadataAPI;
    @Mock NodeDataRetriever mockNodeDataRetriever;
    @Mock WorkspaceNodeLinker mockNodeLinker;
    @Mock WorkspaceFileImporter mockFileImporter;
    @Mock WorkspaceNodeFactory mockNodeFactory;
    @Mock WorkspaceParentNodeReferenceFactory mockParentNodeReferenceFactory;
    @Mock WorkspaceNodeLinkFactory mockWsNodeLinkFactory;
    @Mock WorkspaceFileHandler mockFileHandler;
    @Mock WorkspaceNodeExplorer mockWorkspaceNodeExplorer;
    
    @Mock Workspace mockWorkspace;
    
    
    public TopNodeImporterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        topNodeImporter = new TopNodeImporter(mockAoDB, mockWsDao, metadataAPI, mockNodeDataRetriever,
                mockNodeLinker, mockFileImporter, mockNodeFactory, mockParentNodeReferenceFactory,
                mockWsNodeLinkFactory, mockFileHandler, mockWorkspaceNodeExplorer);
        
        ReflectionTestUtils.setField(topNodeImporter, "metadataNodeImporter", mockMetadataNodeImporter);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of importNode method, of class TopNodeImporter.
     */
    @Test
    public void testImportNode() throws Exception {
        
        final int workspaceID = 1;
        final int nodeArchiveID = 10;
        
        context.checking(new Expectations() {{
            oneOf(mockMetadataNodeImporter).importNode(workspaceID, null, null, null, nodeArchiveID);
        }});
        
        topNodeImporter.importNode(workspaceID, nodeArchiveID);
    }

    /**
     * Test of setWorkspace method, of class TopNodeImporter.
     */
//    @Test
//    public void testSetWorkspace() {
//        
//        context.checking(new Expectations() {{
//            oneOf(mockMetadataNodeImporter).setWorkspace(mockWorkspace);
//        }});
//        
//        topNodeImporter.setWorkspace(mockWorkspace);
//    }
}