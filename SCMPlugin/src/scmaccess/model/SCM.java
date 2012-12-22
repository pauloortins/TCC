package scmaccess.model;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;

import org.eclipse.core.runtime.IProgressMonitor;

import scmaccess.model.exceptions.SCMConnectionRefusedException;
import scmaccess.model.objects.RevisionItem;

public abstract class SCM {
	public String url;
	public String user;
	public String password;
	public String selectedDirectory = "";
	
	/**
	 * Connect to the SCM repository. 
	 * Returns true if the connection has been accepted, false otherwise
	 * */
	public abstract void connect(String url, String user, String password) throws SCMConnectionRefusedException;

	/**
	 * It returns a list of all directory of the repository.
	 * This is help the user to select the directory that, for example, represents a project.
	 * At the final of the user' selection action, you should set the selectedDirectory attribute
	 * */
	public abstract Node getDirectoryList();
	
	public abstract long getLastRevisionNumber();
	
	public abstract String[] getDirectoryList2();
	
	/**
	 * This method returns a String table containing the list of 
	 * revisions.	 
	 * */
	public abstract LinkedList<RevisionItem> getListOfRevisions();
	
	/**
	 * This method exports contents of the repository directory into file system using
	 * a revision number. It will export to a localPath
	 * the name of the generated folder will be dirpath+"_"+revision
	 * where this.selectedDirectory is a directory selected by the user 
	 * calling getDirectoryList() 
	 * */
	public abstract void exportRevision(long revision, String localPath) throws Exception;
	
	public abstract void exportPath(Node node, String localPath) throws Exception;
	
	public abstract void exportPath(Node node, String localPath, long initialRevision, long finalRevision) throws Exception;
	
	public abstract SCMHistoryData proccessHistory(int startRevision, int endRevision);
	
	public LinkedList<RevisionItem> getSortedListOfRevisions(){
		LinkedList<RevisionItem> list = getListOfRevisions();
		Collections.sort(list);
		return list;
	}
	
	public void setSelectedDirectory(String dir){
		this.selectedDirectory = dir;
	}
	
	public String getSelectedDirectory(){
		return this.selectedDirectory;
	}
	
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public abstract void exportPath(Node currentNode, String folderExport,
			long initialRevision, long finalRevision, IProgressMonitor monitor) throws Exception;
	
}
