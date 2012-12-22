package scmaccess.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.branding.IProductConstants;
import org.eclipse.ui.progress.IProgressConstants;
import org.omg.CORBA.RepositoryIdHelper;

import dubaj.internacionalizacao.InternationalResource;
import dubaj.tr.RelatorioFinal;

import scmaccess.model.Node;
import scmaccess.model.SCM;
import scmaccess.model.SCMHistoryData;
import scmaccess.model.objects.Element;
import scmplugin.popup.actions.GetProjectEvolutionAction;

public class ViewSelectVersions extends AbstractForm{
	private Text txtDestinationFolder;
	private Tree scmTree; 
	private ArrayList selectedNodeOnTree = new ArrayList();
	private Shell parent;
	private Text txtInitialRevision;
	private Text txtFinalRevision;
	
	private SCM scm;
	
	//Display display = Display.getCurrent();

	public ViewSelectVersions(Shell parent, SCM scm) {
		super(InternationalResource.getIdioma().getSelectVersions(),parent);
		this.parent = parent;
		this.scm = scm;
		this.initComponents();
		//		print();
	}
	
	public ViewSelectVersions(SCM scm) {
		super(InternationalResource.getIdioma().getSelectVersions());
		this.parent = null;
		this.scm = scm;
		this.initComponents();
		//print();
	}
	
	/* print test */
	void print() {
		SCMHistoryData scmdata = scm.proccessHistory(100, 502);

		Collection c = scmdata.getElements().values();
		for (Iterator entries = c.iterator(); entries.hasNext();) {
			Element element = (Element) entries.next();
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
	/* print test end */

	public void initComponents() {
		//Destination Folder
		Label lblDestinationFolder = new Label(this.ioShell, SWT.SINGLE);
		lblDestinationFolder.setText(InternationalResource.getIdioma().getDestinationFolder());
		FormData loDataDestinationFolderLabel = new FormData();
		lblDestinationFolder.setLayoutData(loDataDestinationFolderLabel);
		this.txtDestinationFolder = new Text(this.ioShell, SWT.BORDER | SWT.WRAP
				| SWT.MULTI);
		FormData loDataDestinationFolder = new FormData(300, 16);
		loDataDestinationFolder.left = new FormAttachment(lblDestinationFolder, 20);
		this.txtDestinationFolder.setLayoutData(loDataDestinationFolder);
		this.txtDestinationFolder.setText(SCMAccessMain.getStartFolder());
		
		Button butBrowserFolder = new Button(this.ioShell, SWT.BUTTON1);
		butBrowserFolder.setText(InternationalResource.getIdioma().getBrowser());
		FormData loDataBrowserFolder = new FormData(100, 19);
		loDataBrowserFolder.top = new FormAttachment(this.txtDestinationFolder, 10);
		loDataBrowserFolder.left = new FormAttachment(lblDestinationFolder, 170);
		butBrowserFolder.setLayoutData(loDataBrowserFolder);
		
		butBrowserFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(ioShell, SWT.NULL);
				String path = dialog.open();
				if (path != null) {

					File file = new File(path);
					if (!file.isFile()){
						txtDestinationFolder.setText(file.getAbsolutePath());
					}	
				}
			}
		});
		
		// Ortins Label
		Label lblAvisoNumeroRevisoes = new Label(this.ioShell, SWT.SINGLE);
		lblAvisoNumeroRevisoes.setText(InternationalResource.getIdioma().getNumberOfRevisionsOfRepository() + " " + scm.getLastRevisionNumber());
		FormData loLblAvisoNumeroRevisoes = new FormData();
		loLblAvisoNumeroRevisoes.top = new FormAttachment(this.txtDestinationFolder,30);
		lblAvisoNumeroRevisoes.setLayoutData(loLblAvisoNumeroRevisoes);
		
		// ORTINS
		// Number of revisions
		
		// Initial Revision
		Label lblInitialRevision = new Label(this.ioShell, SWT.SINGLE);
		lblInitialRevision.setText(InternationalResource.getIdioma().getInitialRevision());
		
		FormData loLblInitialRevision = new FormData();
		loLblInitialRevision.top = new FormAttachment(this.txtDestinationFolder,50);
		lblInitialRevision.setLayoutData(loLblInitialRevision);
		
		this.txtInitialRevision = new Text(this.ioShell, SWT.BORDER | SWT.WRAP
				| SWT.MULTI);
		FormData loTxtInitialRevision = new FormData(70, 16);
		loTxtInitialRevision.top = new FormAttachment(this.txtDestinationFolder, 50);
		loTxtInitialRevision.left = new FormAttachment(lblInitialRevision, 20);
		this.txtInitialRevision.setLayoutData(loTxtInitialRevision);
		this.txtInitialRevision.setText("0");
		
		// Final Revision
		Label lblFinalRevision = new Label(this.ioShell, SWT.SINGLE);
		lblFinalRevision.setText(InternationalResource.getIdioma().getFinalRevision());
		
		FormData loLblFinalRevision = new FormData();
		loLblFinalRevision.top = new FormAttachment(this.txtInitialRevision,10);
		lblFinalRevision.setLayoutData(loLblFinalRevision);
		
		this.txtFinalRevision = new Text(this.ioShell, SWT.BORDER | SWT.WRAP
				| SWT.MULTI);
		FormData loTxtFinalRevision = new FormData(70, 16);
		loTxtFinalRevision.top = new FormAttachment(this.txtInitialRevision, 10);
		loTxtFinalRevision.left = new FormAttachment(lblFinalRevision, 24);
		this.txtFinalRevision.setLayoutData(loTxtFinalRevision); 
		this.txtFinalRevision.setText("" + scm.getLastRevisionNumber());
		
