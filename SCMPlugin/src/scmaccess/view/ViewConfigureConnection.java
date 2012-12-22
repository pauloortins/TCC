package scmaccess.view;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import dubaj.internacionalizacao.InternationalResource;

import scmaccess.model.SCM;
import scmaccess.model.SubVersion;
import scmaccess.model.exceptions.SCMConnectionRefusedException;
import scmaccess.model.objects.InfoSCM;


public class ViewConfigureConnection extends AbstractForm {
	
	private Text ioUrlText;
	private Text ioUserText;
	private Text ioPasswordText;
	private SCM scm;
	private String urlTest = "file://c:/Users/paulo/RepositorioTesteDubaJ";
	private String userTest = "paulo";
	private String passwordTest = "";
	
	public ViewConfigureConnection(){
		super(InternationalResource.getIdioma().getConfigureConnection());
		this.initComponents();
		this.setInitialValues();
	}
	
	private void initComponents() {
		
		//SCM Type
		Label loSCMLabel = new Label(this.ioShell, SWT.SINGLE);
		loSCMLabel.setText(InternationalResource.getIdioma().getSCM());
		FormData loDataSCMLabel = new FormData();
		loSCMLabel.setLayoutData(loDataSCMLabel);
		
		Label loSVNLabel = new Label(this.ioShell,SWT.SINGLE);
		loSVNLabel.setText("SubVersion");
		FormData loDataCboSCM = new FormData(250, 16);
		loDataCboSCM.left = new FormAttachment(loSCMLabel, 50);
		loSVNLabel.setLayoutData(loDataCboSCM);
		// URL
		Label loUrlLabel = new Label(this.ioShell, SWT.SINGLE);
		loUrlLabel.setText(InternationalResource.getIdioma().getURL());
		FormData loDataUrlLabel = new FormData();
		loDataUrlLabel.top = new FormAttachment(loSCMLabel, 20);
		loUrlLabel.setLayoutData(loDataUrlLabel);
		this.ioUrlText = new Text(this.ioShell, SWT.BORDER);
		FormData loDataUrlText = new FormData(300, 16);
		loDataUrlText.top = new FormAttachment(loSCMLabel, 20);
		loDataUrlText.left = new FormAttachment(loSCMLabel, 50);
		this.ioUrlText.setLayoutData(loDataUrlText);
		// User
		Label lblUser = new Label(this.ioShell, SWT.SINGLE);
		lblUser.setText(InternationalResource.getIdioma().getUser());
		FormData loDataUserLabel = new FormData();
		loDataUserLabel.top = new FormAttachment(loUrlLabel, 20);
		lblUser.setLayoutData(loDataUserLabel);
		this.ioUserText = new Text(this.ioShell, SWT.BORDER);
		FormData loDataUserText = new FormData(300, 16);
		loDataUserText.top = new FormAttachment(loUrlLabel, 20);
		loDataUserText.left = new FormAttachment(loSCMLabel,50);
		this.ioUserText.setLayoutData(loDataUserText);
		// Password
		Label loPasswordLabel = new Label(this.ioShell, SWT.SINGLE);
		loPasswordLabel.setText(InternationalResource.getIdioma().getPassword());
		FormData loDataPasswordLabel = new FormData();
		loDataPasswordLabel.top = new FormAttachment(lblUser, 20);
		loPasswordLabel.setLayoutData(loDataPasswordLabel);
		this.ioPasswordText = new Text(this.ioShell, SWT.BORDER | SWT.PASSWORD);
		FormData loDataPasswordText = new FormData(300, 16);
		loDataPasswordText.top = new FormAttachment(lblUser, 20);
		loDataPasswordText.left = new FormAttachment(loSCMLabel,50);
		this.ioPasswordText.setLayoutData(loDataPasswordText);
		// Test connection
		Button butDoConnection = new Button(this.ioShell, SWT.BUTTON1);
		butDoConnection.setText(InternationalResource.getIdioma().getConnect());
		butDoConnection.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				final InfoSCM loInfo = getNewInfoSCM();
				try {
						
						//ioShell.dispose();
						ProgressMonitorDialog dialog = new ProgressMonitorDialog(ioShell);
						dialog.run(true, true, new IRunnableWithProgress(){
						    public void run(IProgressMonitor monitor) {
						        monitor.beginTask("Connecting to the repository ...", 100);
						        // execute the task ...
						        try {
						        	scm = new SubVersion();
									scm.connect(loInfo.getUrl(), loInfo.getUser(), loInfo.getPassword());
									
								} catch (SCMConnectionRefusedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
						        monitor.done();
						    }
						});
						
						showMessage(InternationalResource.getIdioma().getConnectionOK());
						
						new ViewSelectVersions(ioShell, scm).show();
				} catch (Exception e) { 
					showMessage(InternationalResource.getIdioma().getConnectionRefused());
				}
			}
		});
		FormData loDataDoConnection = new FormData(150, 25);
		loDataDoConnection.top = new FormAttachment(this.ioPasswordText, 20);
		loDataDoConnection.left = new FormAttachment(loPasswordLabel, 75);
		butDoConnection.setLayoutData(loDataDoConnection);
		
	}
	
	private void setInitialValues(){
		/*InfoBugTracker loInfo = InfoBugTracker.getInstance();*/
		this.ioUrlText.setText(this.urlTest);
		this.ioUserText.setText(this.userTest);
		this.ioPasswordText.setText(this.passwordTest);
	}
	
	/*@Override
	protected void save() throws Exception {
		InfoBugTracker loInfoBugTracker = this.getNewInfoBugTracker();
		loInfoBugTracker.saveFile();
	}
*/
	private InfoSCM getNewInfoSCM(){
		InfoSCM loInfoSCM = new InfoSCM();
		loInfoSCM.setUrl(this.ioUrlText.getText());
		loInfoSCM.setUser(this.ioUserText.getText());
		loInfoSCM.setPassword(this.ioPasswordText.getText());
		return loInfoSCM;
	}
}

