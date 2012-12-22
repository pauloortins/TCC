package scmaccess.model;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Utility {
	public static void showMessage(Shell shell, String msn){
		Shell dialog = new Shell(shell);
		dialog.setText("Message");
		dialog.setSize(400, 200);
		GridLayout gl = new GridLayout();
        gl.numColumns = 1;
        dialog.setLayout(gl);
		Label labelPassword = new Label(dialog, SWT.NONE);
		labelPassword.setText(msn+"     \n\n\n");
		dialog.open();
		dialog.pack();
		//MessageDialog.openInformation(shell, "", msn);
	}

}
