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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.lamus.dao.WorkspaceDao;
import nl.mpi.lamus.filesystem.WorkspaceFileHandler;
import nl.mpi.lamus.workspace.factory.WorkspaceNodeLinkFactory;
import nl.mpi.lamus.workspace.factory.WorkspaceParentNodeReferenceFactory;
import nl.mpi.lamus.workspace.importing.WorkspaceNodeLinkManager;
import nl.mpi.lamus.workspace.model.Workspace;
import nl.mpi.lamus.workspace.model.WorkspaceNode;
import nl.mpi.lamus.workspace.model.WorkspaceNodeLink;
import nl.mpi.lamus.workspace.model.WorkspaceNodeType;
import nl.mpi.lamus.workspace.model.WorkspaceParentNodeReference;
import nl.mpi.lamus.workspace.model.implementation.LamusWorkspaceNodeLink;
import nl.mpi.metadata.api.MetadataAPI;
import nl.mpi.metadata.api.MetadataException;
import nl.mpi.metadata.api.model.MetadataDocument;
import nl.mpi.metadata.api.model.MetadataReference;
import nl.mpi.metadata.api.model.Reference;
import nl.mpi.metadata.api.model.ReferencingMetadataDocument;
import nl.mpi.metadata.api.model.ResourceReference;
import nl.mpi.metadata.cmdi.api.model.MetadataResourceProxy;
import nl.mpi.metadata.cmdi.api.model.ResourceProxy;
import org.apache.commons.io.FileUtils;
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
import org.junit.runner.RunWith;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author Guilherme Silva <guilherme.silva@mpi.nl>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class})
public class LamusWorkspaceNodeLinkManagerTest {
    
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    private WorkspaceNodeLinkManager nodeLinkManager;
    
    @Mock WorkspaceParentNodeReferenceFactory mockWorkspaceParentNodeReferenceFactory;
    @Mock WorkspaceNodeLinkFactory mockWorkspaceNodeLinkFactory;
    @Mock WorkspaceDao mockWorkspaceDao;
    @Mock MetadataAPI mockMetadataAPI;
    @Mock WorkspaceFileHandler mockWorkspaceFileHandler;
    
    @Mock WorkspaceParentNodeReference mockWorkspaceParentNodeReference;
    @Mock WorkspaceNodeLink mockWorkspaceNodeLink;
    
    @Mock Workspace mockWorkspace;
    @Mock WorkspaceNode mockParentNode;
    @Mock WorkspaceNode mockOtherParentNode;
    @Mock WorkspaceNode mockChildNode;
    @Mock Reference mockChildReference;
    @Mock ResourceProxy mockChildReferenceWithHandle;

    @Mock ReferencingMetadataDocument mockParentDocument;
    @Mock ReferencingMetadataDocument mockOtherParentDocument;
    @Mock MetadataReference mockChildMetadataReference;
    @Mock ResourceReference mockChildResourceReference;
    
    @Mock File mockParentFile;
    @Mock StreamResult mockParentStreamResult;
    
    
    public LamusWorkspaceNodeLinkManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        nodeLinkManager = new LamusWorkspaceNodeLinkManager(
                mockWorkspaceParentNodeReferenceFactory, mockWorkspaceNodeLinkFactory,
                mockWorkspaceDao, mockMetadataAPI, mockWorkspaceFileHandler);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void linkNodesWithReference() throws URISyntaxException {
        
        final int parentNodeID = 1;
        final int childNodeID = 2;
        final URI childURI = new URI("http://some.uri");
        
        context.checking(new Expectations() {{
            
            oneOf(mockWorkspaceParentNodeReferenceFactory).getNewWorkspaceParentNodeReference(mockParentNode, mockChildReference);
                will(returnValue(mockWorkspaceParentNodeReference));
            
            oneOf(mockWorkspaceParentNodeReference).getParentWorkspaceNodeID(); will(returnValue(parentNodeID));
            oneOf(mockChildNode).getWorkspaceNodeID(); will(returnValue(childNodeID));
            oneOf(mockChildReference).getURI(); will(returnValue(childURI));
                
            oneOf(mockWorkspaceNodeLinkFactory).getNewWorkspaceNodeLink(parentNodeID, childNodeID, childURI);
                will(returnValue(mockWorkspaceNodeLink));
            oneOf(mockWorkspaceDao).addWorkspaceNodeLink(mockWorkspaceNodeLink);
        }});
        
        nodeLinkManager.linkNodesWithReference(mockParentNode, mockChildNode, mockChildReference);
    }

