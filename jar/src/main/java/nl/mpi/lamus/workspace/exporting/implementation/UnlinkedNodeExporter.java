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
package nl.mpi.lamus.workspace.exporting.implementation;

import java.net.URL;
import nl.mpi.lamus.exception.WorkspaceExportException;
import nl.mpi.lamus.workspace.exporting.NodeExporter;
import nl.mpi.lamus.workspace.exporting.VersioningHandler;
import nl.mpi.lamus.workspace.model.Workspace;
import nl.mpi.lamus.workspace.model.WorkspaceNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author guisil
 */
public class UnlinkedNodeExporter implements NodeExporter{
    
    private static final Logger logger = LoggerFactory.getLogger(UnlinkedNodeExporter.class);

    private final VersioningHandler versioningHandler;
    
    private Workspace workspace;
    
    public UnlinkedNodeExporter(VersioningHandler versioningHandler) {

        this.versioningHandler = versioningHandler;
    }
    
    /**
     * @see NodeExporter#getWorkspace()
     */
    @Override
    public Workspace getWorkspace() {
        return this.workspace;
    }

    /**
     * @see NodeExporter#setWorkspace(nl.mpi.lamus.workspace.model.Workspace)
     */
    @Override
    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    /**
     * @see NodeExporter#exportNode(nl.mpi.lamus.workspace.model.WorkspaceNode, nl.mpi.lamus.workspace.model.WorkspaceNode)
     */
    @Override
    public void exportNode(WorkspaceNode parentNode, WorkspaceNode currentNode) throws WorkspaceExportException {

        //TODO FOR NOW SIMILAR TO DeletedNodeExporter, but will have to be changed
            // when new functionality is added regarding unlinked nodes
        
        
        if (workspace == null) {
	    String errorMessage = "Workspace not set";
	    logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
	}
        
        logger.debug("Exporting unlinked node to archive; workspaceID: " + workspace.getWorkspaceID() + "; currentNodeID: " + currentNode.getWorkspaceNodeID());
        
        if(currentNode.getArchiveURI() == null) {
            
            logger.debug("Node " + currentNode.getWorkspaceNodeID() + " was not in the archive previously; will be skipped and eventually deleted with the workspace folder");
            // if there is no archiveURI, the node was never in the archive, so it can actually be deleted;
            // to make it easier, that node can simply be skipped and eventually will be deleted together with the whole workspace folder
            return;
        }
        
        if(currentNode.isProtected()) { // a protected node should remain intact after the workspace submission
            logger.info("Node " + currentNode.getWorkspaceNodeID() + " is protected; skipping export of this node to keep it intact in the archive");
            return;
        }

        //TODO What to do with this URL? Update it and use to inform the crawler of the change?
        
        URL trashedNodeArchiveURL = this.versioningHandler.moveFileToTrashCanFolder(currentNode);
        currentNode.setArchiveURL(trashedNodeArchiveURL);
        
        //TODO is this necessary?
//        searchClientBridge.removeNode(currentNode.getArchiveURI());
        
        //TODO REMOVE LINK FROM PARENT (IF THERE IS ONE)
    }
    
}
