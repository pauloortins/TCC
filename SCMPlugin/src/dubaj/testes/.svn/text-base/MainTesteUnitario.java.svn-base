package dubaj.testes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import diffj.org.incava.diffj.DiffJ;
import diffj.org.incava.qualog.Qualog;
import dubaj.tr.Relatorio;
import dubaj.tr.RelatorioFinal;

public class MainTesteUnitario {

	private static final org.eclipse.swt.graphics.Color VERMELHO = new org.eclipse.swt.graphics.Color(
			null, 255, 0, 0);
	
	private static final org.eclipse.swt.graphics.Color VERDE = new org.eclipse.swt.graphics.Color(
			null, 153, 205, 50);

	final static String FOLDER_PATH = "D:\\Faculdade\\TCC\\TestesUnitarios";
	final static String NOME_VERSAO_ANTIGA = "ClasseAntiga.java";
	final static String NOME_VERSAO_NOVA = "ClasseNova.java";
	final static String NOME_RESULTADO_ESPERADO = "ResultadoEsperado.txt";
	final static String NOME_DESCRICAO = "Descricao.txt";

	public static boolean rodarTeste(int numeroDoTeste, TreeItem treeItem) {
		boolean testSucess = true;
		
		String descricao = obterDescricaoTeste(numeroDoTeste);
		String resultadoEsperado = obterResultadoEsperado(numeroDoTeste);
		String resultadoObtido = obterResultadoDiffJ(numeroDoTeste);

		TreeItem itemCabecalho = new TreeItem(treeItem, SWT.NONE);

		if (resultadoEsperado.equals(resultadoObtido)) {
			itemCabecalho.setText("Case " + numeroDoTeste + ": OK");
			itemCabecalho.setBackground(VERDE);
			
			testSucess = true;
		} else {
			itemCabecalho.setText("Case " + numeroDoTeste + ": ERROR");
			itemCabecalho.setBackground(VERMELHO);
			
			testSucess = false;
		}
		
		TreeItem noDescricao = new TreeItem(itemCabecalho, SWT.NONE);
		noDescricao.setText("Descrição:");
		
		formatarParaExibicao(descricao, noDescricao);
		
		TreeItem noResultadoEsperado = new TreeItem(itemCabecalho, SWT.NONE);
		noResultadoEsperado.setText("Resultado Esperado:");

		formatarParaExibicao(resultadoEsperado, noResultadoEsperado);
		
		TreeItem noResultadoObtido = new TreeItem(itemCabecalho, SWT.NONE);
		noResultadoObtido.setText("Resultado Obtido:");
		
		formatarParaExibicao(resultadoObtido, noResultadoObtido);
		
		return testSucess;
	}

	private static void formatarParaExibicao(String resultado, TreeItem item) {
		// TODO Auto-generated method stub
		String[] listaStrings = resultado.split(",");
		
		for (String string : listaStrings) {
			TreeItem noItem = new TreeItem(item,SWT.NONE);
			noItem.setText(string);
		}
	}

	private static String obterResultadoDiffJ(int numeroTeste) {
		// TODO Auto-generated method stub
		RelatorioFinal.zerarRelatorio();
		Relatorio.zerarVariaveis(); 
		
		String[] args = { getPathVersaoAntiga(numeroTeste), getPathVersaoNova(numeroTeste) };
		DiffJ diffj = new DiffJ(args);
		
		Relatorio.getMudancaClasse().setNomClasse("Teste1");
		RelatorioFinal.preencherHashMap(Relatorio.getMudancaClasse());
		String resultado = RelatorioFinal.gerarResultadoXML();
		
		Relatorio.zerarVariaveis();
		RelatorioFinal.zerarRelatorio();

		return resultado;
	}

	public static String getPathVersaoAntiga(int numeroDoTeste) {
		return FOLDER_PATH + "\\" + numeroDoTeste + "\\" + NOME_VERSAO_ANTIGA;
	}

	public static String getPathVersaoNova(int numeroDoTeste) {
		return FOLDER_PATH + "\\" + numeroDoTeste + "\\" + NOME_VERSAO_NOVA;
	}

	public static String getPathResultadoEsperado(int numeroDoTeste) {
		return FOLDER_PATH + "\\" + numeroDoTeste + "\\"
				+ NOME_RESULTADO_ESPERADO;
	}
	
	public static String getPathDescricao(int numeroDoTeste) {
		return FOLDER_PATH + "\\" + numeroDoTeste + "\\"
		+ NOME_DESCRICAO;
	}

	public static String obterResultadoEsperado(int numeroDoTeste) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					getPathResultadoEsperado(numeroDoTeste)));
			List list = new ArrayList();

			String in;
			while ((in = br.readLine()) != null) {
				// contents.append(in + System.getProperty("line.separator"));
				list.add(in);
			}
			
			return list.toString().replace(" ", "")
					.replace("[", "").replace("]", "");
		} catch (Exception e) {
			Qualog.log("exception: " + e);
			return null;
		}
	}
	
	public static String obterDescricaoTeste(int numeroDoTeste) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					getPathDescricao(numeroDoTeste)));
			List list = new ArrayList();

			String in;
			while ((in = br.readLine()) != null) {
				// contents.append(in + System.getProperty("line.separator"));
				list.add(in);
			}
			
			return list.toString().replace("[", "").replace("]", "");
		} catch (Exception e) {
			Qualog.log("exception: " + e);
			return null;
		}
	}
}
