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
package nl.mpi.lamus.filesystem.implementation;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import nl.mpi.lamus.filesystem.WorkspaceFileHandler;
import nl.mpi.lamus.workspace.model.Workspace;
import nl.mpi.lamus.workspace.model.WorkspaceNode;
import nl.mpi.metadata.api.MetadataAPI;
import nl.mpi.metadata.api.MetadataException;
import nl.mpi.metadata.api.model.MetadataDocument;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @see WorkspaceFileHandler
 * 
 * @author Guilherme Silva <guilherme.silva@mpi.nl>
 */
@Component
public class LamusWorkspaceFileHandler implements WorkspaceFileHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(LamusWorkspaceFileHandler.class);
    
    @Autowired
    @Qualifier("workspaceBaseDirectory")
    private File workspaceBaseDirectory;

    /**
     * @see WorkspaceFileHandler#copyMetadataFileToWorkspace(nl.mpi.lamus.workspace.model.Workspace, nl.mpi.lamus.workspace.model.WorkspaceNode, nl.mpi.metadata.api.MetadataAPI, nl.mpi.metadata.api.model.MetadataDocument, java.io.File, javax.xml.transform.stream.StreamResult)
     */
    @Override
    public void copyMetadataFile(WorkspaceNode workspaceNode,
            MetadataAPI metadataAPI, MetadataDocument metadataDocument, File originNodeFile, StreamResult targetNodeFileStreamResult)
                throws IOException, TransformerException, MetadataException {
        
        
        //TODO NO NEED TO BE A SEPARATE METHOD...
        
        
        metadataAPI.writeMetadataDocument(metadataDocument, targetNodeFileStreamResult);
    }
    
    /**
     * @see WorkspaceFileHandler#copyResourceFile(nl.mpi.lamus.workspace.model.WorkspaceNode, java.io.File, java.io.File)
     */
    @Override
    public void copyResourceFile(WorkspaceNode workspaceNode,
            File originNodeFile, File targetNodeFile)
                throws IOException {
        
        
        //TODO NO NEED TO BE A SEPARATE METHOD...
        
        
        FileUtils.copyFile(originNodeFile, targetNodeFile);
    }
    
    //TODO standardise the copy methods
    
    /**
     * @see WorkspaceFileHandler#copyFile(int, java.io.File, java.io.File)
     */
    @Override
    public void copyFile(int workspaceID, File originFile, File targetNodeFile)
            throws IOException {
        try {
            FileUtils.copyFile(originFile, targetNodeFile);
        } catch (IOException ioex) {
            String errorMessage = "Problem writing file " + targetNodeFile.getAbsolutePath();
            logger.error(errorMessage, ioex);
            throw ioex;
        }
    }
    
    /**
     * @see WorkspaceFileHandler#getStreamResultForWorkspaceNodeFile(java.io.File)
     */
    @Override
    public StreamResult getStreamResultForNodeFile(File nodeFile) {
            StreamResult streamResult = new StreamResult(nodeFile);
            return streamResult;
    }

    /**
     * @see WorkspaceFileHandler#getFileForImportedWorkspaceNode(java.net.URL, nl.mpi.lamus.workspace.model.WorkspaceNode)
     */
    @Override
    public File getFileForImportedWorkspaceNode(URL archiveNodeURL, WorkspaceNode workspaceNode) {
        File workspaceDirectory = new File(workspaceBaseDirectory, "" + workspaceNode.getWorkspaceID());
        String nodeFilename = FilenameUtils.getName(archiveNodeURL.toString());
        
        //TODO Should it be based on the name in the node's metadata??
        File workspaceNodeFile = new File(workspaceDirectory, nodeFilename);
        
        return workspaceNodeFile;
    }

}
