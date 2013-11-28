/*
 * Copyright (C) 2012 Max Planck Institute for Psycholinguistics
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
package nl.mpi.lamus.web.components;

import nl.mpi.lamus.service.WorkspaceService;
import nl.mpi.lamus.web.model.WorkspaceModel;
import nl.mpi.lamus.web.pages.IndexPage;
import nl.mpi.lamus.web.providers.LamusWicketPagesProvider;
import nl.mpi.lamus.workspace.model.Workspace;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Button page that allows navigation
 * 
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */

public final class ButtonPanel extends Panel {
// Services to be injected
    @SpringBean
    private WorkspaceService workspaceService;
    
    @SpringBean
    private LamusWicketPagesProvider pagesProvider;
    
    
    public ButtonPanel(String id, IModel<Workspace> model) {
        super(id, model);
        add(new WorkspaceActionsForm("workspaceActionsForm", model));
    }
    
    public ButtonPanel(String id, Workspace workspace) {
        this(id, new WorkspaceModel(workspace));
    }

    
    /**
     * Form that allows user to select actions on the current workspace
     */
    private class WorkspaceActionsForm extends Form<Workspace> {
        
        public WorkspaceActionsForm(String id, final IModel<Workspace> model) {
            super(id, model);

            final Button uploadFilesButton = new Button("uploadFilesButton") {

                @Override
                public void onSubmit() {
                    setResponsePage(pagesProvider.getUploadPage(model.getObject()));
                }
            };
            add(uploadFilesButton);

            final Button requestStorageButton = new Button("requestStorageButton") {

                @Override
                public void onSubmit() {
                    handleRequestStorage();
                }
            };
            add(requestStorageButton);

            final Button unlinkedFilesButton = new Button("unlinkedFilesButton") {

                @Override
                public void onSubmit() {
                    setResponsePage(pagesProvider.getUnlinkedNodesPage(model.getObject()));
                }
            };
            add(unlinkedFilesButton);
            
            final Button linkNodesButton = new Button("linkNodesButton") {

                @Override
                public void onSubmit() {
                    setResponsePage(pagesProvider.getLinkNodesPage(model.getObject()));
                }
            };
            add(linkNodesButton);
            
            final Button deleteWorkspaceButton = new Button("deleteWorkspaceButton") {
                
                @Override
                public void onSubmit() {
                    workspaceService.deleteWorkspace(model.getObject().getUserID(), model.getObject().getWorkspaceID());                   
                    setResponsePage(pagesProvider.getIndexPage());
                }
            };
            deleteWorkspaceButton.add(new AttributeModifier("onclick", "if(!confirm('are you sure?'))return false;"));
            add(deleteWorkspaceButton);
            
            final Button indexPageButton = new Button("indexPageButton") {

                @Override
                public void onSubmit() {
                    final IndexPage resultPage = new IndexPage();
                    setResponsePage(resultPage);
                }
            };
            add(indexPageButton);
        }
    }

    private void handleRequestStorage() {
    }

}