    @Test
    public void linkNodesWithReferenceWithNullParentNode() throws URISyntaxException, MalformedURLException, UnknownNodeException {
        
        final int childNodeID = 2;
        final URI childNodeURI = new URI(UUID.randomUUID().toString());
        final URL childNodeURL = new URL("file:/archive/somewhere/node.cmdi");
        final int workspaceID = 1;
        
        context.checking(new Expectations() {{
            
            oneOf(mockWorkspaceParentNodeReferenceFactory).getNewWorkspaceParentNodeReference(null, mockChildReference);
                will(returnValue(null));
                
            oneOf(mockChildNode).getWorkspaceNodeID(); will(returnValue(childNodeID));
            oneOf(mockWorkspace).setTopNodeID(childNodeID);
            oneOf(mockChildNode).getArchiveURI(); will(returnValue(childNodeURI));
            oneOf(mockWorkspace).setTopNodeArchiveURI(childNodeURI);
            oneOf(mockChildNode).getArchiveURL(); will(returnValue(childNodeURL));
            oneOf(mockWorkspace).setTopNodeArchiveURL(childNodeURL);
            
            oneOf(mockChildNode).getWorkspaceID(); will(returnValue(workspaceID));
            oneOf(mockWorkspaceDao).getWorkspace(workspaceID); will(returnValue(mockWorkspace));
            oneOf(mockWorkspaceDao).updateWorkspaceTopNode(mockWorkspace);
        }});
        
        nodeLinkManager.linkNodesWithReference(null, mockChildNode, mockChildReference);
    }
  
    //TODO top node -> UnknownNodeException
    
    
    
//    @Test
//    public void linkNodesWithNullChildReference() {
//        
////        context.checking(new Expectations() {{
////            
////            
////        }});
////        
////        nodeLinkManager.linkNodesWithReference(mockWorkspace, mockParentNode, mockChildNode, null);
//        
//        fail("What should happen when just the child reference is null?");
//    }

    @Test
    public void linkNodesMetadata() throws MalformedURLException, URISyntaxException, IOException, MetadataException, TransformerException {
        
        final int workspaceID = 1;
        final int parentNodeID = 2;
        final URL parentURL = new URL("file:/lamus/workspace/" + workspaceID + "/parent.cmdi");
        final int childNodeID = 3;
        final URL childURL = new URL("file:/lamus/workspace/" + workspaceID + "/child.cmdi");
        final URI childURI = childURL.toURI();
        final String childMimetype = "text/x-cmdi+xml";
        
        context.checking(new Expectations() {{
            
            oneOf(mockParentNode).getWorkspaceURL(); will(returnValue(parentURL));
            oneOf(mockMetadataAPI).getMetadataDocument(parentURL); will(returnValue(mockParentDocument));
            
            oneOf(mockChildNode).isMetadata(); will(returnValue(Boolean.TRUE));
            oneOf(mockChildNode).getWorkspaceURL(); will(returnValue(childURL));
            oneOf(mockChildNode).getFormat(); will(returnValue(childMimetype));
            oneOf(mockParentDocument).createDocumentMetadataReference(childURI, childMimetype);
                will(returnValue(mockChildMetadataReference));
            
            oneOf(mockParentNode).getWorkspaceURL(); will(returnValue(parentURL));
            oneOf(mockWorkspaceFileHandler).getStreamResultForNodeFile(mockParentFile); will(returnValue(mockParentStreamResult));
            oneOf(mockMetadataAPI).writeMetadataDocument(mockParentDocument, mockParentStreamResult);
            
            oneOf(mockParentNode).getWorkspaceNodeID(); will(returnValue(parentNodeID));
            oneOf(mockChildNode).getWorkspaceNodeID(); will(returnValue(childNodeID));
            oneOf(mockChildNode).getWorkspaceURL(); will(returnValue(childURL));
            
            oneOf(mockWorkspaceNodeLinkFactory).getNewWorkspaceNodeLink(parentNodeID, childNodeID, childURI);
                will(returnValue(mockWorkspaceNodeLink));
            
            oneOf(mockWorkspaceDao).addWorkspaceNodeLink(mockWorkspaceNodeLink);
        }});
        
        stub(method(FileUtils.class, "toFile", URL.class)).toReturn(mockParentFile);
        
        nodeLinkManager.linkNodes(mockParentNode, mockChildNode);
    }
    
