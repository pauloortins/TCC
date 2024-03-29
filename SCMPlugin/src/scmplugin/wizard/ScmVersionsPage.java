package scmplugin.wizard;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import scmaccess.model.Node;
import scmaccess.model.SCM;
import scmaccess.model.SCMHistoryData;
import scmaccess.model.objects.Element;
import scmaccess.model.objects.InfoSCM;
import scmaccess.model.objects.SmeProperties;

class ScmVersionsPage extends WizardPage implements Listener{

	public static final String PAGE_Title = "Repository Settings";
	// Widgets
	private Text txtDestinationFolder;
	private Tree scmTree;

	// Array which contain all the selected versions of tRepositoryRepository.
	private ArrayList<TreeItem> selectedNodeOnTree = new ArrayList<TreeItem>();
	

	// Error Status
	private IStatus pathStatus;
	
	

	public ScmVersionsPage() {
		super("Page 3", PAGE_Title, null);
		setDescription("Select Repository Versions");
		pathStatus = new Status(IStatus.OK, "not_used", 0, "", null);
	}

	private String setInitialFolderDestination() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IPath location = root.getLocation();
		String folderExport = location.toPortableString();
		return folderExport;
	}

	@Override
	public void createControl(Composite parent) {
		Composite topLevel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);

		Label lblDestinationFolder = new Label(topLevel, SWT.SINGLE);
		lblDestinationFolder.setText("Destination Folder:");

		this.txtDestinationFolder = new Text(topLevel, SWT.BORDER | SWT.WRAP
				| SWT.MULTI);
		GridData destinationData = new GridData(300, 16);

		Button butBrowserFolder = new Button(topLevel, SWT.BUTTON1);
		butBrowserFolder.setText("Browse");
		this.txtDestinationFolder.setLayoutData(destinationData);
		this.txtDestinationFolder.setText(setInitialFolderDestination());
		butBrowserFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(new Shell(Display
						.getCurrent()), SWT.NULL);
				String path = dialog.open();
				if (path != null) {

					File file = new File(path);
					if (!file.isFile()) {
						txtDestinationFolder.setText(file.getAbsolutePath());
					}
				}
			}
		});

		// Tree
		Label lblTree = new Label(topLevel, SWT.SINGLE);
		lblTree.setText("SCM Data:");

		this.scmTree = new Tree(topLevel, SWT.CHECK | SWT.BORDER);
		GridData loDataScmTree = new GridData(300, 200);
		this.scmTree.setLayoutData(loDataScmTree);
		
		topLevel.setLayout(layout);
		setControl(topLevel);
		addListeners();
	}

	@Override
	public void handleEvent(Event event) {
		if (event.detail == SWT.CHECK) {
			TreeItem ti = (TreeItem) event.item;
			if (selectedNodeOnTree.contains(ti))
				selectedNodeOnTree.remove(ti);
			else
				selectedNodeOnTree.add(ti);
			getWizard().getContainer().updateButtons();
		}
		
	}

	private void print(SCM scmConnected) {
		SCMHistoryData scmdata = scmConnected.proccessHistory(100, 502);

		Collection<Element> c = scmdata.getElements().values();
		for (Iterator<Element> entries = c.iterator(); entries.hasNext();) {
			Element element = entries.next();
			int numberRevisions = element.getRevisionItems().size();
			if (numberRevisions > 10)
				System.out.println("---" + element.getName() + " - "
						+ numberRevisions);
			/*
			 * if (numberRevisions > 30){ System.out.println(element.getName());
			 * for (RevisionItem ri : element.getRevisionItems()){
			 * System.out.println("---");
			 * System.out.println("Revision Number: "+ri.getRevisionNumber());
			 * System.out.println("Log Message: "+ri.getLogMessage());
			 * System.out.println("Author: "+ri.getAuthor().getName());
			 * System.out.println("Date: "+ri.getDate()); }
			 * System.out.println("-----------------"); }
			 */

		}
	}

	public void addListeners() {
		this.scmTree.addListener(SWT.Selection, this);
		this.txtDestinationFolder.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent event) {
				Text text = (Text) event.widget;
				String path = text.getText();
				File dir = new File(path);   
				if (!dir.exists() || dir.isFile())
					pathStatus = new Status(IStatus.ERROR, "notUsed", 0,
							"Invalid Path", null);
				else
					pathStatus = new Status(IStatus.OK, "not_used", 0, "", null);
				applyToStatusLine();
				getWizard().getContainer().updateButtons();
			}
		});
	}

	private void applyToStatusLine() {
		String message = pathStatus.getMessage();
		if (message.length() == 0)
			message = null;
		switch (pathStatus.getSeverity()) {
		case IStatus.OK:
			setErrorMessage(null);
			setMessage(message);
			break;
		default:
			setErrorMessage(message);
			setMessage(null);
			break;
		}
		
	}

	public void fillTree(SCM scmConnected){
		fillTree(this.scmTree, null,scmConnected.getDirectoryList());
	}
	
	private void fillTree(Tree tree, TreeItem item, Node node) {
		scmTree.setRedraw(false);
		int size = node.getChildren().size();
		for (int i = 0; i < size; i++) {
			Node currentNode = node.getChildren().get(i);
			TreeItem currentItem;
			if (item == null)
				currentItem = new TreeItem(tree, SWT.NONE);
			else
				currentItem = new TreeItem(item, SWT.NONE);
			currentItem.setText(currentNode.getName());
			currentItem.setData(currentNode);
			fillTree(tree, currentItem, currentNode);
		}
		scmTree.setRedraw(true);
	}

	
	
	public boolean canFlipToNextPage() {
		if (isTextNonEmpty(txtDestinationFolder)&& (selectedNodeOnTree.size() != 0)) {
			return true;
		}
		return false;
	}
	
	private static boolean isTextNonEmpty(Text t) {
		String s = t.getText();
		if ((s != null) && (s.trim().length() > 0))
			return true;
		return false;
	}
	

	public IWizardPage getPreviousPage() {
		ScmWizardPage page = ((SmeWizard) getWizard()).scmPage;
		return page;
	}
	
	public IWizardPage getNextPage(){
		ScmSummaryPage summaryPage = ((SmeWizard) getWizard()).summaryPage;
		InfoSCM info = ((SmeWizard) getWizard()).info;
		SmeProperties sme = info.getSme();
		sme.setMainPath(this.txtDestinationFolder.getText());
		info.setSme(sme);
		summaryPage.setSummaryGroup(info);
		summaryPage.setListVersions(selectedNodeOnTree);
		return summaryPage;
	}
	
	public List<TreeItem> getOrderingVersions(){
		return selectedNodeOnTree;
	}
	
	public String getMainPath(){
		return this.txtDestinationFolder.getText();
	}
}
