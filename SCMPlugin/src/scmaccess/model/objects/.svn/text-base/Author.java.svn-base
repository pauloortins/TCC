package scmaccess.model.objects;

import java.util.LinkedList;

public class Author {
	private String name;
	private LinkedList<RevisionItem> revisionItems = new LinkedList<RevisionItem>();
	
	
	public Author(String authorName){
		this.name = authorName;
	}
	
	public void addRevisionItem(RevisionItem ri){
		revisionItems.add(ri);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<RevisionItem> getRevisionItems() {
		return revisionItems;
	}
	
}
