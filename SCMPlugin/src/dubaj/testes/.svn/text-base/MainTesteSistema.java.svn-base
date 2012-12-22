package dubaj.testes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

import scmaccess.model.Node;
import scmaccess.model.SCM;
import scmaccess.model.SubVersion;

import diffj.org.incava.qualog.Qualog;
import dubaj.tr.Relatorio;
import dubaj.tr.RelatorioFinal;

public class MainTesteSistema {

	private static final org.eclipse.swt.graphics.Color VERMELHO = new org.eclipse.swt.graphics.Color(
			null, 255, 0, 0);

	private static final org.eclipse.swt.graphics.Color VERDE = new org.eclipse.swt.graphics.Color(
			null, 153, 205, 50);

	final static String FOLDER_PATH = "D:\\Faculdade\\TCC\\TestesSistema";
	final static String NOME_RESULTADO_ESPERADO = "ResultadoEsperado.txt";
	final static String NOME_DESCRICAO = "Descricao.txt";
	final static String NOME_REVISAO_INICIAL = "RevisaoInicial.txt";
	final static String NOME_REVISAO_FINAL = "RevisaoFinal.txt";
	final static String URL_REPOSITORIO = "file://c:/Users/paulo/RepositorioTesteDubaJ";
	final static String LOGIN_USUARIO = "paulo.ortins";
	final static String PASSWORD_USUARIO = "";
	final static String EXPORT_FOLDER_PATH = "D:/runtime-EclipseApplication";
	final static String NODE_PATH = "/";
	final static String NODE_NAME = "TesteSistemaDubaJ";

	static SCM scm = null;
	static Node node = null;

	public static void SetUp() {

		try {
			scm = new SubVersion();
			scm.connect(URL_REPOSITORIO, LOGIN_USUARIO, PASSWORD_USUARIO);

			node = new Node(NODE_NAME, NODE_PATH, 0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean rodarTeste(int numeroDoTeste, TreeItem testTree) {

		try {

			boolean testSucess = true;

			String descricao = obterDescricaoTeste(numeroDoTeste);
			String resultadoEsperado = obterResultadoEsperado(numeroDoTeste);
			String resultadoObtido = obterResultadoDiffJ(numeroDoTeste);

			TreeItem itemCabecalho = new TreeItem(testTree, SWT.NONE);

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

		} catch (Exception ex) {
			return false;
		}
	}

	private static void formatarParaExibicao(String resultado, TreeItem item) {
		// TODO Auto-generated method stub
		String[] listaStrings = resultado.split(",");

		for (String string : listaStrings) {
			TreeItem noItem = new TreeItem(item, SWT.NONE);
			noItem.setText(string);
		}
	}

	public static String obterResultadoDiffJ(int numeroDoTeste)
			throws Exception {
		// TODO Auto-generated method stub
		int revisaoInicial = obterRevisaoInicial(numeroDoTeste);
		int revisaoFinal = obterRevisaoFinal(numeroDoTeste);

		RelatorioFinal.zerarRelatorio();

		scm.exportPath(node, EXPORT_FOLDER_PATH, revisaoInicial, revisaoFinal);

		String resultado = RelatorioFinal.gerarResultadoXML();

		RelatorioFinal.zerarRelatorio();

		return resultado;
	}

	public static String getPathResultadoEsperado(int numeroDoTeste) {
		return FOLDER_PATH + "\\" + numeroDoTeste + "\\"
				+ NOME_RESULTADO_ESPERADO;
	}

	public static String getPathDescricao(int numeroDoTeste) {
		return FOLDER_PATH + "\\" + numeroDoTeste + "\\" + NOME_DESCRICAO;
	}

	public static String getPathRevisaoInicial(int numeroDoTeste) {
		return FOLDER_PATH + "\\" + numeroDoTeste + "\\" + NOME_REVISAO_INICIAL;
	}

	public static String getPathRevisaoFinal(int numeroDoTeste) {
		return FOLDER_PATH + "\\" + numeroDoTeste + "\\" + NOME_REVISAO_FINAL;
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

			return list.toString().replace(" ", "").replace("[", "")
					.replace("]", "");
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

	public static int obterRevisaoInicial(int numeroDoTeste) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					getPathRevisaoInicial(numeroDoTeste)));
			List list = new ArrayList();

			String in;
			while ((in = br.readLine()) != null) {
				// contents.append(in + System.getProperty("line.separator"));
				list.add(in);
			}

			return Integer.parseInt(list.toString().replace("[", "")
					.replace("]", ""));
		} catch (Exception e) {
			Qualog.log("exception: " + e);
			return -1;
		}
	}

	public static int obterRevisaoFinal(int numeroDoTeste) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					getPathRevisaoFinal(numeroDoTeste)));
			List list = new ArrayList();

			String in;
			while ((in = br.readLine()) != null) {
				// contents.append(in + System.getProperty("line.separator"));
				list.add(in);
			}

			return Integer.parseInt(list.toString().replace("[", "")
					.replace("]", ""));
		} catch (Exception e) {
			Qualog.log("exception: " + e);
			return -1;
		}
	}

}
