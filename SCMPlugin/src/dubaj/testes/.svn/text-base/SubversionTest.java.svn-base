package dubaj.testes;

import org.junit.Test;

import dubaj.model.Metrica;
import dubaj.tr.Relatorio;
import dubaj.tr.RelatorioFinal;
import scmaccess.model.SubVersion;
import junit.framework.Assert;
import junit.framework.TestCase;

public class SubversionTest extends TestCase {

	@Test
	public void testInsertInDatabase() {

		try {
			SubVersion subversion = new SubVersion();

			MainTesteSistema.SetUp();
			Metrica.deleteAll();
			String resultado = MainTesteSistema.obterResultadoDiffJ(3);

			Metrica filter = new Metrica();

			boolean expected = true;
			boolean actual;

			filter.setUrlProjetoAntiga("teste1.Teste1.java");
			filter.setUrlProjetoAtual("teste1.Teste1.java");
			filter.setNumeroRevisaoInicial(2);
			filter.setNumeroRevisaoFinal(3);

			actual = subversion.isMetricInDatabase(filter);

			assertEquals(expected, actual);

			filter.setNumeroRevisaoInicial(3);
			filter.setNumeroRevisaoFinal(4);

			actual = subversion.isMetricInDatabase(filter);

			assertEquals(expected, actual);

			filter.setUrlProjetoAntiga("teste11.Teste1.java");
			filter.setUrlProjetoAtual("teste11.Teste1.java");
			filter.setNumeroRevisaoInicial(4);
			filter.setNumeroRevisaoFinal(5);

			actual = subversion.isMetricInDatabase(filter);

			assertEquals(expected, actual);

			int numberOfLinesExpected = 3;

			int numberOfLinesActual = Metrica.SelectByFilter(new Metrica())
					.size();

			assertEquals(numberOfLinesExpected, numberOfLinesActual);

			Metrica.deleteAll();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void testConverterMetricaParaMudancaClasse() {

		Metrica metrica = new Metrica();

		metrica.setUrlProjetoAtual("teste1.java");
		metrica.setMudancasClasse(1);
		metrica.setMudancasAtributos(2);
		metrica.setMudancasMetodos(3);
		metrica.setCodeChurn(4);

		Relatorio.setMudancaClasse(new SubVersion()
				.converterMetricaParaMudancaClasse(metrica));

		assertEquals(Relatorio.getMudancaClasse().getNomClasse(),
				metrica.getUrlProjetoAtual());
		assertEquals(Relatorio.getMudancaClasse().calcularMudancasClasse(),
				metrica.getMudancasClasse());
		assertEquals(Relatorio.getMudancaClasse().calcularMudancasMetodo(),
				metrica.getMudancasMetodos());
		assertEquals(Relatorio.getMudancaClasse().calcularMudancasAtributo(),
				metrica.getMudancasAtributos());
		assertEquals(Relatorio.getMudancaClasse().obterCodeChurn(),
				metrica.getCodeChurn());
	}

	public void testSelectFromDatabase() {
		Metrica.deleteAll();
		
		Metrica metrica = new Metrica();

		metrica.setUrlProjetoAntiga("teste1.Teste1.java");
		metrica.setUrlProjetoAtual("teste1.Teste1.java");
		metrica.setNumeroRevisaoInicial(2);
		metrica.setNumeroRevisaoFinal(3);
		metrica.setMudancasClasse(1);
		metrica.setMudancasAtributos(1);
		metrica.setMudancasMetodos(1);
		metrica.setCodeChurn(1);

		Metrica.Insert(metrica);

		metrica.setNumeroRevisaoInicial(3);
		metrica.setNumeroRevisaoFinal(4);

		Metrica.Insert(metrica);

		metrica.setUrlProjetoAntiga("teste11.Teste1.java");
		metrica.setUrlProjetoAtual("teste11.Teste1.java");
		metrica.setNumeroRevisaoInicial(4);
		metrica.setNumeroRevisaoFinal(5);

		Metrica.Insert(metrica);
		try {
			String resultado = MainTesteSistema.obterResultadoDiffJ(3);

			StringBuilder stringExpected = new StringBuilder();
			stringExpected.append("<classe>,");
			stringExpected.append("<nomClasse>teste11.Teste1.java</nomClasse>,");
			stringExpected.append("<numeroMudancasTotais>9</numeroMudancasTotais>,");
			stringExpected.append("<numeroMudancasClasse>3</numeroMudancasClasse>,");
			stringExpected.append("<numeroMudancasMetodo>3</numeroMudancasMetodo>,");
			stringExpected.append("<numeroMudancasAtributo>3</numeroMudancasAtributo>,");
			stringExpected.append("<numeroCodeChurn>3</numeroCodeChurn>,");
			stringExpected.append("<numeroCommits>3</numeroCommits>,");
			stringExpected.append("</classe>");
			
			assertEquals(stringExpected.toString(), resultado);
			
			int numberOfLinesExpected = 3;
			int numberOfLinesActual = Metrica.SelectByFilter(new Metrica()).size();
			
			assertEquals(numberOfLinesExpected, numberOfLinesActual);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
