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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import nl.mpi.lamus.exception.TypeCheckerException;
import nl.mpi.lamus.exception.WorkspaceException;
import nl.mpi.lamus.service.WorkspaceService;
import nl.mpi.lamus.web.pages.LamusPage;
import nl.mpi.lamus.web.session.LamusSession;
import nl.mpi.lamus.workspace.model.Workspace;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author guisil
 */
public class UploadPanel extends Panel {
    
    private static final Logger log = LoggerFactory.getLogger(UploadPanel.class);

    public static final PackageResourceReference DELETE_IMAGE_RESOURCE_REFERENCE = new PackageResourceReference(LamusPage.class, "delete.gif");

    @SpringBean
    private WorkspaceService workspaceService;
    
    private final IModel<Workspace> model;
    
    
    /**
     * Constructor.
     *
     * @param parameters Page parameters
     */
    public UploadPanel(String id, IModel<Workspace> model) {
        
        super(id, model);
        
        this.model = model;

        // Add upload form with progress bar that uses HTML <input type="file" multiple />, so it can upload
        // more than one file in browsers which support "multiple" attribute
        final FileUploadForm progressUploadForm = new FileUploadForm("progressUpload");

        progressUploadForm.add(new UploadProgressBar("progress", progressUploadForm,
                progressUploadForm.fileUploadField));
        add(progressUploadForm);
    }
    

    /**
     * Form for uploads.
     */
    private class FileUploadForm extends Form<Void> {

        FileUploadField fileUploadField;

        /**
         * Construct.
         *
         * @param name Component name
         */
        public FileUploadForm(String name) {
            super(name);

            // set this form to multipart mode (allways needed for uploads!)
            setMultiPart(true);

            // Add one file input field
            add(fileUploadField = new FileUploadField("fileInput"));

            // Set maximum size to 100K for demo purposes
            //setMaxSize(Bytes.kilobytes(100));
        }

        /**
         * @see org.apache.wicket.markup.html.form.Form#onSubmit()
         */
        @Override
        protected void onSubmit() {
            final List<FileUpload> uploads = fileUploadField.getFileUploads();
            if (uploads != null) {
                
                File uploadDirectory = workspaceService.getWorkspaceUploadDirectory(model.getObject().getWorkspaceID());
                
                Collection<File> copiedFiles = new ArrayList<File>();
                
                for (FileUpload upload : uploads) {
                    // Create a new file
                    File newFile = new File(uploadDirectory, upload.getClientFileName());
                    
                    if(newFile.isDirectory()) {
                        continue;
                    }
                    
                    //TODO a better way of deciding this? typechecker?
                    if(newFile.getName().endsWith(".zip")) {
                    
                        try {
                        
                            byte[] buffer = new byte[1024];

                            InputStream newInputStream = upload.getInputStream();
                            ZipInputStream zipInputStream = new ZipInputStream(newInputStream);
                            ZipEntry nextEntry = zipInputStream.getNextEntry();
                            while(nextEntry != null) {
                                File entryFile = new File(uploadDirectory, nextEntry.getName());
                                if(nextEntry.isDirectory()) {
                                    entryFile.mkdirs();
                                    nextEntry = zipInputStream.getNextEntry();
                                    continue;
                                }
                                OutputStream outputStream = new FileOutputStream(entryFile);
                                int len;
                                while((len = zipInputStream.read(buffer)) > 0) {
                                    outputStream.write(buffer, 0, len);
                                }
                                outputStream.close();
                                nextEntry = zipInputStream.getNextEntry();
                                copiedFiles.add(entryFile);
                            }

                            zipInputStream.close();

                            UploadPanel.this.info(getLocalizer().getString("upload_panel_success_message", this) + upload.getClientFileName());
                        } catch(IOException ex) {
                            UploadPanel.this.error(ex.getMessage());
                        }
                    } else {
                    
                        try {
                            //TODO PERFORM A "SHALLOW" TYPECHECK BEFORE UPLOADING?

                            // Save to new file
                            newFile.createNewFile();
                            upload.writeTo(newFile);

                            //TODO ADD UPLOADED FILE TO LIST OF FILES TO PROCESS LATER
                            copiedFiles.add(newFile);

                            UploadPanel.this.info(getLocalizer().getString("upload_panel_success_message", this) + upload.getClientFileName());
                        } catch (Exception e) {
                            throw new IllegalStateException(getLocalizer().getString("upload_panel_failure_message", this), e);
                        }
                    }
                }
                try {
                    Map<File, String> failedUploads = workspaceService.processUploadedFiles(LamusSession.get().getUserId(), model.getObject().getWorkspaceID(), copiedFiles);
                    
                    for(File failedFile : failedUploads.keySet()) {
                        UploadPanel.this.error(failedUploads.get(failedFile));
                    }
                    
                } catch (IOException | WorkspaceException | TypeCheckerException ex) {
                    UploadPanel.this.error(ex.getMessage());
                }
                
            }
        }
    }
}
