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
package nl.mpi.lamus.workspace.exporting;

import nl.mpi.lamus.exception.WorkspaceExportException;
import nl.mpi.lamus.workspace.model.Workspace;
import nl.mpi.lamus.workspace.model.WorkspaceExportPhase;
import nl.mpi.lamus.workspace.model.WorkspaceNode;
import nl.mpi.lamus.workspace.model.WorkspaceSubmissionType;

/**
 * Generic interface for a node exporter.
 * 
 * @author guisil
 */
public interface NodeExporter {
    
    /**
     * Exports the given node from the workspace to the archive
     * @param workspace Workspace currently being exported
     * @param parentNode Parent of the node to export
     * @param parentCorpusNamePathToClosestTopNode Path of corpus names
     *  from the parent node up to the closest top node
     * @param currentNode Node to export
     * @param keepUnlinkedFiles true if unlinked files are to be kept for future use
     *  (only used in the unlinked files exporter)
     * @param submissionType indicates whether the method is being executed
     * during a workspace submission or deletion
     * @param exportPhase indicates whether the workspace export is currently in
     * the first stage, in which the tree is exported, or in the second stage,
     * in which the unlinked nodes are exported
     */
    public void exportNode(
            Workspace workspace, WorkspaceNode parentNode,
            String parentCorpusNamePathToClosestTopNode,
            WorkspaceNode currentNode, boolean keepUnlinkedFiles,
            WorkspaceSubmissionType submissionType, WorkspaceExportPhase exportPhase)
            throws WorkspaceExportException;
    
}
