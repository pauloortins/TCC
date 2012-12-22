package scmaccess.model.objects;

import java.util.Date;
import java.util.LinkedList;

public class RevisionItem implements Comparable{
	private long revisionNumber;
	private Author author;
	private Date date;
	private String logMessage;
	private LinkedList<ChangedPath> changedPaths = new LinkedList<ChangedPath>();
	
	public RevisionItem(long revisionNumber, Author author, 
						Date date, String logMessage){
		this.revisionNumber = revisionNumber;
		
		this.author = author;
		this.date = date;
		this.logMessage = (logMessage==null)?"":logMessage; 
	}
	
	public void addChangedPaths(ChangedPath cp){
		changedPaths.add(cp);
	}
	
	public LinkedList<ChangedPath> getChangedPaths() {
		return changedPaths;
	}


	public long getRevisionNumber() {
		return revisionNumber;
	}
	public void setRevisionNumber(long revisionNumber) {
		this.revisionNumber = revisionNumber;
	}
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getLogMessage() {
		return logMessage;
	}
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	
	@Override
	public int compareTo(Object arg0) {
		
		return (this.revisionNumber >= ((RevisionItem)arg0).revisionNumber)? 0 : 1;
	}
	
}
