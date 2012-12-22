package diffj.org.incava.diffj;

import java.awt.Point;
import java.io.*;
import java.util.*;

import diffj.net.sourceforge.pmd.ast.*;
import diffj.org.incava.analysis.*;
import diffj.org.incava.java.*;
import diffj.org.incava.util.*;
import diffj.org.incava.util.diff.*;
import dubaj.tr.MudancaVO;
import dubaj.tr.Relatorio;
import dubaj.tr.Relatorio.TipoDeclaracao;



public class FieldDiff extends ItemDiff
{
    public static final String VARIABLE_REMOVED = "variable removed: {0}";

    public static final String VARIABLE_ADDED = "variable added: {0}";

    public static final String VARIABLE_CHANGED = "variable changed from {0} to {1}";

    public static final String INITIALIZER_REMOVED = "initializer removed";

    public static final String INITIALIZER_ADDED = "initializer added";

    protected static final int[] VALID_MODIFIERS = new int[] {
        JavaParserConstants.FINAL,
        JavaParserConstants.STATIC,
    };

    public FieldDiff(Collection differences)
    {
        super(differences);
    }

    public void compare(ASTFieldDeclaration a, ASTFieldDeclaration b,Relatorio.TipoDeclaracao tipoElemento)
    {
        if (compareModifiers(a, b, tipoElemento))
        {
        	Relatorio.getMudancaClasse().incrementarMudancasAtributo();
        }
        
        if (compararTiposRetorno(a, b, tipoElemento))
        {
        	Relatorio.getMudancaClasse().incrementarMudancasAtributo();
        }
        
        compareVariables(a, b, TipoDeclaracao.Atributo);
    }

    protected boolean compareModifiers(ASTFieldDeclaration a, ASTFieldDeclaration b, Relatorio.TipoDeclaracao tipoElemento)
    {
        return compareModifiers((SimpleNode)a.jjtGetParent(), (SimpleNode)b.jjtGetParent(), VALID_MODIFIERS,tipoElemento);
    }

    protected void compareVariables(ASTVariableDeclarator a, ASTVariableDeclarator b,Relatorio.TipoDeclaracao tipoElemento)
    {
        ASTVariableInitializer ainit = (ASTVariableInitializer)SimpleNodeUtil.findChild(a, ASTVariableInitializer.class);
        ASTVariableInitializer binit = (ASTVariableInitializer)SimpleNodeUtil.findChild(b, ASTVariableInitializer.class);
        
        if (ainit == null) {
            if (binit == null) {
                // no change in init
            }
            else {
                dubaj.tr.Ace.stack(dubaj.tr.Ace.YELLOW, "binit", binit);
                addChanged(INITIALIZER_ADDED, a, binit,tipoElemento);
                
                Relatorio.getMudancaClasse().incrementarMudancasAtributo();
            }
        }
        else if (binit == null) {
            addChanged(INITIALIZER_REMOVED, ainit, b, tipoElemento);
            
            Relatorio.getMudancaClasse().incrementarMudancasAtributo();
        }
        else {
            List aCode = SimpleNodeUtil.getChildrenSerially(ainit);
            List bCode = SimpleNodeUtil.getChildrenSerially(binit);

            // It is logically impossible for this to execute where "b"
            // represents the from-file, and "a" the to-file, since "a.name"
            // would have matched "b.name" in the first loop of
            // compareVariableLists

            String aName = FieldUtil.getName(a).image;
            String bName = FieldUtil.getName(b).image;
            
            if (compararCorpo(aName, aCode, bName, bCode, tipoElemento))
            {
            	Relatorio.getMudancaClasse().incrementarMudancasAtributo();
            }
        }
    }

    protected static Map makeVDMap(ASTVariableDeclarator[] vds)
    {
        Map namesToVD = new HashMap();
        for (int vi = 0; vi < vds.length; ++vi) {
            ASTVariableDeclarator vd = vds[vi];
            String name = FieldUtil.getName(vd).image;
            namesToVD.put(name, vd);
        }
        return namesToVD;
    }

	protected void compareVariables(ASTFieldDeclaration a, ASTFieldDeclaration b, Relatorio.TipoDeclaracao tipoElemento)
    {
        ASTVariableDeclarator[] avds = (ASTVariableDeclarator[])SimpleNodeUtil.findChildren(a, ASTVariableDeclarator.class);
        ASTVariableDeclarator[] bvds = (ASTVariableDeclarator[])SimpleNodeUtil.findChildren(b, ASTVariableDeclarator.class);

        Map aNamesToVD = makeVDMap(avds);
        Map bNamesToVD = makeVDMap(bvds);

        Collection names = new TreeSet();
        names.addAll(aNamesToVD.keySet());
        names.addAll(bNamesToVD.keySet());
        
        Iterator nit = names.iterator();
        while (nit.hasNext()) {
            String name = (String)nit.next();
            dubaj.tr.Ace.log("name", name);
            ASTVariableDeclarator avd = (ASTVariableDeclarator)aNamesToVD.get(name);
            ASTVariableDeclarator bvd = (ASTVariableDeclarator)bNamesToVD.get(name);

            if (avd == null || bvd == null) {
                if (avds.length == 1 && bvds.length == 1) {
                    dubaj.tr.Ace.log("avd", avd);
                    dubaj.tr.Ace.log("bvd", bvd);
                    Token aTk = FieldUtil.getName(avds[0]);
                    Token bTk = FieldUtil.getName(bvds[0]);
                    
                    addChanged(VARIABLE_CHANGED, aTk, bTk,tipoElemento);
                    
                    compareVariables(avds[0], bvds[0],tipoElemento);
                }
                else if (avd == null) {
                    dubaj.tr.Ace.log("avd", avd);
                    dubaj.tr.Ace.log("bvd", bvd);
                    Token aTk = FieldUtil.getName(avds[0]);
                    Token bTk = FieldUtil.getName(bvd);
                    addChanged(VARIABLE_ADDED, new Object[] { name }, aTk, bTk, tipoElemento);
                  
                }
                else {
                    dubaj.tr.Ace.log("avd", avd);
                    dubaj.tr.Ace.log("bvd", bvd);
                    Token aTk = FieldUtil.getName(avd);
                    Token bTk = FieldUtil.getName(bvds[0]);
                    addChanged(VARIABLE_REMOVED, new Object[] { name }, aTk, bTk, tipoElemento);
                   
                }
            }
            else {
                dubaj.tr.Ace.log("avd", avd);
                dubaj.tr.Ace.log("bvd", bvd);
                compareVariables(avd, bvd, tipoElemento);
            }
        }
    }

}
