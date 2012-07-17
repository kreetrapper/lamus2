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
package nl.mpi.lamus.workspace.importing;

import nl.mpi.lamus.dao.WorkspaceDao;
import nl.mpi.lamus.workspace.exception.FileExplorerException;
import nl.mpi.lamus.workspace.exception.FileImporterException;
import nl.mpi.lamus.workspace.importing.implementation.FileImporterFactoryBean;
import nl.mpi.lamus.workspace.importing.implementation.MetadataFileImporter;
import nl.mpi.lamus.workspace.model.Workspace;
import nl.mpi.metadata.api.MetadataException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.*;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

/**
 *
 * @author Guilherme Silva <guilherme.silva@mpi.nl>
 */
public class WorkspaceImportRunnerTest {
    
    Synchroniser synchroniser = new Synchroniser();
    Mockery context = new JUnit4Mockery() {{
        setThreadingPolicy(synchroniser);
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    private WorkspaceImportRunner workspaceImportRunner;
    private final WorkspaceDao mockWorkspaceDao = context.mock(WorkspaceDao.class);
    private final WorkspaceFileExplorer mockWorkspaceFileExplorer = context.mock(WorkspaceFileExplorer.class);
    private final FileImporter mockFileImporter = context.mock(FileImporter.class);
    private final FileImporterFactoryBean mockFileImporterFactoryBean = context.mock(FileImporterFactoryBean.class);
    
    private final Workspace mockWorkspace = context.mock(Workspace.class);
    private int topNodeArchiveID = 10;

    public WorkspaceImportRunnerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        workspaceImportRunner = new WorkspaceImportRunner(mockWorkspaceDao, mockWorkspaceFileExplorer, mockFileImporterFactoryBean);
        workspaceImportRunner.setWorkspace(mockWorkspace);
        workspaceImportRunner.setTopNodeArchiveID(topNodeArchiveID);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class WorkspaceImportRunner.
     */
    @Test
    public void runsSuccessfully() throws Exception {
        
        final States importing = context.states("importing");
        
        context.checking(new Expectations() {{
            
            oneOf (mockFileImporterFactoryBean).setFileImporterTypeForReference(null);
            oneOf (mockFileImporterFactoryBean).getObject(); will(returnValue(mockFileImporter));
                when(importing.isNot("finished"));
            oneOf (mockFileImporter).setWorkspace(mockWorkspace);
                when(importing.isNot("finished"));
            oneOf (mockFileImporter).importFile(null, null, null, topNodeArchiveID);
                then(importing.is("finished"));
        }});
        
        
        executeRunner();
        
        long timeoutInMs = 2000L;
        synchroniser.waitUntil(importing.is("finished"), timeoutInMs);
    }
    
    /**
     * Test of run method, of class WorkspaceImportRunner.
     */
    @Test
    public void throwsException() throws Exception {
        
        final String someExceptionMessage = "some exception message";
        
        final States importing = context.states("importing");
        
        context.checking(new Expectations() {{

            oneOf (mockFileImporterFactoryBean).setFileImporterTypeForReference(null);
            oneOf (mockFileImporterFactoryBean).getObject(); will(throwException(new Exception(someExceptionMessage)));
            oneOf (mockFileImporter).setWorkspace(mockWorkspace);
                when(importing.isNot("finished"));
            never (mockFileImporter).importFile(null, null, null, topNodeArchiveID);
            oneOf (mockWorkspace).setStatusMessageErrorDuringInitialisation();
            oneOf (mockWorkspaceDao).updateWorkspaceStatusMessage(mockWorkspace);
                then(importing.is("finished"));
            
            //TODO expect a call to a listener indicating failure
        }});
        
        
        executeRunner();
        
        long timeoutInMs = 2000L;
        synchroniser.waitUntil(importing.is("finished"), timeoutInMs);
    }
    
    /**
     * Test of run method, of class WorkspaceImportRunner.
     */
    @Test
    public void throwsFileImporterException() throws Exception {
        
        final Class<? extends FileImporter> expectedImporterType = MetadataFileImporter.class;
        final String expectedExceptionMessage = "this is a test message for the exception";
        final Throwable expectedExceptionCause = new MetadataException("this is a test message for the exception cause");
        
        final States importing = context.states("importing");
        
        context.checking(new Expectations() {{

            oneOf (mockFileImporterFactoryBean).setFileImporterTypeForReference(null);
            oneOf (mockFileImporterFactoryBean).getObject(); will(returnValue(mockFileImporter));
                when(importing.isNot("finished"));
            oneOf (mockFileImporter).setWorkspace(mockWorkspace);
                when(importing.isNot("finished"));
            oneOf (mockFileImporter).importFile(null, null, null, topNodeArchiveID);
                will(throwException(new FileImporterException(expectedExceptionMessage, mockWorkspace, expectedImporterType, expectedExceptionCause)));
            
            oneOf (mockWorkspace).setStatusMessageErrorDuringInitialisation();
            oneOf (mockWorkspaceDao).updateWorkspaceStatusMessage(mockWorkspace);
                then(importing.is("finished"));
            
            //TODO expect a call to a listener indicating failure
        }});
        
        
        executeRunner();
        
        long timeoutInMs = 2000L;
        synchroniser.waitUntil(importing.is("finished"), timeoutInMs);
    }
    
    /**
     * Test of run method, of class WorkspaceImportRunner.
     */
    @Test
    public void throwsFileExplorerException() throws Exception {
        
        final String expectedExceptionMessage = "this is a test message for the exception";
        final Throwable expectedExceptionCause = null;
        
        final States importing = context.states("importing");
        
        context.checking(new Expectations() {{

            oneOf (mockFileImporterFactoryBean).setFileImporterTypeForReference(null);
            oneOf (mockFileImporterFactoryBean).getObject(); will(returnValue(mockFileImporter));
                when(importing.isNot("finished"));
            oneOf (mockFileImporter).setWorkspace(mockWorkspace);
                when(importing.isNot("finished"));
            oneOf (mockFileImporter).importFile(null, null, null, topNodeArchiveID);
                will(throwException(new FileExplorerException(expectedExceptionMessage, mockWorkspace, expectedExceptionCause)));
            
            oneOf (mockWorkspace).setStatusMessageErrorDuringInitialisation();
            oneOf (mockWorkspaceDao).updateWorkspaceStatusMessage(mockWorkspace);
                then(importing.is("finished"));
            
            //TODO expect a call to a listener indicating failure
        }});
        
        
        executeRunner();
        
        long timeoutInMs = 2000L;
        synchroniser.waitUntil(importing.is("finished"), timeoutInMs);
    }
    
    private void executeRunner() {
        
        TaskExecutor executor = new SimpleAsyncTaskExecutor();
        executor.execute(workspaceImportRunner);
    }
}