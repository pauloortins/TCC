package diffj.org.incava.diffj;

import java.util.*;

import diffj.net.sourceforge.pmd.ast.*;
import diffj.org.incava.analysis.*;
import diffj.org.incava.java.*;
import dubaj.tr.MudancaVO;
import dubaj.tr.Relatorio;
import dubaj.tr.Relatorio.TipoDeclaracao;



public class PackageDiff extends DiffComparator
{
    public static final String PACKAGE_REMOVED = "package removed: {0}";

    public static final String PACKAGE_ADDED = "package added: {0}";

    public static final String PACKAGE_RENAMED = "package renamed from {0} to {1}";

    public PackageDiff(Collection differences)
    {
        super(differences);        
    }

    public void compare(ASTCompilationUnit a, ASTCompilationUnit b)
    {
        ASTPackageDeclaration aPkg = CompilationUnitUtil.getPackage(a);
        ASTPackageDeclaration bPkg = CompilationUnitUtil.getPackage(b);
        
        boolean houveAlteracao = false;
        
        if (aPkg == null) {
            if (bPkg == null) {
                dubaj.tr.Ace.log("neither has packages");
            }
            else {
                ASTName    name = (ASTName)SimpleNodeUtil.findChild(bPkg, ASTName.class);
                SimpleNode aPos = SimpleNodeUtil.findChild(a, null);
                dubaj.tr.Ace.log("name: " + name + "; aPos: " + aPos);
                if (aPos == null) {
                    aPos = a;
                }
                
                addAdded(PACKAGE_ADDED, aPos, name, Relatorio.TipoDeclaracao.Pacote);
                houveAlteracao = true;
            }
        }
        else if (bPkg == null) {
            ASTName    name = (ASTName)SimpleNodeUtil.findChild(aPkg, ASTName.class);
            SimpleNode bPos = SimpleNodeUtil.findChild(b, null);
            dubaj.tr.Ace.log("name: " + name + "; bPos: " + bPos);
            if (bPos == null) {
                bPos = b;
            }
            
            addDeleted(PACKAGE_REMOVED, name, bPos, Relatorio.TipoDeclaracao.Pacote);
            houveAlteracao = true;
            
        }
        else {
            ASTName aName = (ASTName)SimpleNodeUtil.findChild(aPkg, ASTName.class);
            String  aStr  = SimpleNodeUtil.toString(aName);
            ASTName bName = (ASTName)SimpleNodeUtil.findChild(bPkg, ASTName.class);
            String  bStr  = SimpleNodeUtil.toString(bName);

            dubaj.tr.Ace.log("aName: " + aName + "; bName: " + bName);
            dubaj.tr.Ace.log("aStr: " + aStr + "; bStr: " + bStr);
            
            // Obs: Compara as duas strings referentes a declaracao de package , caso forem iguais "no change" senao adiciona como changed
            if (aStr.equals(bStr)) {
                dubaj.tr.Ace.log("no change");
            }
            else {
            	
                addChanged(PACKAGE_RENAMED, aName, bName,Relatorio.TipoDeclaracao.Pacote);
                houveAlteracao = true;
            }
        }
        
        if (houveAlteracao)
        {
        	Relatorio.getMudancaClasse().incrementarMudancasClasse();
        }
    }
    
}
