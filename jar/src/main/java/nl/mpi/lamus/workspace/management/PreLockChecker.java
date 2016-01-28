/*
 * Copyright (C) 2016 Max Planck Institute for Psycholinguistics
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
package nl.mpi.lamus.workspace.management;

import java.net.URI;
import nl.mpi.lamus.exception.PreLockedNodeException;

/**
 * Checker for pre-locked nodes (nodes where a workspace is being created).
 * @author guisil
 */
public interface PreLockChecker {
    
    /**
     * Ensures that both ancestor and descendant nodes of the given node
     * are not pre-locked (have a workspace being created).
     * @param nodeURI URI of the node to check
     */
    public void ensureNoNodesInPathArePreLocked(URI nodeURI) throws PreLockedNodeException;
}
