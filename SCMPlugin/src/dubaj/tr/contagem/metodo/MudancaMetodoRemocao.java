package dubaj.tr.contagem.metodo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import org.eclipse.core.runtime.ISafeRunnable;

import diffj.net.sourceforge.pmd.ast.ASTMethodDeclaration;
import diffj.org.incava.diffj.MethodDiff;
import dubaj.tr.Relatorio;

public class MudancaMetodoRemocao extends MudancaMetodo {
	
	protected ASTMethodDeclaration declaracaoMetodo;
	
	public MudancaMetodoRemocao(ASTMethodDeclaration declaracaoMetodo) {
		super();
		// TODO Auto-generated constructor stub
		this.declaracaoMetodo = declaracaoMetodo;
	}
	
	public int contar(ArrayList<MudancaMetodo> listaMudancaMetodo) {
		// TODO Auto-generated method stub
		
		for (MudancaMetodo mudancaMetodo : listaMudancaMetodo) {
			
			if (mudancaMetodo instanceof MudancaMetodoAdicao)
			{
				if (isRenameMethod((MudancaMetodoAdicao) mudancaMetodo))
				{
					return 0;
				}
			}
		}
		
		return 1;
	}
	
	public boolean isRenameMethod(MudancaMetodoAdicao mudancaMetodo)
	{
		MethodDiff methodDiff = new MethodDiff(new TreeSet());

		return !methodDiff.compararCorpo(this.declaracaoMetodo, mudancaMetodo.getDeclaracaoMetodo(), Relatorio.TipoDeclaracao.Metodo);
	}
}
