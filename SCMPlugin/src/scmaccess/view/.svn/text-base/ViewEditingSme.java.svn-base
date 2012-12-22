package scmaccess.view;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import scmaccess.model.objects.InfoSCM;
import scmaccess.model.objects.Version;
import scmplugin.persistence.InfoScmDao;

//class RowSaveCancel extends Composite {
//	final Button saveBtn;
//	final Button cancelBtn;
//
//	public RowSaveCancel(Composite c, final Shell tela) {
//		super(c, SWT.NO_FOCUS);
//		RowLayout rl = new RowLayout();
//		rl.wrap = false;
//		rl.pack = false;
//		this.setLayout(rl);
//		saveBtn = new Button(this, SWT.BORDER | SWT.PUSH);
//		saveBtn.setText("Save");
//		saveBtn.setSize(30, 20);
//		saveBtn.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//
//				tela.dispose();
//			}
//		});
//		cancelBtn = new Button(this, SWT.BORDER | SWT.PUSH);
//		cancelBtn.setText("Cancel");
//		cancelBtn.setSize(30, 20);
//		cancelBtn.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				tela.dispose();
//			}
//		});
//	}
//}

public class ViewEditingSme implements Listener {

	private Shell tela;
	private TabFolder tabs;
	private Composite main;
	private Text txtNameSme;
	private Combo[] combos;
	private Button saveBtn;
	private Button cancelBtn;

	public ViewEditingSme(Display current, final InfoSCM info, final TableViewer viewer) {
		tela = new Shell(current, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		tela.setText("Editing Sme Settings");
		tela.setSize(450, 450);

		// Shell Composite
		main = new Composite(tela, SWT.None);
		GridLayout layout = new GridLayout();
		main.setLayout(layout);

		// Tab Widget
		tabs = new TabFolder(main, SWT.BORDER);

		// Sme Tab
		TabItem tabSme = new TabItem(tabs, SWT.NONE);
		tabSme.setText("Sme Properties");
		// Sme Tab Composite
		Composite compSme = new Composite(tabs, SWT.NONE);
		compSme.setLayout(new GridLayout(2, false));
		new Label(compSme, SWT.NONE).setText("Name");
		txtNameSme = new Text(compSme, SWT.BORDER | SWT.LEFT);
		txtNameSme.setLayoutData(new GridData(300, 16));
		txtNameSme.setText(info.getSme().getName());
		txtNameSme.addListener(SWT.KeyUp, this);
		createLine(compSme, 2);
		tabSme.setControl(compSme);

		// SCM tab
		TabItem tabScm = new TabItem(tabs, SWT.NONE);
		tabScm.setText("SCM Settings");

		// SCM Tab Composite
		Composite compScm = new Composite(tabs, SWT.NONE);
		compScm.setLayout(new GridLayout(2, false));
		new Label(compScm, SWT.NONE).setText("Scm: ");
		new Label(compScm, SWT.NONE).setText(info.getSCScmType());
		new Label(compScm, SWT.NONE).setText("Url: ");
		new Label(compScm, SWT.NONE).setText(info.getUrl());
		new Label(compScm, SWT.NONE).setText("User: ");
		new Label(compScm, SWT.NONE).setText(info.getUser());
		createLine(compScm, 2);
		tabScm.setControl(compScm);

		// Version Tab
		TabItem tabVersions = new TabItem(tabs, SWT.NONE);
		tabVersions.setText("Versions Settings");

		// SCM Tab Composite
		Composite compVersion = new Composite(tabs, SWT.NONE);
		compVersion.setLayout(new GridLayout(2, false));
		combos = new Combo[info.getSme().getVersions().size()];
		for (int i = 0; i < info.getSme().getVersions().size(); i++) {
			String version = info.getSme().getVersions().get(i).getName();
			new Label(compVersion, SWT.NONE).setText(version);
			Combo comb = new Combo(compVersion, SWT.READ_ONLY);
			comb.setItems(comboVersionSelection(info.getSme().getVersions()
					.size()));
			comb.select(i);
			comb.setSize(200, 200);
			comb.addListener(SWT.Selection, this);
			combos[i] = comb;
		}

		createLine(compVersion, 2);
		tabVersions.setControl(compVersion);

		Composite buttons = new Composite(main, SWT.NO_FOCUS);
		RowLayout rl = new RowLayout();
		rl.wrap = false;
		rl.pack = false;
		buttons.setLayout(rl);

		saveBtn = new Button(buttons, SWT.BORDER | SWT.PUSH);
		saveBtn.setText("Save");
		saveBtn.setSize(30, 20);
		saveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String newNameSme = txtNameSme.getText();
				String oldNameSme = info.getSme().getName(); 
				String mainPath = info.getSme().getMainPath();
				InfoScmDao infoDao = new InfoScmDao();
				InfoSCM newInfo = new InfoSCM();
				newInfo.getSme().setName(newNameSme);
				List<Version> listaVersoes = info.getSme().getVersions();
				int index = 0;
				for(Combo comb: combos){
					listaVersoes.get(index).setOrder(comb.getSelectionIndex());
					index++;
				}
				Collections.sort(listaVersoes);
				newInfo.getSme().setVersions(listaVersoes);
				try {
					infoDao.edit(info, newInfo);
					infoDao.renameDir(mainPath+"/"+oldNameSme, mainPath+"/"+newNameSme);
					viewer.refresh();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				tela.dispose();
			}
		});
		cancelBtn = new Button(buttons, SWT.BORDER | SWT.PUSH);
		cancelBtn.setText("Cancel");
		cancelBtn.setSize(30, 20);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tela.dispose();
			}
		});
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

	private void createLine(Composite parent, int ncol) {
		Label line = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL
				| SWT.BOLD);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = ncol;
		line.setLayoutData(gridData);
	}

	public void show() {
		main.pack();
		tela.pack();
		tela.open();
	}

	@Override
	public void handleEvent(Event event) {
		boolean a,b;
		a= b = false;
		if (txtNameSme == event.widget) {
			if (isTextNonEmpty(txtNameSme)) {
				a = true;
			}
			b = isRightVersionSelected();
		}
		if (txtNameSme != event.widget) {
			if (!isNotRightVersionSelected()) {
				b = true;
			}
			a = isTextEmpty();
		}

		saveBtn.setEnabled(a & b);

	}

	private boolean isTextEmpty() {
		String s = txtNameSme.getText();
		if ((s != null) && (s.trim().length() > 0))
			return true;
		return false;
	}

	private boolean isRightVersionSelected() {
		boolean result = true;
		for (int i = 0; i< combos.length -1; i++ ) {
			for(int j = i+1; j<combos.length; j++){
				if(combos[i].getSelectionIndex()==combos[j].getSelectionIndex() ){
					result = false;
					break;
				}
			}
		}
		return result;
	}

	private boolean isNotRightVersionSelected() {
		boolean result = false;
		for (int i = 0 ; i< combos.length -1;i++) {
			for(int j = i+1; j<combos.length ;j++){
				if (combos[i].getSelectionIndex() == combos[j].getSelectionIndex()){
					result = true;
					break;
				}
			}
			
		}
		return result;
	}

	private static boolean isTextNonEmpty(Text t) {
		String s = t.getText();
		if ((s != null) && (s.trim().length() > 0))
			return true;
		return false;
	}
}
