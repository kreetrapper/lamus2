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
package nl.mpi.lamus.workspace.tree.implementation;

import nl.mpi.archiving.tree.GenericTreeModelProviderFactory;
import nl.mpi.archiving.tree.LinkedTreeModelProvider;
import nl.mpi.lamus.workspace.tree.WorkspaceTreeNode;

/**
 * @see GenericTreeModelProviderFactory
 * 
 * @author guisil
 */
public class WorkspaceTreeModelProviderFactory implements GenericTreeModelProviderFactory<WorkspaceTreeNode> {

    /**
     * @see GenericTreeModelProviderFactory#createTreeModelProvider(nl.mpi.archiving.tree.GenericTreeNode)
     */
    @Override
    public LinkedTreeModelProvider createTreeModelProvider(WorkspaceTreeNode rootNode) {
        return new WorkspaceTreeModelProvider(rootNode);
    }
}
