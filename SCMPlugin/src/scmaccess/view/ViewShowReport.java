package scmaccess.view;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JFileChooser;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.themes.ColorUtil;
import org.eclipse.swt.widgets.Button;

import dubaj.internacionalizacao.InternationalResource;
import dubaj.tr.CSVFile;
import dubaj.tr.FileFilterCSV;
import dubaj.tr.FileViewCSV;
import dubaj.tr.MudancaClasseVO;
import dubaj.tr.RelatorioFinal;
import dubaj.tr.contagem.metodo.RelatorioMudancasClasse;

import scmaccess.model.Node;

public class ViewShowReport extends AbstractForm {
	
	private Tree reportTree;
	private JFileChooser fileChooser;
	
	private final org.eclipse.swt.graphics.Color VERMELHO = new org.eclipse.swt.graphics.Color(null, 255, 0, 0);
	private final org.eclipse.swt.graphics.Color AMARELO = new org.eclipse.swt.graphics.Color(null, 255, 215, 0);
	private final org.eclipse.swt.graphics.Color VERDE = new org.eclipse.swt.graphics.Color(null, 153, 205, 50);
	
	public ViewShowReport() {
		super(InternationalResource.getIdioma().getDubaJReport());
		this.InitComponents();
	}

	private void InitComponents() {
		
		// Label Tree
		Label lblTree = new Label(this.ioShell, SWT.SINGLE);
		lblTree.setText(InternationalResource.getIdioma().getReport());
		FormData loDataTreeLabel = new FormData();
		
		lblTree.setLayoutData(loDataTreeLabel);
		
		// Results Tree
		this.reportTree = new Tree(this.ioShell, SWT.BORDER);
		FormData loDataScmTree = new FormData(800, 600);
		
		loDataScmTree.left = new FormAttachment(lblTree, 40);
		this.reportTree.setLayoutData(loDataScmTree);
		fillTree();
		
		// Save Button
		Button fileChooserButton = new Button(ioShell, SWT.BUTTON1);
		fileChooserButton.setText(InternationalResource.getIdioma().getExportToCSV());
		
		FormData saveButtonForm = new FormData();
		saveButtonForm.top = new FormAttachment(reportTree, 20);
		
		fileChooserButton.setLayoutData(saveButtonForm);
		
		// Adiciona clique do botao
		fileChooserButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				
				fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileFilterCSV());
				
				File defaultFile = new File("M�tricas.csv");
				fileChooser.setSelectedFile(defaultFile);
				
				int returnVal = fileChooser.showSaveDialog(fileChooser);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            
	            	CSVFile file = new CSVFile(fileChooser.getSelectedFile().getAbsolutePath());
	            	file.generateCsvFile();
	            	showMessage(InternationalResource.getIdioma().getFileSaveSucessful());
	            } 
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void fillTree()
	{
		this.reportTree.setRedraw(false);
		float mediaMudancas = calcularMediaMudancasClasse();
		
		ArrayList<RelatorioMudancasClasse> listaClasses = new ArrayList<RelatorioMudancasClasse>(RelatorioFinal.getMapMudancas().values());
		
		// Ordena as classes pra aquela na ordem descendente de acordo com o numero de mudancas
		Collections.sort(listaClasses, new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				
				RelatorioMudancasClasse m1 = (RelatorioMudancasClasse) o1;
				RelatorioMudancasClasse m2 = (RelatorioMudancasClasse) o2;
				
				if (m1.getNumMudancasTotal() > m2.getNumMudancasTotal()) {
					return -1;
				} else if (m1.getNumMudancasTotal() < m2.getNumMudancasTotal()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		
		for (RelatorioMudancasClasse mudancaClasse : listaClasses) {
			
			// Cria n� primario com o nome da classe seguido do numero de alteracoes
			TreeItem item = new TreeItem(reportTree, SWT.NONE);
			item.setText(mudancaClasse.getNomClasse() + ": " + mudancaClasse.getNumMudancasTotal());
			setarBackground(item, mudancaClasse.getNumMudancasTotal(), mediaMudancas);
			
			// Numero de mudancas outras
			TreeItem itemMudancasOutras = new TreeItem(item, SWT.NONE);
			itemMudancasOutras.setText(InternationalResource.getIdioma().getClassesChanges() + mudancaClasse.getNumMudancasClasse());
			
			// Numero de mudancas nos metodos
			TreeItem itemMudancasMetodos = new TreeItem(item, SWT.NONE);
			itemMudancasMetodos.setText(InternationalResource.getIdioma().getMethodsChanges() + mudancaClasse.getNumMudancasMetodo());
			
			// Numero de mudancas nos atributos
			TreeItem itemMudancasAtributo = new TreeItem(item, SWT.NONE);
			itemMudancasAtributo.setText(InternationalResource.getIdioma().getAttributesChanges() + mudancaClasse.getNumMudancasAtributo());
			
			// Code Churn
			TreeItem itemMudancasCodeChurn = new TreeItem(item, SWT.NONE);
			itemMudancasCodeChurn.setText(InternationalResource.getIdioma().getDeltaLOC() + mudancaClasse.getNumCodeChurn());
			
			TreeItem itemMudancasLinesOfCode = new TreeItem(item, SWT.NONE);
			itemMudancasLinesOfCode.setText(InternationalResource.getIdioma().getLinesOfCode() + mudancaClasse.getNumLinesOfCode());
			
			TreeItem itemMudancasCodeChurnRelativo = new TreeItem(item, SWT.NONE);
			itemMudancasCodeChurnRelativo.setText(InternationalResource.getIdioma().getDeltaLOCRelativo() + mudancaClasse.getNumCodeChurnRelativo());
			
			// Numero de Commits
			TreeItem itemNumeroCommits = new TreeItem(item, SWT.NONE);
			itemNumeroCommits.setText(InternationalResource.getIdioma().getNumberOfCommits() + mudancaClasse.getNumCommits());
		}
		
		this.reportTree.setRedraw(true);
	}
	
	private int somarMudancasHash(HashMap<String,Integer> hash) {
		int somatorio = 0;
		
		for (int numMudanca : hash.values()) {
			somatorio += numMudanca;
		}
		
		return somatorio;
	}
	
	private float calcularMediaMudancasClasse() {
		int somatorioMudancas = 0;
		for (String key : RelatorioFinal.getMapMudancas().keySet()) {
			somatorioMudancas += RelatorioFinal.getMapMudancas().get(key).getNumMudancasTotal();
		}
		
		return (float) somatorioMudancas / RelatorioFinal.getMapMudancas().size();
	}
	
	private void setarBackground(TreeItem no,int numMudancas, float mediaMudancas) {
		
		if (numMudancas > (1.25 * mediaMudancas)) {
			no.setBackground(VERMELHO);
		} else if (numMudancas < (0.75 * mediaMudancas)) {
			no.setBackground(VERDE);
		}
		else {
			no.setBackground(AMARELO);
		}
	}
	
	private float calcularMediaMudancasMetodoConstrutor(HashMap<String, Integer> hash) {
		int somatorio = 0;
		
		for (String key : hash.keySet()) {
			somatorio += hash.get(key);
		}
		
		return (float) somatorio / hash.size();
	}
}
