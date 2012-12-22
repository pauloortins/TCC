package dubaj.testes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import scmaccess.view.AbstractForm;

public class ViewUnitTests extends AbstractForm {

	private static final org.eclipse.swt.graphics.Color VERMELHO = new org.eclipse.swt.graphics.Color(
			null, 255, 0, 0);

	private static final org.eclipse.swt.graphics.Color VERDE = new org.eclipse.swt.graphics.Color(
			null, 153, 205, 50);

	private final int NUMERO_DE_TESTES_UNITARIOS = 56;	
	private final int NUMERO_DE_TESTES_INTEGRADOS = 3;
	private final int NUMERO_DE_TESTES_SISTEMA = 5;
	
	private Tree reportTree;

	public ViewUnitTests() {
		super("Tests Report");
		this.InitComponents();
	}

	private void InitComponents() {
		// TODO Auto-generated method stub
		reportTree = new Tree(ioShell, SWT.BORDER);

		FormData loTestsResult = new FormData();

		Label lblTestsResult = new Label(ioShell, SWT.NONE);

		FormData loTestsTree = new FormData(800, 450);
		loTestsTree.top = new FormAttachment(lblTestsResult, 20);
		this.reportTree.setLayoutData(loTestsTree);

		reportTree.addListener(SWT.ALL, new Listener() {

			@Override
			public void handleEvent(Event event) {

			}
		});

		reportTree.setRedraw(false);
		
		boolean testSucessUnitarios = rodarTestesUnitarios();
		boolean testSucessIntegrados = rodarTestesIntegrados();
		boolean testSucessSistema = rodarTestesSistema();
		
		if (testSucessUnitarios && testSucessIntegrados && testSucessSistema) {
			lblTestsResult.setText("TESTS SUCCESS");
			lblTestsResult.setBackground(VERDE);
		} else {
			lblTestsResult.setText("TESTS FAILED");
			lblTestsResult.setBackground(VERMELHO);
		}

		lblTestsResult.setLayoutData(loTestsResult);

		reportTree.setRedraw(true);
	}

	private boolean rodarTestesSistema() {
		
		// TODO Auto-generated method stub
		TreeItem itemCabecalho = new TreeItem(reportTree, SWT.NONE);
		
		MainTesteSistema.SetUp();
		
		boolean testSuccess = true;
		for (int numeroTeste = 1; numeroTeste <= NUMERO_DE_TESTES_SISTEMA; numeroTeste++) {
			if (!MainTesteSistema.rodarTeste(numeroTeste, itemCabecalho)) {
				testSuccess = false;
			}
		}
		
		if (testSuccess) {
			itemCabecalho.setText("Testes Sistema - OK");
			itemCabecalho.setBackground(VERDE);
		} else {
			itemCabecalho.setText("Testes Sistema - FAIL");
			itemCabecalho.setBackground(VERMELHO);
		}
		
		return testSuccess;
	}

	private boolean rodarTestesUnitarios() {
		// TODO Auto-generated method stub
		
		TreeItem itemCabecalho = new TreeItem(reportTree, SWT.NONE);
		itemCabecalho.setText("Testes Unitarios");
		
		boolean testSuccess = true;
		for (int numeroTeste = 56; numeroTeste <= NUMERO_DE_TESTES_UNITARIOS; numeroTeste++) {
			if (!MainTesteUnitario.rodarTeste(numeroTeste, itemCabecalho)) {
				testSuccess = false;
			}
		}
		
		if (testSuccess) {
			itemCabecalho.setText("Testes Unitarios - OK");
			itemCabecalho.setBackground(VERDE);
		} else {
			itemCabecalho.setText("Testes Unitarios - FAIL");
			itemCabecalho.setBackground(VERMELHO);
		}

		return testSuccess;
	}

	private boolean rodarTestesIntegrados() {
		
		TreeItem itemCabecalho = new TreeItem(reportTree, SWT.NONE);
		itemCabecalho.setText("Testes Unitarios");
		
		boolean testSuccess = true;
		for (int numeroTeste = 1; numeroTeste <= NUMERO_DE_TESTES_INTEGRADOS; numeroTeste++) {
			if (!MainTesteIntegrado.rodarTeste(numeroTeste, itemCabecalho)) {
				testSuccess = false;
			}
		}
		
		if (testSuccess) {
			itemCabecalho.setText("Testes Integrados - OK");
			itemCabecalho.setBackground(VERDE);
		} else {
			itemCabecalho.setText("Testes Integrados - FAIL");
			itemCabecalho.setBackground(VERMELHO);
		}
		
		return testSuccess;
	}

	public static void main(String[] args) {
		ViewUnitTests view = new ViewUnitTests();
		view.show();
	}
}
