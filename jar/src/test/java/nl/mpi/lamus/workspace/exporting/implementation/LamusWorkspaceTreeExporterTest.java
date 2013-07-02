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
package nl.mpi.lamus.workspace.exporting.implementation;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import nl.mpi.lamus.dao.WorkspaceDao;
import nl.mpi.lamus.workspace.exporting.NodeExporter;
import nl.mpi.lamus.workspace.exporting.NodeExporterFactory;
import nl.mpi.lamus.workspace.exporting.WorkspaceTreeExporter;
import nl.mpi.lamus.workspace.model.Workspace;
import nl.mpi.lamus.workspace.model.WorkspaceNode;
import nl.mpi.lamus.workspace.model.WorkspaceNodeStatus;
import nl.mpi.lamus.workspace.model.WorkspaceNodeType;
import nl.mpi.lamus.workspace.model.implementation.LamusWorkspaceNode;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;

/**
 *
 * @author guisil
 */
public class LamusWorkspaceTreeExporterTest {
    
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    
    private WorkspaceTreeExporter workspaceTreeExporter;
    
    @Mock WorkspaceDao mockWorkspaceDao;
    @Mock NodeExporterFactory mockNodeExporterFactory;
    
    @Mock NodeExporter mockNodeExporter;
    @Mock Workspace mockWorkspace;
    
    public LamusWorkspaceTreeExporterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        workspaceTreeExporter = new LamusWorkspaceTreeExporter(mockWorkspaceDao, mockNodeExporterFactory);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of explore method, of class LamusWorkspaceTreeExporter.
     */
    @Test
    public void testExplore() throws MalformedURLException, URISyntaxException {
        
        final int workspaceID = 1;
        final int workspaceNodeID = 10;
        final int archiveNodeID = 100;
        final URL nodeURL = new URL("http://some.url/someName.cmdi");
        final String nodeName = "someName";
        final WorkspaceNodeType nodeType = WorkspaceNodeType.METADATA; //TODO change this
        final String nodeFormat = "";
        final URI schemaLocation = new URI("http://some.location");
        final String nodePid = "0000-0001";
        final WorkspaceNode node = new LamusWorkspaceNode(workspaceNodeID, workspaceID, archiveNodeID, schemaLocation,
                nodeName, "", nodeType, nodeURL, nodeURL, nodeURL, WorkspaceNodeStatus.NODE_ISCOPY, nodePid, nodeFormat);
        
        final int childWorkspaceNodeID = 20;
        final int childArchiveNodeID = -1;
        final URL childWorkspaceURL = new URL("file://workspace/folder/someOtherName.pdf");
        final URL childOriginURL = new URL("file://some/different/local/folder/someOtherName.pdf");
        final String childNodeName = "someOtherName";
        final WorkspaceNodeType childNodeType = WorkspaceNodeType.RESOURCE_WR;
        final String childNodeFormat = "";
        final String childNodePid = "0000-0002";
        final WorkspaceNode childNode = new LamusWorkspaceNode(childWorkspaceNodeID, workspaceID, childArchiveNodeID, schemaLocation,
                childNodeName, "", childNodeType, childWorkspaceURL, null, childOriginURL, WorkspaceNodeStatus.NODE_UPLOADED, childNodePid, childNodeFormat);
        
        final Collection<WorkspaceNode> children = new ArrayList<WorkspaceNode>();
        children.add(childNode);
        
        context.checking(new Expectations() {{
            
            oneOf(mockWorkspaceDao).getChildWorkspaceNodes(node.getWorkspaceNodeID()); will(returnValue(children));
            
            //TODO FOR EACH CHILD NODE, GET THE PROPER EXPORTER AND CALL IT
            oneOf(mockNodeExporterFactory).getNodeExporterForNode(mockWorkspace, childNode); will(returnValue(mockNodeExporter));
            oneOf(mockNodeExporter).exportNode(node, childNode);
            
        }});
        
        workspaceTreeExporter.explore(mockWorkspace, node);
    }
}