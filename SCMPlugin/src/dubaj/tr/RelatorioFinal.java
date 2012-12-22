package dubaj.tr;

import java.util.HashMap;

import dubaj.tr.contagem.metodo.RelatorioMudancasClasse;
import dubaj.util.Util;

public class RelatorioFinal {

	private static HashMap<String, RelatorioMudancasClasse> mapMudancas = new HashMap<String, RelatorioMudancasClasse>();

	public static HashMap<String, RelatorioMudancasClasse> getMapMudancas() {
		return mapMudancas;
	}

	public static String gerarResultadoXML() {

		StringBuilder string = new StringBuilder();
		boolean isPrimeiro = true;

		for (String chave : mapMudancas.keySet()) {
			RelatorioMudancasClasse mudancas = mapMudancas.get(chave);

			if (isPrimeiro) {
				isPrimeiro = false;
			} else {
				string.append(",");
			}

			string.append("<classe>,");
			string.append("<nomClasse>" + mudancas.getNomClasse()
					+ "</nomClasse>,");
			string.append("<numeroMudancasTotais>"
					+ mudancas.getNumMudancasTotal()
					+ "</numeroMudancasTotais>,");
			string.append("<numeroMudancasClasse>"
					+ mudancas.getNumMudancasClasse()
					+ "</numeroMudancasClasse>,");
			string.append("<numeroMudancasMetodo>"
					+ mudancas.getNumMudancasMetodo()
					+ "</numeroMudancasMetodo>,");
			string.append("<numeroMudancasAtributo>"
					+ mudancas.getNumMudancasAtributo()
					+ "</numeroMudancasAtributo>,");
			string.append("<deltaLOC>" + mudancas.getNumCodeChurn()
					+ "</deltaLOC>,");
			string.append("<linesOfCode>" + mudancas.getNumLinesOfCode() + "</linesOfCode>,");
			string.append("<deltaLOCRelativo>" + mudancas.getNumCodeChurnRelativo() + "</deltaLOCRelativo>,");
			string.append("<numeroCommits>" + mudancas.getNumCommits()
					+ "</numeroCommits>,");
			string.append("</classe>");

		}

		return string.toString();
	}

	public static void preencherHashMap(MudancaClasseVO mudancaVO) {
		// Caso ja exista a classe no relatorio de mudancas, soma as alteracoes,
		// caso contrario inclui um novo registro

		RelatorioMudancasClasse mudanca = null;

		if (mapMudancas.containsKey(mudancaVO.getNomClasse())) {
			mudanca = mapMudancas.get(mudancaVO.getNomClasse());
		} else {
			mudanca = new RelatorioMudancasClasse();
		}

		mudanca.setNomClasse(mudancaVO.getNomClasse());
		mudanca.setNumMudancasAtributo(mudanca.getNumMudancasAtributo()
				+ mudancaVO.calcularMudancasAtributo());
		mudanca.setNumMudancasMetodo(mudanca.getNumMudancasMetodo()
				+ mudancaVO.calcularMudancasMetodo());
		mudanca.setNumMudancasClasse(mudanca.getNumMudancasClasse()
				+ mudancaVO.calcularMudancasClasse());
		mudanca.setNumCodeChurn(mudanca.getNumCodeChurn()
				+ mudancaVO.obterCodeChurn());
		mudanca.setNumLinesOfCode(mudancaVO.getNumeroLinesOfCode());
		if (mudanca.getNumLinesOfCode() != 0) {
			mudanca.setNumCodeChurnRelativo(Util.roundTwoDecimals((double) mudanca.getNumCodeChurn() / mudanca.getNumLinesOfCode()));
		} else {
			mudanca.setNumCodeChurnRelativo(0);
		}
		
		mudanca.addNumCommits();

		mapMudancas.remove(mudanca.getNomClasse());
		mapMudancas.put(mudanca.getNomClasse(), mudanca);

	}

	public static void AlterarPacote(String nomClasseAntiga,
			String nomClasseNova) {

		RelatorioMudancasClasse mudanca = mapMudancas.get(nomClasseAntiga);

		if (mudanca != null) {
			mudanca.setNomClasse(nomClasseNova);

			mapMudancas.remove(nomClasseAntiga);
			mapMudancas.put(mudanca.getNomClasse(), mudanca);
		}
	}

	public static void zerarRelatorio() {
		mapMudancas = new HashMap<String, RelatorioMudancasClasse>();
		listaMudancasPacotes = new HashMap<String, String>();
	}

	private static HashMap<String, String> listaMudancasPacotes;

	public static void AdicionarAlteracaoPacote(String nomClasseAntiga,
			String nomClasseNova) {
		// TODO Auto-generated method stub
		listaMudancasPacotes.put(nomClasseAntiga, nomClasseNova);
	}

}
