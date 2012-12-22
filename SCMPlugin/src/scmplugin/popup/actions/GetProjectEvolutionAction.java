package scmplugin.popup.actions;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import scmaccess.model.Node;
import scmaccess.model.SCM;
import scmaccess.view.ViewConfigureConnection;
import scmaccess.view.SCMAccessMain;

public class GetProjectEvolutionAction implements IObjectActionDelegate {

	private Shell shell;
	private static SCM scm;

	/**
	 * Constructor for Action1.
	 */
	public GetProjectEvolutionAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	public void creatingAProject2() {
		IJavaProject ijproject = null;
		try {
			
			/*o problema é quando o projeto tem outro nome um nome de pasta e tem um project description com nome diferente 
			 * isto ocorre quando está dentro do workspace*/
			//E:\projetos\java\workspace\runtime-EclipseApplication\SourceminerEvolution\628\Mobile Media
			String path = "E:/projetos/java/workspace/runtime-EclipseApplication/SourceminerEvolution/629/";
			String projectFolder = "Mobile Media";//"Mobile Media OO";//
			String file = path+"/"+projectFolder+"/.project";
			//file = "C:/Documents and Settings/Renato Novais/Desktop/projetos/tags.V2.0.Mobile Media/.project";
			//Mobile Media01_OO
			IProjectDescription description = ResourcesPlugin.getWorkspace()
					.loadProjectDescription(new Path(file));

			IProject project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(description.getName());
			//description.setName("tags.V2.0.Mobile Media");
			
			/*boolean isWorkspace = false;
			boolean changed = false;
			if (isWorkspace && true) {//is workspace
				File projectDir = new File(path+"/"+projectFolder);
				if (projectDir.exists()) {//exist a folder with the same name
					
				}
				
				File dest = new File(path+"/"+description.getName());
				projectDir.renameTo(dest);
				
				changed = true;
			}*/
			
			if (project.exists())
				project.delete(true, null);
			project.create(description, IProject.FORCE, null);//
			project.open(null);
			
			/*if (changed){
				File projectDir = new File(path+"/"+description.getName());
				if (projectDir.exists()) {//exist a folder with the same name
					
				}
				File dest = new File(path+"/"+projectFolder);
				projectDir.renameTo(dest);
			}*/
			
			description.setName(description.getName()+" 629");
			project.move(description, IProject.FORCE, null);
			ijproject = JavaCore.create(project);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IPath location = root.getLocation();
		// folderExport = location5
		System.out.println(location.toOSString());
		System.out.println(location.toPortableString());
		System.out.println(location.toString());*/
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		new SCMAccessMain();
		//creatingAProject2();
		// creatingAProject2();
		
		//processAllProject();
		 

		/*
		 * MessageDialog.openInformation(shell, "SCMPlugin",
		 * "Import Project was executed.");
		 */

	}

	public static void processAllProject() {
		ArrayList<Node> listOfExportedNodes = SCMAccessMain
				.getListOfExportedNodes();
		if (listOfExportedNodes != null && listOfExportedNodes.size() > 0) {
			for (Node currentNode : listOfExportedNodes)
				processProject(currentNode);

		} else {
			// TODO no project or some error has happen
		}
	}

	private static void processProject(Node node) {
		IJavaProject ijproject = null;
		try {

			String file = SCMAccessMain.getFolderExport() + "/" +node.getRevision()+"/"
					+ node.getName() + "/.project";
			IProjectDescription description = ResourcesPlugin.getWorkspace()
					.loadProjectDescription(new Path(file));

			IProject project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(description.getName());
			
			if (project.exists())
				project.delete(true, null);
			project.create(description, IProject.FORCE, null);
			project.open(null);
			description.setName(node.getName()+" "+node.getRevision());
			project.move(description, IProject.FORCE, null);
			ijproject = JavaCore.create(project);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
