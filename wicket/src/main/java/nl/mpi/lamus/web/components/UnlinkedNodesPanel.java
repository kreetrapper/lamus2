/*
 * Copyright (C) 2013 Max Planck Institute for Psycholinguistics
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import nl.mpi.lamus.exception.WorkspaceAccessException;
import nl.mpi.lamus.exception.WorkspaceException;
import nl.mpi.lamus.exception.WorkspaceNotFoundException;
import nl.mpi.lamus.service.WorkspaceService;
import nl.mpi.lamus.web.pages.LamusPage;
import nl.mpi.lamus.web.session.LamusSession;
import nl.mpi.lamus.web.unlinkednodes.model.SelectedUnlinkedNodesWrapper;
import nl.mpi.lamus.web.unlinkednodes.model.WorkspaceTreeNodeExpansion;
import nl.mpi.lamus.web.unlinkednodes.providers.UnlinkedNodesModelProvider;
import nl.mpi.lamus.workspace.model.Workspace;
import nl.mpi.lamus.workspace.tree.WorkspaceTreeNode;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.DefaultTableTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.TableTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.content.CheckedFolder;
import org.apache.wicket.extensions.markup.html.repeater.tree.content.Folder;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author guisil
 */
public class UnlinkedNodesPanel extends FeedbackPanelAwarePanel<Workspace> {
    
    public static final PackageResourceReference DELETE_IMAGE_RESOURCE_REFERENCE = new PackageResourceReference(LamusPage.class, "delete.gif");
    
    @SpringBean
    private WorkspaceService workspaceService;
    
    private AbstractTree<WorkspaceTreeNode> unlinkedNodesTree;
    
    private Collection<WorkspaceTreeNode> checked;
    
    
    public UnlinkedNodesPanel(String id, IModel<Workspace> model, UnlinkedNodesModelProvider provider, FeedbackPanel feedbackPanel) {
        super(id, model, feedbackPanel);
        
        checked = new ArrayList<WorkspaceTreeNode>();
        
        unlinkedNodesTree = createTree("unlinkedNodesTableTree", provider, new WorkspaceTreeNodeExpansionModel());
        unlinkedNodesTree.setOutputMarkupPlaceholderTag(true);
        
        add(unlinkedNodesTree);
        
        setOutputMarkupId(true);
    }
    
    
    private AbstractTree<WorkspaceTreeNode> createTree(String id, UnlinkedNodesModelProvider provider, IModel<Set<WorkspaceTreeNode>> state) {
        
        List<IColumn<WorkspaceTreeNode, String>> columns = createColumns();
        
        final TableTree<WorkspaceTreeNode, String> tree = new DefaultTableTree<WorkspaceTreeNode, String>(id, columns, provider, Integer.MAX_VALUE, state) {

            @Override
            protected Component newContentComponent(String id, IModel<WorkspaceTreeNode> model) {
                
                if(model.getObject().getParent() != null) {
                    return new Folder<WorkspaceTreeNode>(id, unlinkedNodesTree, model);
                } else {
                    return new CheckedFolder<WorkspaceTreeNode>(id, unlinkedNodesTree, model) {

                        private static final long serialVersionUID = 1L;
                        
                        @Override
                        protected IModel<Boolean> newCheckBoxModel(final IModel<WorkspaceTreeNode> model) {
                            
                            return new IModel<Boolean>() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public Boolean getObject() {
                                    return isChecked(model.getObject());
                                }

                                @Override
                                public void setObject(Boolean object) {
                                    check(model.getObject(), object);
                                }

                                @Override
                                public void detach() {
                                }
                            };
                        }
                    };
                }
            }

            @Override
            protected Item<WorkspaceTreeNode> newRowItem(String id, int index, IModel<WorkspaceTreeNode> model) {
                return new OddEvenItem<WorkspaceTreeNode>(id, index, model);
            }
        };

        return tree;
    }
    
    
    private List<IColumn<WorkspaceTreeNode, String>> createColumns() {
        
        List<IColumn<WorkspaceTreeNode, String>> columns = new ArrayList<IColumn<WorkspaceTreeNode, String>>();
        
        columns.add(new TreeColumn<WorkspaceTreeNode, String>(Model.of(getLocalizer().getString("unlinked_nodes_table_column_node", this))));

        columns.add(new PropertyColumn<WorkspaceTreeNode, String>(Model.of(getLocalizer().getString("unlinked_nodes_table_column_type", this)), "type"));
        
        columns.add(new PropertyColumn<WorkspaceTreeNode, String>(Model.of(getLocalizer().getString("unlinked_nodes_table_column_status", this)), "statusAsString"));

        columns.add(new AbstractColumn<WorkspaceTreeNode, String>(Model.of("")) {
            
            @Override
            public void populateItem(Item<ICellPopulator<WorkspaceTreeNode>> cellItem, String componentId, IModel<WorkspaceTreeNode> model) {
                
                Link<WorkspaceTreeNode> deleteLink =new Link<WorkspaceTreeNode>(componentId, model) {

                    @Override
                    public void onClick() {
                        
                        try {
                            //TODO Add confirmation dialog
                            workspaceService.deleteNode(LamusSession.get().getUserId(), getModelObject());

                        } catch (WorkspaceNotFoundException ex) {
                            error(ex.getMessage());
                        } catch (WorkspaceAccessException ex) {
                            error(ex.getMessage());
                        } catch (WorkspaceException ex) {
                            error(ex.getMessage());
                        }
                    }
                    
                };
                deleteLink.setBody(Model.of(getLocalizer().getString("unlinked_nodes_table_column_remove_button", UnlinkedNodesPanel.this)));
                deleteLink.add(AttributeModifier.append("class", new Model<String>("tableActionLink")));
                cellItem.add(deleteLink);
            }
        });
        
        return columns;
    }

    
    public Collection<WorkspaceTreeNode> getSelectedUnlinkedNodes() {
        return checked;
    }
    
    
    public void clearSelectedUnlinkedNodes() {
        checked = new ArrayList<WorkspaceTreeNode>();
    }
    
    
    private Iterator<WorkspaceTreeNode> getChecked() {
        return checked.iterator();
    }
    
    private boolean isChecked(WorkspaceTreeNode wsTreeNode) {
        return checked.contains(wsTreeNode);
    }

    private void check(WorkspaceTreeNode wsTreeNode, boolean check) {
        if (check && !checked.contains(wsTreeNode)) {
            checked.add(wsTreeNode);
        }
        else {
            checked.remove(wsTreeNode);
        }
        
        send(this, Broadcast.BUBBLE, new SelectedUnlinkedNodesWrapper(checked));
    }
    
    
    private static class WorkspaceTreeNodeExpansionModel extends AbstractReadOnlyModel<Set<WorkspaceTreeNode>> {

        public WorkspaceTreeNodeExpansionModel() {
        }

        @Override
        public Set<WorkspaceTreeNode> getObject() {
            return WorkspaceTreeNodeExpansion.get();
        }
    }
}
