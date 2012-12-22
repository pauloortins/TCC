package dubaj.testes;

import dubaj.tr.RelatorioFinal;
import scmaccess.model.Node;
import scmaccess.model.SCM;
import scmaccess.model.SubVersion;
import scmaccess.model.exceptions.SCMConnectionRefusedException;

public class TesteUnitario {
	private static SCM scm;
	
	private int initialRevision;
	private int finalRevision;
	private String xmlEsperado;
	
	private TesteUnitario(SCM scm, int initialRevision, int finalRevision,
			String xmlEsperado) {
		super();
		this.scm = scm;
		this.initialRevision = initialRevision;
		this.finalRevision = finalRevision;
		this.xmlEsperado = xmlEsperado;
	}
	
	public static TesteUnitario criarTesteUnitario(int initialRevision, int finalRevision, String xmlEsperado)
	{
		TesteUnitario testeUnitario = new TesteUnitario(getSCM(), initialRevision, finalRevision, xmlEsperado);
		return testeUnitario;
	}
	
	public void ExecutarTeste()
	{
		RelatorioFinal.zerarRelatorio();
		
		Node node = new Node("ClassesTeste", "/", 0);
		
		try {
			scm.exportPath(node, "D:/runtime-EclipseApplication", initialRevision, finalRevision);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (ValidarResultado())
		{
			System.out.println("True");
		}
		else
		{
			System.out.println("False");
		}
	}
	
	private boolean ValidarResultado() {
		return xmlEsperado.equals(RelatorioFinal.gerarResultadoXML());
	}

	private static SCM getSCM()
	{
		if (scm == null)
		{
			scm = new SubVersion();
			try {
				scm.connect("file://c:/Users/paulo/svnrepositorio", "paulo", "");
			} catch (SCMConnectionRefusedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return scm;
	}
	
}
