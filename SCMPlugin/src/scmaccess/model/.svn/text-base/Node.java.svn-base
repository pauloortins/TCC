package scmaccess.model;

import java.util.LinkedList;

public class Node {
	private String name;
	private String path;
	private LinkedList<Node> children = new LinkedList<Node>();
	private long revision;
	
	public Node(String name, String path, long revision){
		this.name = name;
		this.path = path;
		this.revision = revision;
	}
	
	public void addChildren(Node node){
		this.children.add(node);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	

	public LinkedList<Node> getChildren() {
		return children;
	}

	public void setChildren(LinkedList<Node> children) {
		this.children = children;
	}
	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}
	
	public String getFolderName(){
		String folderName;
		if (getPath().equals("/"))
			folderName = getName();
		else
			folderName = getPath().substring(1).replaceAll("/", ".")+"."+getName();
		return folderName;
	}
}
