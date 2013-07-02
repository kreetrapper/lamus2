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
package nl.mpi.lamus.workspace.model.implementation;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import nl.mpi.lamus.workspace.model.WorkspaceNode;
import nl.mpi.lamus.workspace.model.WorkspaceNodeStatus;
import nl.mpi.lamus.workspace.model.WorkspaceNodeType;
import nl.mpi.lamus.workspace.model.WorkspaceParentNodeReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author Guilherme Silva <guilherme.silva@mpi.nl>
 */
public class LamusWorkspaceNode implements WorkspaceNode {
    
    private int workspaceNodeID;
    private int workspaceID;

    //TODO Worth having???
    private int archiveNodeID;
    
    private URI profileSchemaURI;
    private String name;
    private String title;
    private WorkspaceNodeType type;
    private URL workspaceURL;
    private URL archiveURL;
    private URL originURL;
    private WorkspaceNodeStatus status;
    private String pid;
    private String format;
    private Collection<WorkspaceParentNodeReference> parentNodesReferences;
    
    public LamusWorkspaceNode() {
        
    }
    
    public LamusWorkspaceNode(int workspaceID, int archiveNodeID, URL archiveURL, URL originURL) {
        this.workspaceID = workspaceID;
        this.archiveNodeID = archiveNodeID;
        this.archiveURL = archiveURL;
        this.originURL = originURL;
    }
    
    public LamusWorkspaceNode(int workspaceNodeID, int workspaceID, int archiveNodeID,
            URI profileSchemaURI, String name, String title, WorkspaceNodeType type,
            URL workspaceURL, URL archiveURL, URL originURL,
            WorkspaceNodeStatus status, String pid, String format) {
        
        this.workspaceNodeID = workspaceNodeID;
        this.workspaceID = workspaceID;
        this.archiveNodeID = archiveNodeID;
        this.profileSchemaURI = profileSchemaURI;
        this.name = name;
        this.title = title;
        this.type = type;
        this.workspaceURL = workspaceURL;
        this.archiveURL = archiveURL;
        this.originURL = originURL;
        this.status = status;
        this.pid = pid;
        this.format = format;
    }

    @Override
    public int getWorkspaceNodeID() {
        return this.workspaceNodeID;
    }
    
    @Override
    public void setWorkspaceNodeID(int workspaceNodeID) {
        this.workspaceNodeID = workspaceNodeID;
    }

    @Override
    public int getWorkspaceID() {
        return this.workspaceID;
    }
    
    @Override
    public void setWorkspaceID(int workspaceID) {
        this.workspaceID = workspaceID;
    }

    @Override
    public int getArchiveNodeID() {
        return this.archiveNodeID;
    }
    
    @Override
    public void setArchiveNodeID(int archiveNodeID) {
        this.archiveNodeID = archiveNodeID;
    }

    @Override
    public URI getProfileSchemaURI() {
        return this.profileSchemaURI;
    }
    
    @Override
    public void setProfileSchemaURI(URI profileSchemaURI) {
        this.profileSchemaURI = profileSchemaURI;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public WorkspaceNodeType getType() {
        return this.type;
    }
    
    @Override
    public boolean isMetadata() {
        return WorkspaceNodeType.METADATA.equals(this.type);
    }
    
    @Override
    public void setType(WorkspaceNodeType type) {
        this.type = type;
    }

    @Override
    public URL getWorkspaceURL() {
        return this.workspaceURL;
    }
    
    @Override
    public void setWorkspaceURL(URL workspaceURL) {
        this.workspaceURL = workspaceURL;
    }

    @Override
    public URL getArchiveURL() {
        return this.archiveURL;
    }
    
    @Override
    public void setArchiveURL(URL archiveURL) {
        this.archiveURL = archiveURL;
    }

    @Override
    public URL getOriginURL() {
        return this.originURL;
    }
    
    @Override
    public void setOriginURL(URL originURL) {
        this.originURL = originURL;
    }

    @Override
    public WorkspaceNodeStatus getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(WorkspaceNodeStatus status) {
        this.status = status;
    }

    @Override
    public String getPid() {
        return this.pid;
    }

    @Override
    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String getFormat() {
        return this.format;
    }
    
    @Override
    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public Collection<WorkspaceParentNodeReference> getParentNodesReferences() {
        return Collections.unmodifiableCollection(this.parentNodesReferences);
    }

    @Override
    public void setParentNodesReferences(Collection<WorkspaceParentNodeReference> parentNodesReferences) {
        this.parentNodesReferences = parentNodesReferences;
    }
    
    @Override
    public void addParentNodeReference(WorkspaceParentNodeReference parentNodeReference) {
        if(this.parentNodesReferences == null) {
            this.parentNodesReferences = new ArrayList<WorkspaceParentNodeReference>();
        }
        this.parentNodesReferences.add(parentNodeReference);
    }
    

    @Override
    public int hashCode() {
        
        HashCodeBuilder hashCodeB = new HashCodeBuilder()
                .append(this.workspaceNodeID)
                .append(this.workspaceID)
                .append(this.archiveNodeID)
                .append(this.profileSchemaURI)
                .append(this.name)
                .append(this.title)
                .append(this.type)
                .append(this.workspaceURL)
                .append(this.archiveURL)
                .append(this.originURL)
                .append(this.status)
                .append(this.pid)
                .append(this.format);
                
        return hashCodeB.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof LamusWorkspaceNode)) {
            return false;
        }
        LamusWorkspaceNode other = (LamusWorkspaceNode) obj;
        
        
        EqualsBuilder equalsB = new EqualsBuilder()
                .append(this.workspaceNodeID, other.getWorkspaceNodeID())
                .append(this.workspaceID, other.getWorkspaceID())
                .append(this.archiveNodeID, other.getArchiveNodeID())
                .append(this.profileSchemaURI, other.getProfileSchemaURI())
                .append(this.name, other.getName())
                .append(this.title, other.getTitle())
                .append(this.type, other.getType())
                .append(this.workspaceURL, other.getWorkspaceURL())
                .append(this.archiveURL, other.getArchiveURL())
                .append(this.originURL, other.getOriginURL())
                .append(this.status, other.getStatus())
                .append(this.pid, other.getPid())
                .append(this.format, other.getFormat());
        
        return equalsB.isEquals();
    }
    
    @Override
    public String toString() {
        
        String stringResult = "Workspace Node ID: " + this.workspaceNodeID + ", Workspace ID: " + this.workspaceID +
                ", Archive Node ID: " + this.archiveNodeID + ", Profile Schema URI: " + this.profileSchemaURI +
                ", Name: " + this.name + ", Title: " + this.title +
                ", Type: " + this.type + ", Workspace URL: " + this.workspaceURL +
                ", Archive URL: " + this.archiveURL + ", Origin URL: " + this.originURL +
                ", Status: " + this.status + ", PID: " + this.pid + ", Format: " + this.format;
        
        return stringResult;
    }
}