    @Test
    public void linkNodesResource() throws MalformedURLException, URISyntaxException, IOException, MetadataException, TransformerException {
        
        final int workspaceID = 1;
        final int parentNodeID = 2;
        final URL parentURL = new URL("file:/lamus/workspace/" + workspaceID + "/parent.cmdi");
        final int childNodeID = 3;
        final URL childURL = new URL("file:/lamus/workspace/" + workspaceID + "/child.txt");
        final URI childURI = childURL.toURI();
        final String childMimetype = "text/x-cmdi+xml";
        final WorkspaceNodeType childWsType = WorkspaceNodeType.RESOURCE_WR;
        final String childStringType = childWsType.toString();
        
        context.checking(new Expectations() {{
            
            oneOf(mockParentNode).getWorkspaceURL(); will(returnValue(parentURL));
            oneOf(mockMetadataAPI).getMetadataDocument(parentURL); will(returnValue(mockParentDocument));
            
            oneOf(mockChildNode).isMetadata(); will(returnValue(Boolean.FALSE));
            oneOf(mockChildNode).getWorkspaceURL(); will(returnValue(childURL));
            oneOf(mockChildNode).getType(); will(returnValue(childWsType));
            oneOf(mockChildNode).getFormat(); will(returnValue(childMimetype));
            oneOf(mockParentDocument).createDocumentResourceReference(childURI, childStringType, childMimetype);
                will(returnValue(mockChildResourceReference));
                
            oneOf(mockParentNode).getWorkspaceURL(); will(returnValue(parentURL));
            oneOf(mockWorkspaceFileHandler).getStreamResultForNodeFile(mockParentFile); will(returnValue(mockParentStreamResult));
            oneOf(mockMetadataAPI).writeMetadataDocument(mockParentDocument, mockParentStreamResult);
            
            oneOf(mockParentNode).getWorkspaceNodeID(); will(returnValue(parentNodeID));
            oneOf(mockChildNode).getWorkspaceNodeID(); will(returnValue(childNodeID));
            oneOf(mockChildNode).getWorkspaceURL(); will(returnValue(childURL));
            
            oneOf(mockWorkspaceNodeLinkFactory).getNewWorkspaceNodeLink(parentNodeID, childNodeID, childURI);
                will(returnValue(mockWorkspaceNodeLink));
            
            oneOf(mockWorkspaceDao).addWorkspaceNodeLink(mockWorkspaceNodeLink);
        }});
        
        stub(method(FileUtils.class, "toFile", URL.class)).toReturn(mockParentFile);
        
        nodeLinkManager.linkNodes(mockParentNode, mockChildNode);
    }
    
    @Test
    public void unlinkNodesWithURI() throws MalformedURLException, URISyntaxException, IOException, MetadataException, TransformerException {
        
        final int workspaceID = 1;
        final int parentNodeID = 2;
        final URL parentURL = new URL("file:/lamus/workspace/" + workspaceID + "/parent.cmdi");
        final int childNodeID = 3;
        final URI childURI = new URI(UUID.randomUUID().toString());
        final URL childURL = new URL("file:/lamus/workspace/" + workspaceID + "/child.txt");
        
        context.checking(new Expectations() {{
            
            oneOf(mockParentNode).getWorkspaceURL(); will(returnValue(parentURL));
            oneOf(mockMetadataAPI).getMetadataDocument(parentURL); will(returnValue(mockParentDocument));
            
            oneOf(mockChildNode).getArchiveURI(); will(returnValue(childURI));
            
            oneOf(mockParentDocument).getDocumentReferenceByURI(childURI); will(returnValue(mockChildReferenceWithHandle));
            oneOf(mockParentDocument).removeDocumentReference(mockChildReferenceWithHandle); will(returnValue(mockChildReferenceWithHandle));
            
            oneOf(mockParentNode).getWorkspaceURL(); will(returnValue(parentURL));
            oneOf(mockWorkspaceFileHandler).getStreamResultForNodeFile(mockParentFile); will(returnValue(mockParentStreamResult));
            oneOf(mockMetadataAPI).writeMetadataDocument(mockParentDocument, mockParentStreamResult);
            
            oneOf(mockParentNode).getWorkspaceID(); will(returnValue(workspaceID));
            oneOf(mockParentNode).getWorkspaceNodeID(); will(returnValue(parentNodeID));
            oneOf(mockChildNode).getWorkspaceNodeID(); will(returnValue(childNodeID));
            
            oneOf(mockWorkspaceDao).deleteWorkspaceNodeLink(workspaceID, parentNodeID, childNodeID);
        }});
        
        stub(method(FileUtils.class, "toFile", URL.class)).toReturn(mockParentFile);
        
        nodeLinkManager.unlinkNodes(mockParentNode, mockChildNode);
    }
    
