package scmplugin.wizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TreeItem;

import scmaccess.model.Node;
import scmaccess.model.SCM;
import scmaccess.model.objects.InfoSCM;
import scmaccess.model.objects.Version;
import scmaccess.view.SCMAccessMain;
import scmplugin.persistence.InfoScmDao;
import scmplugin.popup.actions.GetProjectEvolutionAction;

public class SmeWizard extends Wizard {

	// Wizard Pages
	ScmDescriptionPage scmDescripPage;
	ScmWizardPage scmPage;
	ScmVersionsPage versionsPage;
	ScmSummaryPage summaryPage;

	InfoSCM info;
	// View Reference
	TableViewer tbConnections;
	// flag indicated whether the wizard can be completed or not
	// if the user has set the SCM configuration
	protected boolean scmCompleted = false;

	public SmeWizard() {
		super();
		info = new InfoSCM();
	}

	public SmeWizard(TableViewer viewer) {
		super();
		info = new InfoSCM();
		tbConnections = viewer;
	}

	public void addPages() {
		scmDescripPage = new ScmDescriptionPage();
		addPage(scmDescripPage);
		scmPage = new ScmWizardPage();
		addPage(scmPage);
//		versionsPage = new ScmVersionsPage();
//		addPage(versionsPage);
//		summaryPage = new ScmSummaryPage();
//		addPage(summaryPage);
	}

	public boolean performCancel() {
		System.out.println("Wizard Cancelado");
		return true;
	}

	@Override
	public boolean performFinish() {
		
		fillInfoData();
		
		saveInfoScm();
		
		// Including new SmeProperties on the viewer.
		tbConnections.add(info);
		return true;
	}

	private void fillInfoData() {
		// TODO Auto-generated method stub
		info.setUrl(scmPage.getIoUrlText().getText());
		info.setUser(scmPage.getIoUrlText().getText());
		info.setPassword(scmPage.getIoPasswordText().getText());
	}

	private List<TreeItem> setVersionsToGet() {
		List<TreeItem> selectedNodes;
		if (this.getContainer().getCurrentPage() == versionsPage) {
			 selectedNodes = versionsPage.getOrderingVersions();
			 info.getSme().setMainPath(versionsPage.getMainPath());
		} else {
			selectedNodes = summaryPage.getOrderingVersions();
		}
		
		List<Version> nameVersions = new ArrayList<Version>();
		int i = 1;
		for(TreeItem item: selectedNodes ){
			nameVersions.add(new Version(item.getText(),i));
			i++;
		}
		info.getSme().setVersions(nameVersions);
		return selectedNodes;
	}

	private void downloadVersions(InfoSCM info, SCM scm,
			List<TreeItem> selectedNodeOnTree) {
		SCMAccessMain.setFolderExport(info.getSme().getMainPath()); 
		for (int i = 0; i < selectedNodeOnTree.size(); i++) {
			TreeItem ti = (TreeItem) selectedNodeOnTree.get(i);
			Node currentNode = (Node) ti.getData(); // listOfNodes.add(currentNode);
			System.out.println("############\n" + "Download Started: Name: "
					+ currentNode.getName() + " - " + "Path: "
					+ currentNode.getPath() + " - " + "Revision: "
					+ currentNode.getRevision());
			try {
				scm.exportPath(currentNode, info.getSme().getMainPath()+"/"+info.getSme()+"/");
				SCMAccessMain.addExportedNode(currentNode);
			} catch (Exception e1) {
				System.out.println("Error while downloading...\n"
						+ e1.getMessage());
			}
			System.out.println("############\n" + "Download Finished: Name: "
					+ currentNode.getName() + " - " + "Path: "
					+ currentNode.getPath());
		}
		GetProjectEvolutionAction.processAllProject();
	}

	private void saveInfoScm() {
		
		InfoScmDao infoDao;
		try {
			infoDao = new InfoScmDao();
			infoDao.save(info);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean canFinish() {
		// cannot complete the wizard from the first page
		if (this.getContainer().getCurrentPage() == scmPage) {
			return true;
		} else
		{
			return false;
		}
	}
}
