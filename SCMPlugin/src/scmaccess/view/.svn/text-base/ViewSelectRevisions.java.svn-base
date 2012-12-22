package scmaccess.view;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import scmaccess.model.SCM;
import scmaccess.model.Utility;
import scmaccess.model.objects.RevisionItem;

public class ViewSelectRevisions {

	private SCM scm;
	private Shell shell = null;
	Display display = Display.getCurrent();

	private Label lTables = null;
	//private Table table = null;
	private Table table = null;

	String Table = null;
	Vector<String> tables = new Vector<String>();

	public ViewSelectRevisions(SCM scm) {
		this.scm = scm;
	}
	public void open() {
		createShell();
	}

	private void createShell(){
		Display d = display;
		Shell s = new Shell(d);
        
        s.setSize(550,500);
        
        s.setText("A Table Shell Example");
        GridLayout gl = new GridLayout();
        gl.numColumns = 4;
        s.setLayout(gl);
        
        table = new Table(s, SWT.BORDER | SWT.CHECK | 
            SWT.MULTI | SWT.FULL_SELECTION);
        final GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 4;
        table.setLayoutData(gd);
        table.setHeaderVisible(true);
        final TableColumn column1 = new TableColumn(table, SWT.LEFT);
        column1.setText("Revision Number");
        final TableColumn column2 = new TableColumn(table, SWT.LEFT);
		column2.setText("Author");
		final TableColumn column3 = new TableColumn(table, SWT.LEFT);
		column3.setText("Date");
		final TableColumn column4 = new TableColumn(table, SWT.LEFT);
		column4.setText("Log Message");
		
		column1.setWidth(70);
		column2.setWidth(70);
		column3.setWidth(180);
		column4.setWidth(480);
        
		 /*final TableColumn tc2 = new TableColumn(t, SWT.CENTER);
        final TableColumn tc3 = new TableColumn(t, SWT.CENTER);
        final TableColumn tc4 = new TableColumn(t, SWT.CENTER);*/
		/*tc2.setWidth(70);
        tc3.setWidth(80);*/
        /*for (int i = 0 ; i < 15; i++){
	        final TableItem item1 = new TableItem(t,SWT.NONE);
	        item1.setText(new String[] {"Tim","Hatton","Kentucky"});
	        final TableItem item2 = new TableItem(t,SWT.NONE);
	        item2.setText(new String[] {"Caitlyn","Warner","Ohio"});
	        final TableItem item3 = new TableItem(t,SWT.NONE);
	        item3.setText(new String[] {"Reese","Miller","Ohio"});
        }*/
		LinkedList<RevisionItem> revisionList = scm.getSortedListOfRevisions();

		for (int i = 0; i < revisionList.size(); i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			RevisionItem ri = revisionList.get(i);
			item.setText(0, String.valueOf(ri.getRevisionNumber()));
			item.setText(1, ri.getAuthor().getName());
			item.setText(2, String.valueOf(ri.getDate()));
			item.setText(3, ri.getLogMessage());
		}
        
        /*final Text find = new Text(s, SWT.SINGLE | SWT.BORDER);
        final Text replace = new Text(s, SWT.SINGLE | SWT.BORDER);*/
        final Button btnExport = new Button(s, SWT.BORDER | SWT.PUSH);
        btnExport.setText("Export");
        btnExport.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                TableItem[] tia = table.getItems();
                
                for(int i = 0; i<tia.length;i++)
                {
                    /*if (tia[i].getText(2).equals(find.getText()))
                    {
                        tia[i].setText(2, replace.getText());
                    }*/
                	if (tia[i].getChecked()){
                		try {
							scm.exportRevision(Long.parseLong(tia[i].getText(0)), "export");
						} catch (Exception e1) {
							String messageError = "Export for '"+scm.url+scm.selectedDirectory+"' in revision '"+tia[i].getText(0)+"' failed.";
							
							messageError += "\n"+e1.getMessage();
							Utility.showMessage(shell,messageError);
							
						}
                	}
                    
                }
            }
        });
               
        s.open();
        while(!s.isDisposed()){
            if(!d.readAndDispatch())
                d.sleep();
        }
        d.dispose();
		
	}
	
	
	


}