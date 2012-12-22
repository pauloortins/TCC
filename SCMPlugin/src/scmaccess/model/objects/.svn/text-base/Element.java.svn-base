package scmaccess.model.objects;

import java.util.LinkedList;

public class Element {
	private String name;
	private String path;
	private LinkedList<RevisionItem> revisionItems = new LinkedList<RevisionItem>();

	public void addRevisionItem(RevisionItem ri){
		revisionItems.add(ri);
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
	public LinkedList<RevisionItem> getRevisionItems() {
		return revisionItems;
	}
}