    @Test
    public void unlinkNodesWithoutURI() throws MalformedURLException, URISyntaxException, IOException, MetadataException, TransformerException {
        
        final int workspaceID = 1;
        final int parentNodeID = 2;
        final URL parentURL = new URL("file:/lamus/workspace/" + workspaceID + "/parent.cmdi");
        final int childNodeID = 3;
        final URL childURL = new URL("file:/lamus/workspace/" + workspaceID + "/child.cmdi");
        
        context.checking(new Expectations() {{
            
            oneOf(mockParentNode).getWorkspaceURL(); will(returnValue(parentURL));
            oneOf(mockMetadataAPI).getMetadataDocument(parentURL); will(returnValue(mockParentDocument));
            
            oneOf(mockChildNode).getArchiveURI(); will(returnValue(null));
            oneOf(mockChildNode).getWorkspaceURL(); will(returnValue(childURL));
            
            oneOf(mockParentDocument).getDocumentReferenceByURI(childURL.toURI()); will(returnValue(mockChildReference));
            oneOf(mockParentDocument).removeDocumentReference(mockChildReference); will(returnValue(mockChildReference));
            
            oneOf(mockParentNode).getWorkspaceURL(); will(returnValue(parentURL));
            oneOf(mockWorkspaceFileHandler).getStreamResultForNodeFile(mockParentFile); will(returnValue(mockParentStreamResult));
            oneOf(mockMetadataAPI).writeMetadataDocument(mockParentDocument, mockParentStreamResult);
            
            oneOf(mockParentNode).getWorkspaceID(); will(returnValue(workspaceID));
            oneOf(mockParentNode).getWorkspaceNodeID(); will(returnValue(parentNodeID));
            oneOf(mockChildNode).getWorkspaceNodeID(); will(returnValue(childNodeID));
            
            oneOf(mockWorkspaceDao).deleteWorkspaceNodeLink(workspaceID, parentNodeID, childNodeID);
        }});
        
        stub(method(FileUtils.class, "toFile", URL.class)).toReturn(mockParentFile);
        
        nodeLinkManager.unlinkNodes(mockParentNode, mockChildNode);
    }
    
    @Test
    public void unlinkNodeWithURIWithOneParent() throws MalformedURLException, URISyntaxException, IOException, MetadataException, TransformerException {
        
        final int workspaceID = 1;
        final int parentNodeID = 2;
        final URL parentURL = new URL("file:/lamus/workspace/" + workspaceID + "/parent.cmdi");
        final int childNodeID = 4;
        final URL childURL = new URL("file:/lamus/workspace/" + workspaceID + "/child.cmdi");
        final URI childURI = new URI(UUID.randomUUID().toString());
        
        final Collection<WorkspaceNode> parentNodes = new ArrayList<WorkspaceNode>();
        parentNodes.add(mockParentNode);
        
        context.checking(new Expectations() {{
            
            oneOf(mockChildNode).getWorkspaceNodeID(); will(returnValue(childNodeID));
            oneOf(mockWorkspaceDao).getParentWorkspaceNodes(childNodeID); will(returnValue(parentNodes));
        }});
        
        context.checking(new Expectations() {{
            
            oneOf(mockParentNode).getWorkspaceURL(); will(returnValue(parentURL));
            oneOf(mockMetadataAPI).getMetadataDocument(parentURL); will(returnValue(mockParentDocument));
            
            oneOf(mockChildNode).getArchiveURI(); will(returnValue(childURI));
            
            oneOf(mockParentDocument).getDocumentReferenceByURI(childURI); will(returnValue(mockChildReferenceWithHandle));
            oneOf(mockParentDocument).removeDocumentReference(mockChildReferenceWithHandle); will(returnValue(mockChildReferenceWithHandle));
            
            oneOf(mockParentNode).getWorkspaceURL(); will(returnValue(parentURL));
            oneOf(mockWorkspaceFileHandler).getStreamResultForNodeFile(mockParentFile); will(returnValue(mockParentStreamResult));
            oneOf(mockMetadataAPI).writeMetadataDocument(mockParentDocument, mockParentStreamResult);
            
            oneOf(mockParentNode).getWorkspaceID(); will(returnValue(workspaceID));
            oneOf(mockParentNode).getWorkspaceNodeID(); will(returnValue(parentNodeID));
            oneOf(mockChildNode).getWorkspaceNodeID(); will(returnValue(childNodeID));
            
            oneOf(mockWorkspaceDao).deleteWorkspaceNodeLink(workspaceID, parentNodeID, childNodeID);
        }});
        
        stub(method(FileUtils.class, "toFile", URL.class)).toReturn(mockParentFile);
        
        nodeLinkManager.unlinkNodeFromAllParents(mockChildNode);
    }
    
