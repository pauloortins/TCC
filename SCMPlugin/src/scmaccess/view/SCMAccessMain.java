package scmaccess.view;

import java.util.ArrayList;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

import dubaj.tr.RelatorioFinal;

import scmaccess.model.Node;

public class SCMAccessMain {
	
	private static String folderExport = "";
	//public static String imageIconPath = "images\\repo.ico";
	private static ArrayList<Node> listOfExportedNodes;
	private static final String FOLDER_SOURCEMINER_EVOLUTION = "SourceMinerEvolution"; 
	
	public SCMAccessMain(){
		
		listOfExportedNodes = new ArrayList<Node>(); 
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IPath location = root.getLocation();
		folderExport = location.toPortableString();
		new ViewConfigureConnection().show();
	}
	/*public static SCMAccessMain getSingleton(){
		if (singleton == null) singleton = new SCMAccessMain();
		return singleton;
	}*/
	
	public static String getFolderExport() {
		return folderExport+"/"+FOLDER_SOURCEMINER_EVOLUTION;
	}
	
	public static String getStartFolder(){
		return folderExport;
	}


	public static void setFolderExport(String folderExport) {
		SCMAccessMain.folderExport = folderExport;
	}


	public static ArrayList<Node> getListOfExportedNodes() {
		return SCMAccessMain.listOfExportedNodes;
	}


	public static void setListOfExportedNodes(ArrayList<Node> listOfExportedNodes) {
		SCMAccessMain.listOfExportedNodes = listOfExportedNodes;
	}
	public static void addExportedNode(Node node){
		SCMAccessMain.listOfExportedNodes.add(node);
	}


	public static void main(String[] args) {
		/*ViewConnection v = new ViewConnection();
		v.open();*/
		new ViewConfigureConnection().show();
		
		String s = "ola";
	}
	
	

}
