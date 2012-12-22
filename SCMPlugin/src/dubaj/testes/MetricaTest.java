package dubaj.testes;

import org.junit.Test;

import scmaccess.model.SubVersion;
import dubaj.model.Metrica;
import junit.framework.TestCase;

public class MetricaTest extends TestCase {
	
	@Test public void testAllQuerys() {
		Metrica metrica = new Metrica();
		metrica.setUrlProjetoAntiga("url//projeto//teste");
		metrica.setUrlProjetoAtual("url//projeto//teste");
		metrica.setNumeroRevisaoInicial(0);
		metrica.setNumeroRevisaoFinal(1);
		metrica.setMudancasMetodos(1);
		metrica.setMudancasAtributos(1);
		metrica.setMudancasClasse(1);
		metrica.setCodeChurn(4);
		
		Metrica.deleteAll();
		
		Metrica.Insert(metrica);
		
		int numRegisters = Metrica.SelectByFilter(metrica).size();
		
		assertEquals(1, numRegisters);
		
		Metrica.deleteAll();
		
		numRegisters = Metrica.SelectByFilter(metrica).size();
		
		assertEquals(0, numRegisters);
	}
	
	@Test public void testIfExistsInDatabase()
	{
		Metrica metrica = new Metrica();
		metrica.setUrlProjetoAntiga("url//projeto//teste");
		metrica.setUrlProjetoAtual("url//projeto//teste");
		metrica.setNumeroRevisaoInicial(0);
		metrica.setNumeroRevisaoFinal(1);
		metrica.setMudancasMetodos(1);
		metrica.setMudancasAtributos(1);
		metrica.setMudancasClasse(1);
		metrica.setCodeChurn(4);
		
		Metrica.Insert(metrica);
		
		SubVersion subversion = new SubVersion();
		
		Metrica metricaFilter = new Metrica();
		
		metrica.setUrlProjetoAntiga("url//projeto//teste");
		metrica.setUrlProjetoAtual("url//projeto//teste");
		metrica.setNumeroRevisaoInicial(0);
		metrica.setNumeroRevisaoFinal(1);
		
		boolean expected = true;
		boolean actual = subversion.isMetricInDatabase(metricaFilter);
		
		assertEquals(expected, actual);
	}
}
