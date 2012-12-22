package scmplugin.wizard;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import scmaccess.model.SCM;
import scmaccess.model.SubVersion;
import scmaccess.model.objects.InfoSCM;

class ScmWizardPage extends WizardPage implements Listener {

	public static final String PAGE_Title = "Repository Settings";

	// Widgets
	private Combo cboSCM;
	private Text ioUrlText;
	private Text ioUserText;
	private Text ioPasswordText;

	public Text getIoUrlText() {
		return ioUrlText;
	}

	public Text getIoUserText() {
		return ioUserText;
	}

	public Text getIoPasswordText() {
		return ioPasswordText;
	}

	// Default Configuration
	private String urlTest = "file://c:/Users/paulo/svnrepositorio";
	private String userTest = "";
	private String passwordTest = "";
	private Button checkStoreButton;
	
	// status variable for the possible errors on this page
	// scmConnection holds an error if it occurs some error in
	// the network or configuration.
	private IStatus scmConnection;

	SCM scm;

	public ScmWizardPage() {
		super("Page 2", PAGE_Title, null);
		setDescription("Setting SCM Configuration");
		scmConnection = new Status(IStatus.OK, "not_used", 0, "", null);
	}

	@Override
	public void createControl(Composite parent) {
		Composite topLevel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);

		topLevel.setLayout(layout);

		Label l = new Label(topLevel, SWT.CENTER);
		l.setText("SCM:");

		this.cboSCM = new Combo(topLevel, SWT.READ_ONLY | SWT.CENTER);
		this.cboSCM.setItems(new String[] { "Subversion"});
		this.cboSCM.select(0);

		Label loUrlLabel = new Label(topLevel, SWT.LEFT);
		loUrlLabel.setText("Url:");

		this.ioUrlText = new Text(topLevel, SWT.BORDER | SWT.LEFT);
		GridData urlData = new GridData(300, 16);
		this.ioUrlText.setLayoutData(urlData);

		Label lblUser = new Label(topLevel, SWT.LEFT);
		lblUser.setText("User:");

		this.ioUserText = new Text(topLevel, SWT.BORDER | SWT.LEFT);
		GridData userData = new GridData(150, 16);
		this.ioUserText.setLayoutData(userData);

		Label loPasswordLabel = new Label(topLevel, SWT.LEFT);
		loPasswordLabel.setText("Password:");

		this.ioPasswordText = new Text(topLevel, SWT.BORDER | SWT.PASSWORD);
		GridData loDataPasswordText = new GridData(150, 16);
		this.ioPasswordText.setLayoutData(loDataPasswordText);
		
		checkStoreButton = new Button(topLevel, SWT.CHECK);
		checkStoreButton.setText("Do you want to store your password ?");
		
		createLine(topLevel, 2);
		setInitialValues();
		setControl(topLevel);
		addListeners();
	}

	private void createLine(Composite parent, int ncol) {
		Label line = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL
				| SWT.BOLD);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = ncol;
		line.setLayoutData(gridData);
	}

	private void setInitialValues() {
		this.ioUrlText.setText(this.urlTest);
		this.ioUserText.setText(this.userTest);
		this.ioPasswordText.setText(this.passwordTest);
	}

	public IWizardPage getNextPage() {
		if (connectSCM()) {
			ScmVersionsPage page = ((SmeWizard) getWizard()).versionsPage;
			page.fillTree(scm);
			return page;
		}
		ScmWizardPage mainPage = ((SmeWizard) getWizard()).scmPage;
		applyToStatusLine();
		return mainPage;
	}

	public boolean canFlipToNextPage() {
		return false;
	}

	private boolean connectSCM() {
		try {
			tryConnectSCM();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			scmConnection = new Status(IStatus.ERROR, "notUsed", 0,
					"Connection Failure", null);
			return false;
		}
	}

	private void tryConnectSCM() throws Exception {
		String url = this.ioUrlText.getText();
		String user = this.ioUserText.getText();
		String password = this.ioPasswordText.getText();
		int opcSCM = this.cboSCM.getSelectionIndex();
		switch (opcSCM) {
		case 0:
			scm = new SubVersion();
			scm.connect(url, user, password);
			InfoSCM info = ((SmeWizard)getWizard()).info;
			info.setSCMTypes(opcSCM);
			info.setUrl(url);
			info.setUser(user);
			if(checkStoreButton.getSelection()){
				info.setPassword(password);
			}			
			break;
		default:
			throw new Exception("Not Implemented Yet");
		}

	}

	private static boolean isTextNonEmpty(Text t) {
		String s = t.getText();
		if ((s != null) && (s.trim().length() > 0))
			return true;
		return false;
	}

	private void addListeners() {
		this.ioUrlText.addListener(SWT.KeyUp, this);
		this.ioUserText.addListener(SWT.KeyUp, this);
		this.ioPasswordText.addListener(SWT.KeyUp, this);
	}

	@Override
	public void handleEvent(Event event) {
		if ((event.widget == ioUrlText) || (event.widget == ioUserText)
				|| (event.widget == ioPasswordText)) {
			getWizard().getContainer().updateButtons();
		}
		scmConnection = new Status(IStatus.OK, "not_used", 0, "", null);
		applyToStatusLine();
	}

	private void applyToStatusLine() {
		String message = scmConnection.getMessage();
		if (message.length() == 0)
			message = null;
		switch (scmConnection.getSeverity()) {
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

}