		// Tree
		Label lblTree = new Label(this.ioShell, SWT.SINGLE);
		lblTree.setText(InternationalResource.getIdioma().getSCMData());
		FormData loDataTreeLabel = new FormData();
		loDataTreeLabel.top = new FormAttachment(this.txtFinalRevision, 20);
		lblTree.setLayoutData(loDataTreeLabel);
		
		this.scmTree = new Tree(this.ioShell, SWT.CHECK | SWT.BORDER);
		FormData loDataScmTree = new FormData(300, 200);
		loDataScmTree.top = new FormAttachment(this.txtFinalRevision, 20);
		loDataScmTree.left = new FormAttachment(lblTree, 40);
		this.scmTree.setLayoutData(loDataScmTree);
		
		fillTree(this.scmTree, null, scm.getDirectoryList());
		
		this.scmTree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {

				if (event.detail == SWT.CHECK) {
					TreeItem ti = (TreeItem) event.item;
					if (selectedNodeOnTree.contains(ti))
						selectedNodeOnTree.remove(ti);
					else
						selectedNodeOnTree.add(ti);
				}
			}
		});
		
		Button butDownload = new Button(this.ioShell, SWT.BUTTON1);
		butDownload.setText(InternationalResource.getIdioma().getStartDownload());
		FormData loDataButDownLoad = new FormData(100, 19);
		loDataButDownLoad.top = new FormAttachment(scmTree, 20);
		loDataButDownLoad.left = new FormAttachment(lblDestinationFolder, 130);
		butDownload.setLayoutData(loDataButDownLoad);
			
		butDownload.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				
				// Zera o relatorio
				RelatorioFinal.zerarRelatorio();
				
				// Valida interface com usuario
				if (!validarUI()) {
					return;
				}
				
				// Recebe dados da UI
				String path = txtDestinationFolder.getText();
				final int initialRevision = Integer.parseInt(txtInitialRevision.getText());
				final int finalRevision = Integer.parseInt(txtFinalRevision.getText());
				
				File dir = new File(path);   
				if (!dir.exists() || dir.isFile()) {
					showMessage("The path "+path+" is not valid");
				}else{
					SCMAccessMain.setFolderExport(path);
					// ############ Versions ##############
					for (int i = 0; i < selectedNodeOnTree.size(); i++) {
						TreeItem ti = (TreeItem) selectedNodeOnTree.get(i);
						final Node currentNode = (Node) ti.getData();
					
						try {
							Job job = new Job("Analyzing...") {
							    @Override
							    protected IStatus run(IProgressMonitor monitor) {
							        
							    	// execute the task ...
							        try {
										scm.exportPath(currentNode, SCMAccessMain.getFolderExport(),initialRevision,finalRevision, monitor);
										//SCMAccessMain.addExportedNode(currentNode);
										
										System.out.println("############\n"
												+ "Download Finished: Name: "
												+ currentNode.getName() + " - " + "Path: "
												+ currentNode.getPath());
										
										monitor.done();
										
							        } catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							        
							        setProperty(IProgressConstants.KEEP_PROPERTY, Boolean.TRUE);
							        setProperty(IProgressConstants.ACTION_PROPERTY, getCompleteAction());
							        
							        return Status.OK_STATUS;
							    }

								private Action getCompleteAction() {
									// TODO Auto-generated method stub
									return new Action("View reservation status") {
										public void run() {
											showMessage(InternationalResource.getIdioma().getDownloadFinished());
											new ViewShowReport().show();
											ioShell.dispose();
										}
									};
								}
							};
							job.schedule();
							
						} catch (Exception e1) {
							System.out.println("Error while downloading...\n"
									+ e1.getMessage());
						}
						

					}
					
					
					
					
				}
			}

			private boolean validarUI() {
				// TODO Auto-generated method stub
				
				int initialRevision;
				int finalRevision;
				
				if (txtDestinationFolder.getText().equals("")) {
					showMessage(InternationalResource.getIdioma().getDestinationFolderRequired());
					return false;
				}
				else if (txtInitialRevision.getText().equals("")) {
					showMessage(InternationalResource.getIdioma().getInitialRevisionRequired());
					return false;
				} else if (txtFinalRevision.getText().equals("")) {
					showMessage(InternationalResource.getIdioma().getFinalRevisionRequired());
					return false;
				}
				
				try {
					initialRevision = Integer.parseInt(txtInitialRevision.getText());
				} catch (Exception e) {
					// TODO: handle exception
					showMessage(InternationalResource.getIdioma().getInitialRevisionInvalid());
					return false;
				}
				
				try {
					finalRevision = Integer.parseInt(txtFinalRevision.getText());
				} catch (Exception e) {
					// TODO: handle exception
					showMessage(InternationalResource.getIdioma().getFinalRevisionInvalid());
					return false;
				}
				
				if (initialRevision < 0) {
					showMessage(InternationalResource.getIdioma().getInitialRevisionLesserThanZeroOrGreaterThanNumberOfRevisions());
					return false;
				}
				
				if (finalRevision <= initialRevision) {
					showMessage(InternationalResource.getIdioma().getFinalRevisionLesserThanInitialRevision());
					return false;
				}
				
				if (finalRevision > scm.getLastRevisionNumber()) {
					showMessage(InternationalResource.getIdioma().getFinalRevisionLesserThanZeroOrGreaterThanNumberOfRevisions());
					return false;
				}
				
				return true;
			}
		});
	}

	
	 /*// ################# VAI SER USADO REVISIONS#################
	  final String selectedDirectory = cboDirectories.getText();
	  
	  scm.setSelectedDirectory(selectedDirectory);
	  
	  shell.dispose();
	  
	  ViewSelectRevisions v = new ViewSelectRevisions(scm);
	  v.open();*/
	
	public void fillTree(Tree tree, TreeItem item, Node node) {
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

}
