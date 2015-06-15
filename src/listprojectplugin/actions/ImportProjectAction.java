
package listprojectplugin.actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import listprojectplugin.Activator;
//import listprojectplugin.preferences.PreferenceConstants;














import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.progress.IProgressService;

/**
 * Our sample action implements workbench action delegate. The action proxy will be created by the workbench and shown
 * in the UI. When the user tries to use the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class ImportProjectAction
    implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    /**
     * The constructor.
     */
    public ImportProjectAction() {}

    public void run(IAction action) {

    	
    	IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
		ISelectionService selectionService = (ISelectionService)window.getSelectionService();
		ISelection sel = selectionService.getSelection();
		
		if(sel instanceof TreeSelection){
			TreeSelection tsel = (TreeSelection) sel;
			Object sSelObj =  tsel.getFirstElement();
			System.out.println(sSelObj);

			String path = null;
			if(sSelObj instanceof Folder){
				Folder selF = (Folder) sSelObj;
				path = selF.getLocation().toString();
			}else if(sSelObj instanceof org.eclipse.core.internal.resources.File){
				org.eclipse.core.internal.resources.File file = (org.eclipse.core.internal.resources.File) sSelObj;
				path = file.getParent().getLocation().toString();
				
			}else if (sSelObj instanceof PackageFragmentRoot){
				PackageFragmentRoot pfr = (PackageFragmentRoot) sSelObj;
				path = pfr.getPath().toFile().getAbsolutePath();
			}
			if(path!=null){
				
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
				
				try {
                    IPath projectDotProjectFile = new Path(path + "/.project");
                    IProjectDescription projectDescription = workspace.loadProjectDescription(projectDotProjectFile);
                    JavaPlugin.logErrorMessage("got project description for "
                        + projectDescription.getName());
                    IProject project = workspace.getRoot().getProject(projectDescription.getName());
                    JavaPlugin.logErrorMessage("created project for " + projectDescription.getName());
                    JavaCapabilityConfigurationPage.createProject(project, projectDescription.getLocationURI(), null);
                    JavaPlugin
                        .logErrorMessage("created project with JavaCapabilityConfigurationPage for "
                            + projectDescription.getName());
                }
                catch(CoreException e) {
                    //JavaPlugin.log(e);
//                    MessageDialog.openInformation(window.getShell(), "Bulk Import Plug-in", e
//                        .toString());
                    e.printStackTrace();
                }
				
			}				
		}
    }

    /**
     * Selection in the workbench has been changed. We can change the state of the 'real' action here if we want, but
     * this can only happen after the delegate has been created.
     * 
     * @see IWorkbenchWindowActionDelegate#selectionChanged
     */
    public void selectionChanged(IAction action, ISelection selection) {}

    /**
     * We can use this method to dispose of any system resources we previously allocated.
     * 
     * @see IWorkbenchWindowActionDelegate#dispose
     */
    public void dispose() {}

    /**
     * We will cache window object in order to be able to provide parent shell for the message dialog.
     * 
     * @see IWorkbenchWindowActionDelegate#init
     */
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }
}