package scmplugin.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import scmaccess.model.objects.InfoSCM;

class ScmSummaryPage extends WizardPage implements Listener {

	private static final String PAGE_Title = "Repository Summary";
	// Widgets
	private ExpandBar expInfo;
	private Label scmText;
	private Label urlText;
	private Label userText;
	private Composite infoComposite;
	private Composite versionComposite;
	private Combo[] comboOrder;

	// Error: Two or more versions have the same ordering number.
	private IStatus orderNumber;
	private List<TreeItem> versions;
	
	protected ScmSummaryPage() {
		super("Page 4", PAGE_Title, null);
		setDescription("Repository settings summary");
		orderNumber = new Status(IStatus.OK, "not_used", 0, "", null);
		versions = new ArrayList<TreeItem>();
	}

	@Override
	public void createControl(Composite parent) {
		// Expand Info
		expInfo = new ExpandBar(parent, SWT.V_SCROLL);

		GridLayout layoutBar1 = new GridLayout(2, false);
		layoutBar1.marginLeft = layoutBar1.marginTop = layoutBar1.marginRight = layoutBar1.marginBottom = 10;
		layoutBar1.verticalSpacing = 10;
		// Composite Info
		infoComposite = new Composite(expInfo, SWT.NONE);
		infoComposite.setLayout(layoutBar1);

		// SCM
		Label scm = new Label(infoComposite, SWT.CENTER);
		scm.setText("Scm:");
		scmText = new Label(infoComposite, SWT.CENTER);

		// URL
		Label urlScm = new Label(infoComposite, SWT.CENTER);
		urlScm.setText("Url:");
		urlText = new Label(infoComposite, SWT.CENTER);

		// USER
		Label userScm = new Label(infoComposite, SWT.CENTER);
		userScm.setText("User:");
		userText = new Label(infoComposite, SWT.CENTER);

		// Bar Expanded
		ExpandItem item0 = new ExpandItem(expInfo, SWT.NONE, 0);
		item0.setText("SCM Settings Summary");
		item0.setHeight(infoComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(infoComposite);

		GridLayout layoutBar2 = new GridLayout(2, false);
		layoutBar2.marginLeft = layoutBar2.marginTop = layoutBar2.marginRight = layoutBar2.marginBottom = 10;
		layoutBar2.verticalSpacing = 7;

		versionComposite = new Composite(expInfo, SWT.NONE);
		versionComposite.setLayout(layoutBar2);

		ExpandItem item1 = new ExpandItem(expInfo, SWT.NONE, 0);
		item1.setText("Versions Order");
		item1.setControl(versionComposite);
		item1.setHeight(400);

		item0.setExpanded(true);

		expInfo.setSpacing(8);

		setControl(expInfo);

	}

	public void setSummaryGroup(InfoSCM info) {
		// SCM
		scmText.setText(info.getSCScmType());
		// URL
		urlText.setText(info.getUrl());
		// USER
		userText.setText(info.getUser());
		infoComposite.layout();
	}

	@Override
	public void handleEvent(Event event) {
		Combo combSelected = (Combo)event.widget;
		if(isNotRightVersionSelected(combSelected)){
			orderNumber = new Status(IStatus.ERROR, "notUsed", 0,
					"Two or more  versions have the same ordering number ", null);	
		}
		else{
			orderNumber = new Status(IStatus.OK, "not_used", 0, "", null);
		}
		applyToStatusLine();
		getContainer().updateButtons();
	}

	

	private boolean isNotRightVersionSelected(Combo selected) {
		boolean result = false;
		for(Combo comb: comboOrder){
			if((comb!= selected)&&(comb.getSelectionIndex()==selected.getSelectionIndex())){
				result = true;
				break;
			}
		}
		return result;
	}

	public boolean canFlipToNextPage() {
		// no next page for this path through the wizard
		return false;
	}

	public void setListVersions(ArrayList<TreeItem> selectedNodeOnTree) {
		comboOrder = new Combo[selectedNodeOnTree.size()];
		for (int i = 0; i < selectedNodeOnTree.size(); i++) {
			Label t1 = new Label(versionComposite, SWT.NONE);
			t1.setText(selectedNodeOnTree.get(i).getText());
			Combo combo = new Combo(versionComposite, SWT.READ_ONLY);
			combo.setItems(comboVersionSelection(comboOrder.length));
			combo.select(i);
			combo.setSize(200, 200);
			combo.addListener(SWT.Selection, this);
			comboOrder[i] = combo;
		}
		versionComposite.layout();
		versions = selectedNodeOnTree;
	}

	private String[] comboVersionSelection(int size) {
		if (size <= 0) {
			return null;
		} else {
			String array[] = new String[size];
			for (int i = 0; i < size; i++) {
				array[i] = Integer.toString(i + 1);
			}
			return array;
		}
	}
	
	public boolean canFinish(){
		if (getErrorMessage() != null) 
			return false;
		else
			return true;
	}
	
	private void applyToStatusLine() {
		String message = orderNumber.getMessage();
		if (message.length() == 0)
			message = null;
		switch (orderNumber.getSeverity()) {
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
	
	public List<TreeItem> getOrderingVersions(){
		TreeItem[] array =  new TreeItem[versions.size()];
		Combo comb;
		int index;
		List<TreeItem> result = new ArrayList<TreeItem>();
		for(int i = 0; i< comboOrder.length ;i++){
			comb = comboOrder[i];
			index = comb.getSelectionIndex();
			array[index] = versions.get(i);
		}
		for(TreeItem item: array){
			result.add(item);
		}
		return result;
	}
}