    @Test
    public void unlinkNodeWithURIWithSeveralParents() throws MalformedURLException, URISyntaxException, IOException, MetadataException, TransformerException {
        
        final int workspaceID = 1;
        final int parentNodeID = 2;
        final int otherParentNodeID = 3;
        final int[] parentIDs = { parentNodeID, otherParentNodeID };
        
        final URL parentURL = new URL("file:/lamus/workspace/" + workspaceID + "/parent.cmdi");
        final URL otherParentURL = new URL("file:/lamus/workspace/" + workspaceID + "/otherparent.cmdi");
        final URL[] parentURLs = { parentURL, otherParentURL };
        
        final int childNodeID = 4;
        final URL childURL = new URL("file:/lamus/workspace/" + workspaceID + "/child.cmdi");
        final URI childURI = new URI(UUID.randomUUID().toString());
        
        final Collection<WorkspaceNode> parentNodes = new ArrayList<WorkspaceNode>();
        parentNodes.add(mockParentNode);
        parentNodes.add(mockOtherParentNode);
        
        context.checking(new Expectations() {{
            
            oneOf(mockChildNode).getWorkspaceNodeID(); will(returnValue(childNodeID));
            oneOf(mockWorkspaceDao).getParentWorkspaceNodes(childNodeID); will(returnValue(parentNodes));
        }});
        
        // first iteration of the loop
        context.checking(new Expectations() {{
            
            oneOf(mockParentNode).getWorkspaceURL(); will(returnValue(parentURL));
            oneOf(mockMetadataAPI).getMetadataDocument(parentURL); will(returnValue(mockParentDocument));
            
            oneOf(mockChildNode).getArchiveURI(); will(returnValue(childURI));
            
            oneOf(mockParentDocument).getDocumentReferenceByURI(childURI); will(returnValue(mockChildReferenceWithHandle));
            oneOf(mockParentDocument).removeDocumentReference(mockChildReferenceWithHandle); will(returnValue(mockChildReferenceWithHandle));
            
            oneOf(mockParentNode).getWorkspaceURL(); will(returnValue(parentURL));
            oneOf(mockWorkspaceFileHandler).getStreamResultForNodeFile(mockParentFile); will(returnValue(mockParentStreamResult));
            oneOf(mockMetadataAPI).writeMetadataDocument(mockParentDocument, mockParentStreamResult);
            
            oneOf(mockParentNode).getWorkspaceID(); will(returnValue(workspaceID));
            oneOf(mockParentNode).getWorkspaceNodeID(); will(returnValue(parentNodeID));
            oneOf(mockChildNode).getWorkspaceNodeID(); will(returnValue(childNodeID));
            
            oneOf(mockWorkspaceDao).deleteWorkspaceNodeLink(workspaceID, parentNodeID, childNodeID);
        }});
        
        // second iteration of the loop
        context.checking(new Expectations() {{
            
            oneOf(mockOtherParentNode).getWorkspaceURL(); will(returnValue(otherParentURL));
            oneOf(mockMetadataAPI).getMetadataDocument(otherParentURL); will(returnValue(mockOtherParentDocument));
            
            oneOf(mockChildNode).getArchiveURI(); will(returnValue(childURI));
            
            oneOf(mockOtherParentDocument).getDocumentReferenceByURI(childURI); will(returnValue(mockChildReferenceWithHandle));
            oneOf(mockOtherParentDocument).removeDocumentReference(mockChildReferenceWithHandle); will(returnValue(mockChildReferenceWithHandle));
            
            oneOf(mockOtherParentNode).getWorkspaceURL(); will(returnValue(otherParentURL));
            oneOf(mockWorkspaceFileHandler).getStreamResultForNodeFile(mockParentFile); will(returnValue(mockParentStreamResult));
            oneOf(mockMetadataAPI).writeMetadataDocument(mockOtherParentDocument, mockParentStreamResult);
            
            oneOf(mockOtherParentNode).getWorkspaceID(); will(returnValue(workspaceID));
            oneOf(mockOtherParentNode).getWorkspaceNodeID(); will(returnValue(otherParentNodeID));
            oneOf(mockChildNode).getWorkspaceNodeID(); will(returnValue(childNodeID));
            
            oneOf(mockWorkspaceDao).deleteWorkspaceNodeLink(workspaceID, otherParentNodeID, childNodeID);
        }});
        
        stub(method(FileUtils.class, "toFile", URL.class)).toReturn(mockParentFile);
        
        nodeLinkManager.unlinkNodeFromAllParents(mockChildNode);
    }
}