package scmaccess.view;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractForm {
	protected Shell ioShell;

	public AbstractForm(String asTitle) {
		this.ioShell = new Shell(Display.getCurrent());
		this.ioShell.setText(asTitle);
		FormLayout loFormLayout = new FormLayout();
		loFormLayout.marginLeft = 10;
		loFormLayout.marginTop = 10;
		loFormLayout.marginBottom = 20;
		loFormLayout.marginRight = 20;
		this.ioShell.setLayout(loFormLayout);
	}

	public AbstractForm(String asTitle, Shell parent) {
		this.ioShell = new Shell(parent.getDisplay());
		this.ioShell.setText(asTitle);
		FormLayout loFormLayout = new FormLayout();
		loFormLayout.marginLeft = 10;
		loFormLayout.marginTop = 10;
		loFormLayout.marginBottom = 20;
		loFormLayout.marginRight = 20;
		this.ioShell.setLayout(loFormLayout);
	}

	public AbstractForm() {
		this("");
	}

	private static final long serialVersionUID = 1L;

	// protected abstract void save() throws Exception;

	/*
	 * protected void onSave() { try { this.save();
	 * this.showMessage("Operation successful."); } catch (Throwable loCause) {
	 * System.out.println(loCause.getMessage());
	 * this.showError(loCause.getMessage()); } }
	 */

	public void show() {
		this.ioShell.pack();
		this.ioShell.open();
		while (!ioShell.isDisposed()) {
			if (!Display.getCurrent().readAndDispatch())
				Display.getCurrent().sleep();
		}
		//Display.getCurrent().dispose();
	}

	protected void showError(Throwable aoCause) {
		this.showError(aoCause.getMessage());
	}

	protected void showError(String asMessage) {
		MessageDialog.openError(this.ioShell, "", asMessage);
	}

	protected void showMessage(String asMessage) {
		MessageDialog.openInformation(this.ioShell, "", asMessage);
	}
}
