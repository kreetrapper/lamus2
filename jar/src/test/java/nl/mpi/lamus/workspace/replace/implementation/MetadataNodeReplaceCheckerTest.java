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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.lamus.archive.ArchiveFileHelper;
import nl.mpi.lamus.dao.WorkspaceDao;
import nl.mpi.lamus.workspace.model.WorkspaceNode;
import nl.mpi.lamus.workspace.replace.NodeReplaceChecker;
import nl.mpi.lamus.workspace.replace.NodeReplaceExplorer;
import nl.mpi.lamus.workspace.replace.action.ReplaceActionFactory;
import nl.mpi.lamus.workspace.replace.action.ReplaceActionManager;
import nl.mpi.lamus.workspace.replace.action.implementation.DeleteNodeReplaceAction;
import nl.mpi.lamus.workspace.replace.action.implementation.LinkNodeReplaceAction;
import nl.mpi.lamus.workspace.replace.action.implementation.NodeReplaceAction;
import nl.mpi.lamus.workspace.replace.action.implementation.ReplaceNodeReplaceAction;
import nl.mpi.lamus.workspace.replace.action.implementation.UnlinkNodeReplaceAction;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
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
public class MetadataNodeReplaceCheckerTest {
    
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    @Mock ReplaceActionManager mockReplaceActionManager;
    @Mock ReplaceActionFactory mockReplaceActionFactory;
    @Mock NodeReplaceExplorer mockNodeReplaceExplorer;
    
    @Mock WorkspaceNode mockOldNode;
    @Mock WorkspaceNode mockNewNode;
    @Mock WorkspaceNode mockParentNode;
    @Mock CorpusNode mockOldCorpusNode;
//    @Mock FileInfo mockOldCorpusNodeFileInfo;
    
    @Mock UnlinkNodeReplaceAction mockUnlinkAction;
    @Mock DeleteNodeReplaceAction mockDeleteAction;
    @Mock LinkNodeReplaceAction mockLinkAction;
    @Mock ReplaceNodeReplaceAction mockReplaceAction;
    
    private List<NodeReplaceAction> actions;
    
    
    private NodeReplaceChecker nodeReplaceChecker;
    
    public MetadataNodeReplaceCheckerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        nodeReplaceChecker = new MetadataNodeReplaceChecker(
                mockReplaceActionManager, mockReplaceActionFactory,
                mockNodeReplaceExplorer);
        actions = new ArrayList<NodeReplaceAction>();
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void decideReplaceActionsNotLinked() throws URISyntaxException, MalformedURLException {
        
        final int oldNodeID = 100;
        final int newNodeID = 200;
        
        final boolean newNodeAlreadyLinked = Boolean.FALSE;
        
        context.checking(new Expectations() {{
            
            //logger
            oneOf(mockOldNode).getWorkspaceNodeID(); will(returnValue(oldNodeID));
            oneOf(mockNewNode).getWorkspaceNodeID(); will(returnValue(newNodeID));
            
            //TODO CHECK IF FILE EXISTS, IS A FILE AND IS READABLE?
            
            
            oneOf(mockReplaceActionFactory).getReplaceAction(mockOldNode, mockParentNode, mockNewNode, newNodeAlreadyLinked); will(returnValue(mockReplaceAction));
            oneOf(mockReplaceActionManager).addActionToList(mockReplaceAction, actions);
            
            //TODO MULTIPLE PARENTS???
            
            oneOf(mockNodeReplaceExplorer).exploreReplace(mockOldNode, mockNewNode);
            
        }});
        
        nodeReplaceChecker.decideReplaceActions(mockOldNode, mockNewNode, mockParentNode, newNodeAlreadyLinked, actions);
    }
}