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
package nl.mpi.lamus.workspace.model;

/**
 * Enumeration for the different possible types of nodes within a workspace.
 * @author guisil
 */
public enum WorkspaceNodeType {

    METADATA,
    
    RESOURCE_IMAGE,
    RESOURCE_AUDIO,
    RESOURCE_VIDEO,
    RESOURCE_WRITTEN,
    
    RESOURCE_INFO,
    
    RESOURCE_OTHER,
    
    UNKNOWN;
    
    @Override
    public String toString() {
        return (name().charAt(0) + name().substring(1).toLowerCase()).replace("_", " - ");
    }
}
