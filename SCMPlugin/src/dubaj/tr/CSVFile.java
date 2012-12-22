package dubaj.tr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import dubaj.internacionalizacao.InternationalResource;
import dubaj.tr.contagem.metodo.RelatorioMudancasClasse;

public class CSVFile {

	final String fileName;
	
	final String separator = ";";

	public CSVFile(String fileName) {
		super();
		this.fileName = fileName;
	}

	public void generateCsvFile() {

		try {
			FileWriter writer = new FileWriter(fileName);

			writer.append(gerarCabecalho());

			writer.append(gerarLinhasDeClasse());
			// generate whatever data you want

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String gerarLinhasDeClasse() {
		// TODO Auto-generated method stub
		StringBuilder linhasClasse = new StringBuilder();

		ArrayList<RelatorioMudancasClasse> listaClasses = new ArrayList<RelatorioMudancasClasse>(
				RelatorioFinal.getMapMudancas().values());

		// Ordena as classes pra aquela na ordem descendente de acordo com o
		// numero de mudancas
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

		for (RelatorioMudancasClasse mudancas : listaClasses) {

			linhasClasse.append(mudancas.getNomClasse());
			linhasClasse.append(separator);
			linhasClasse.append(mudancas.getNumMudancasClasse());
			linhasClasse.append(separator);
			linhasClasse.append(mudancas.getNumMudancasMetodo());
			linhasClasse.append(separator);
			linhasClasse.append(mudancas.getNumMudancasAtributo());
			linhasClasse.append(separator);
			linhasClasse.append(mudancas.getNumMudancasTotal());
			linhasClasse.append(separator);
			linhasClasse.append(mudancas.getNumCodeChurn());
			linhasClasse.append(separator);
			linhasClasse.append(mudancas.getNumLinesOfCode());
			linhasClasse.append(separator);
			linhasClasse.append(mudancas.getNumCodeChurnRelativo());
			linhasClasse.append(separator);
			linhasClasse.append(mudancas.getNumCommits());
			linhasClasse.append('\n');
		}

		return linhasClasse.toString();
	}

	public void deleteFile() {
		// TODO Auto-generated method stub
		File file = new File(fileName);
		file.delete();
	}

	public String gerarCabecalho() {
		StringBuilder cabecalho = new StringBuilder();

		cabecalho.append(InternationalResource.getIdioma().getClassName());
		cabecalho.append(separator);
		cabecalho.append(InternationalResource.getIdioma().getClassesChanges());
		cabecalho.append(separator);
		cabecalho.append(InternationalResource.getIdioma().getMethodsChanges());
		cabecalho.append(separator);
		cabecalho.append(InternationalResource.getIdioma()
				.getAttributesChanges());
		cabecalho.append(separator);
		cabecalho.append(InternationalResource.getIdioma().getSumOfChanges());
		cabecalho.append(separator);
		cabecalho.append(InternationalResource.getIdioma().getDeltaLOC());
		cabecalho.append(separator);
		cabecalho.append(InternationalResource.getIdioma().getLinesOfCode());
		cabecalho.append(separator);
		cabecalho.append(InternationalResource.getIdioma().getDeltaLOCRelativo());
		cabecalho.append(separator);
		cabecalho.append(InternationalResource.getIdioma().getNumberOfCommits());
		cabecalho.append('\n');

		return cabecalho.toString();
	}
}
