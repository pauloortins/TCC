package scmaccess.model;

import java.util.HashMap;
import java.util.LinkedList;

import scmaccess.model.objects.Author;
import scmaccess.model.objects.Element;
import scmaccess.model.objects.RevisionItem;

public class SCMHistoryData {
	private LinkedList<RevisionItem> generalRevisionItems = new LinkedList<RevisionItem>();
	private HashMap<String, Element> elements = new HashMap<String, Element>();
	private HashMap<String, Author> authors = new HashMap<String, Author>();
	
	public void addGeneralRevisionItem(RevisionItem ri){
		generalRevisionItems.add(ri);
	}
	
	public HashMap<String, Author> getAuthors() {
		return authors;
	}
	public void setAuthors(HashMap<String, Author> authors) {
		this.authors = authors;
	}
	public LinkedList<RevisionItem> getGeneralRevisionItems() {
		return generalRevisionItems;
	}
	public void setGeneralRevisionItems(LinkedList<RevisionItem> revisionItems) {
		this.generalRevisionItems = revisionItems;
	}
	public HashMap<String, Element> getElements() {
		return elements;
	}
	public void setElements(HashMap<String, Element> elements) {
		this.elements = elements;
	}
}
