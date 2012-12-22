package diffj.org.incava.diffj;

import java.util.*;



import diffj.net.sourceforge.pmd.ast.*;
import diffj.org.incava.java.*;
import dubaj.tr.Relatorio;
import dubaj.tr.contagem.metodo.MudancaMetodo;



public class MethodDiff extends FunctionDiff
{
    public static final String METHOD_BLOCK_ADDED = "method block added";

    public static final String METHOD_BLOCK_REMOVED = "method block removed";

    protected static final int[] VALID_MODIFIERS = new int[] {
        JavaParserConstants.ABSTRACT,
        JavaParserConstants.FINAL,
        JavaParserConstants.NATIVE,
        JavaParserConstants.STATIC,
        JavaParserConstants.STRICTFP
    };

	public MethodDiff(Collection differences)
    {
        super(differences);
    }

    public void compare(ASTMethodDeclaration a, ASTMethodDeclaration b, Relatorio.TipoDeclaracao tipoElemento)
    {
        compareModifiers(a, b, tipoElemento);
        compareReturnTypes(a, b,tipoElemento);
        compareParameters(a, b,tipoElemento);
        compareThrows(a, b,tipoElemento);
        compareBodies(a, b,tipoElemento);
    }
    
    public boolean comparar(ASTMethodDeclaration a, ASTMethodDeclaration b, Relatorio.TipoDeclaracao tipoElemento) {
    	boolean achouMudanca = false;
    	
    	if (compararCorpo(a, b, tipoElemento)) {
			Relatorio.getMudancaClasse().incrementarMudancasMetodo(new MudancaMetodo());
			achouMudanca = true;
    	}
    	
    	if (compararParametros(a, b, tipoElemento)) {
    		Relatorio.getMudancaClasse().incrementarMudancasMetodo(new MudancaMetodo());
    		achouMudanca = true;
    	}
    	
    	if (compararLancamentoExcecao(a, b, tipoElemento))
    	{
    		Relatorio.getMudancaClasse().incrementarMudancasMetodo(new MudancaMetodo());
    		achouMudanca = true;
    	}
    	
    	if (compararTiposRetorno(a, b, tipoElemento))
    	{
    		Relatorio.getMudancaClasse().incrementarMudancasMetodo(new MudancaMetodo());
    		achouMudanca = true;
    	}
    	
    	if (compareModifiers(a, b, tipoElemento))
    	{
    		Relatorio.getMudancaClasse().incrementarMudancasMetodo(new MudancaMetodo());
    		achouMudanca = true;
    	}
    	
    	return achouMudanca;
    }
    
    protected boolean compararLancamentoExcecao(ASTMethodDeclaration a, ASTMethodDeclaration b,Relatorio.TipoDeclaracao tipoElemento)
    {
        ASTNameList at = MethodUtil.getThrowsList(a);
        ASTNameList bt = MethodUtil.getThrowsList(b);

        return compararLancamentoExcecao(a, at, b, bt, tipoElemento);
    }
    
    protected boolean compararParametros(ASTMethodDeclaration a, ASTMethodDeclaration b,Relatorio.TipoDeclaracao tipoElemento) {
    	ASTFormalParameters afp = MethodUtil.getParameters(a);
        ASTFormalParameters bfp = MethodUtil.getParameters(b);

        return compararParametroMetodo(afp, bfp,tipoElemento);
    }
    
    protected boolean compararTiposRetorno(SimpleNode a, SimpleNode b,Relatorio.TipoDeclaracao tipoElemento)
    {
        SimpleNode art    = (SimpleNode)a.jjtGetChild(0);
        SimpleNode brt    = (SimpleNode)b.jjtGetChild(0);
        String     artStr = SimpleNodeUtil.toString(art);
        String     brtStr = SimpleNodeUtil.toString(brt);
        // tr.Ace.log("art: " + art + "; brt: " + brt);

        if (artStr.equals(brtStr)) {
            // tr.Ace.log("no change in return types");
        	return false;
        }
        else {
            addChanged(RETURN_TYPE_CHANGED, new Object[] { artStr, brtStr }, art, brt, tipoElemento);
            return true;
        }
    }
    
    protected boolean compararModificadoresMetodo(ASTMethodDeclaration a, ASTMethodDeclaration b,Relatorio.TipoDeclaracao tipoElemento)
    {
    	return compararModificadorMetodo(SimpleNodeUtil.getParent(a), SimpleNodeUtil.getParent(b), VALID_MODIFIERS,tipoElemento);
    }

    protected boolean compareModifiers(ASTMethodDeclaration a, ASTMethodDeclaration b,Relatorio.TipoDeclaracao tipoElemento)
    {
        return compararModificadorMetodo(SimpleNodeUtil.getParent(a), SimpleNodeUtil.getParent(b), VALID_MODIFIERS,tipoElemento);
    }

    protected void compareParameters(ASTMethodDeclaration a, ASTMethodDeclaration b,Relatorio.TipoDeclaracao tipoElemento)
    {
        ASTFormalParameters afp = MethodUtil.getParameters(a);
        ASTFormalParameters bfp = MethodUtil.getParameters(b);

        compareParameters(afp, bfp,tipoElemento);
    }

    protected void compareThrows(ASTMethodDeclaration a, ASTMethodDeclaration b,Relatorio.TipoDeclaracao tipoElemento)
    {
        ASTNameList at = MethodUtil.getThrowsList(a);
        ASTNameList bt = MethodUtil.getThrowsList(b);

        compareThrows(a, at, b, bt, tipoElemento);
    }

    protected void compareBodies(ASTMethodDeclaration a, ASTMethodDeclaration b,Relatorio.TipoDeclaracao tipoElemento)
    {
        final int ABSTRACT_METHOD_CHILDREN = 2;

        ASTBlock aBlock = (ASTBlock)SimpleNodeUtil.findChild(a, ASTBlock.class);
        ASTBlock bBlock = (ASTBlock)SimpleNodeUtil.findChild(b, ASTBlock.class);

        if (aBlock == null) {
            if (bBlock == null) {
                // neither has a block, so no change
            }
            else {
                addChanged(METHOD_BLOCK_ADDED, a, b, tipoElemento);                
            }
        }
        else if (bBlock == null) {
            addChanged(METHOD_BLOCK_REMOVED, a, b, tipoElemento);
        }
        else {
            String aName = MethodUtil.getFullName(a);
            String bName = MethodUtil.getFullName(b);
            
            compareBlocks(aName, aBlock, bName, bBlock, tipoElemento);
        }
    }

    protected void compareBlocks(String aName, ASTBlock aBlock, String bName, ASTBlock bBlock, Relatorio.TipoDeclaracao tipoElemento)
    {
        List a = SimpleNodeUtil.getChildrenSerially(aBlock);
        List b = SimpleNodeUtil.getChildrenSerially(bBlock);

        compareCode(aName, a, bName, b, tipoElemento);
    }
    
    protected boolean compararParametroMetodo(ASTFormalParameters afp, ASTFormalParameters bfp,Relatorio.TipoDeclaracao tipoElemento)
    {
        List aParams = ParameterUtil.getParameterList(afp);
        List bParams = ParameterUtil.getParameterList(bfp);
        
        List aParamTypes = ParameterUtil.getParameterTypes(afp);
        List bParamTypes = ParameterUtil.getParameterTypes(bfp);

        int aSize = aParamTypes.size();
        int bSize = bParamTypes.size();

        // tr.Ace.log("aParamTypes.size: " + aSize + "; bParamTypes.size: " + bSize);

        if (aSize == 0) {
            if (bSize == 0) {
                // tr.Ace.log("no change in parameters");
            	return false;
            }
            else {
                Token[] names = ParameterUtil.getParameterNames(bfp);
                for (int ni = 0; ni < names.length; ++ni) {
                    addChanged(PARAMETER_ADDED, 
                               new Object[] { 
                                   names[ni].image
                               }, 
                               afp.getFirstToken(), 
                               afp.getLastToken(), 
                               names[ni], 
                               names[ni],tipoElemento);
                    return true;
                }
            }
        }
        else if (bSize == 0) {
            Token[] names = ParameterUtil.getParameterNames(afp);
            for (int ni = 0; ni < names.length; ++ni) {
                addChanged(PARAMETER_REMOVED,
                           new Object[] {
                               names[ni].image
                           },
                           names[ni],
                           names[ni],
                           bfp.getFirstToken(),
                           bfp.getLastToken(),tipoElemento);
                return true;
            }
        }
        else {
            for (int ai = 0; ai < aSize; ++ai) {
                Object[] aValues = (Object[])aParams.get(ai);

                // tr.Ace.log("a.param: " + aValues[0] + "; a.type: " + aValues[1] + "; a.name: " + aValues[2]);

                int[] paramMatch = ParameterUtil.getMatch(aParams, ai, bParams);

                // tr.Ace.log("paramMatch", paramMatch);

                ASTFormalParameter aParam = ParameterUtil.getParameter(afp, ai);
                Token aNameTk = ParameterUtil.getParameterName(aParam);

                if (paramMatch[0] == ai && paramMatch[1] == ai) {
                    // tr.Ace.log("exact match");
                }
                else if (paramMatch[0] == ai) {
                    // tr.Ace.log("name changed");
                    Token bNameTk = ParameterUtil.getParameterName(bfp, ai);
                    addChanged(PARAMETER_NAME_CHANGED,
                               new Object[] {
                                   aNameTk.image,
                                   bNameTk.image
                               },
                               aNameTk,
                               bNameTk,tipoElemento);
                    return true;
                }
                else if (paramMatch[1] == ai) {
                    // tr.Ace.log("type changed");
                    ASTFormalParameter bParam = ParameterUtil.getParameter(bfp, ai);
                    String             bType  = ParameterUtil.getParameterType(bParam);
                    // tr.Ace.log("bParam: " + bParam + "; bType: " + bType);

                    addChanged(PARAMETER_TYPE_CHANGED, 
                               new String[] { 
                                   (String)aValues[1], 
                                   bType
                               }, 
                               (ASTFormalParameter)aValues[0], 
                               bParam,tipoElemento);
                    return true;
                }
                else if (paramMatch[0] >= 0) {
                    // tr.Ace.log("misordered match by type");
                    Token bNameTk = ParameterUtil.getParameterName(bfp, paramMatch[0]);
                    // tr.Ace.log("aNameTk: " + aNameTk + "; bNameTk: " + bNameTk);
                    // tr.Ace.log("aNameTk.image: " + aNameTk.image + "; bNameTk.image: " + bNameTk.image);
                    if (aNameTk.image.equals(bNameTk.image)) {
                        addChanged(PARAMETER_REORDERED, 
                                   new Object[] { 
                                       aNameTk.image, 
                                       new Integer(ai), 
                                       new Integer(paramMatch[0])
                                   }, 
                                   aNameTk, 
                                   bNameTk,tipoElemento);
                        return true;
                    }
                    else {
                        addChanged(PARAMETER_REORDERED_AND_RENAMED, 
                                   new Object[] { 
                                       aNameTk.image,
                                       new Integer(ai),
                                       new Integer(paramMatch[0]),
                                       bNameTk.image
                                   },
                                   aNameTk, 
                                   bNameTk,tipoElemento);
                        return true;
                    }
                }
                else if (paramMatch[1] >= 0) {
                    System.out.println("misordered match by name");
                    
                    dubaj.tr.Ace.log("misordered match by name");

                    ASTFormalParameter bParam = ParameterUtil.getParameter(bfp, paramMatch[1]);

                    addChanged(PARAMETER_REORDERED, new Object[] { aNameTk.image, new Integer(ai), new Integer(paramMatch[1]) }, aParam, bParam,tipoElemento);
                    return true;
                }
                else {
                    // tr.Ace.log("not a match");
                    // tr.Ace.log("aNameTk: " + aNameTk);
                    addChanged(PARAMETER_REMOVED, new Object[] { aNameTk.image }, aParam, bfp,tipoElemento);
                    return true;
                }
            }

            // tr.Ace.log("aParams: " + aParams);
            // tr.Ace.log("bParams: " + bParams);

            Iterator bit = bParams.iterator();
            for (int bi = 0; bit.hasNext(); ++bi) {
                Object[] bType = (Object[])bit.next();
                if (bType == null) {
                    // tr.Ace.log("already processed");
          
                }
                else {
                    ASTFormalParameter bParam = ParameterUtil.getParameter(bfp, bi);
                    Token bName = ParameterUtil.getParameterName(bParam);
                    // tr.Ace.log("bName: " + bName);
                    addChanged(PARAMETER_ADDED, new Object[] { bName.image }, afp, bParam,tipoElemento);
                    return true;
                }
            }
        }
        return false;
    }
    
    protected boolean compararLancamentoExcecao(SimpleNode a, ASTNameList at, SimpleNode b, ASTNameList bt, Relatorio.TipoDeclaracao tipoElemento)
    {
        if (at == null) {
            if (bt == null) {
                // tr.Ace.log("no change in throws");
            	return false;
            }
            else {
                ASTName[] names = (ASTName[])SimpleNodeUtil.findChildren(bt, ASTName.class);
                for (int ni = 0; ni < names.length; ++ni) {
                    addChanged(THROWS_ADDED, new Object[] { SimpleNodeUtil.toString(names[ni]) }, a, names[ni],tipoElemento);
                    return true;
                }
            }
        }
        else if (bt == null) {
            ASTName[] names = (ASTName[])SimpleNodeUtil.findChildren(at, ASTName.class);
            for (int ni = 0; ni < names.length; ++ni) {
                addChanged(THROWS_REMOVED, new Object[] { SimpleNodeUtil.toString(names[ni]) }, names[ni], b, tipoElemento);
                return true;
            }
        }
        else {
            ASTName[] aNames = (ASTName[])SimpleNodeUtil.findChildren(at, ASTName.class);
            ASTName[] bNames = (ASTName[])SimpleNodeUtil.findChildren(bt, ASTName.class);

            for (int ai = 0; ai < aNames.length; ++ai) {
                // save a reference to the name here, in case it gets removed
                // from the array in getMatch.
                ASTName aName = aNames[ai];

                int throwsMatch = getMatch(aNames, ai, bNames);

                // tr.Ace.log("throwsMatch: " + throwsMatch);

                if (throwsMatch == ai) {
                    // tr.Ace.log("exact match");
                	return false;
                }
                else if (throwsMatch >= 0) {
                    // tr.Ace.log("misordered match");
                    ASTName bName = ThrowsUtil.getNameNode(bt, throwsMatch);
                    // tr.Ace.log("aName: " + aName + "; bName: " + bName);
                    String aNameStr = SimpleNodeUtil.toString(aName);
                    addChanged(THROWS_REORDERED,
                               new Object[] { aNameStr, new Integer(ai), new Integer(throwsMatch) },
                               aName,
                               bName, tipoElemento);
                    return true;
                }
                else {
                    // tr.Ace.log("not a match; aName: " + aName);
                    addChanged(THROWS_REMOVED, new Object[] { SimpleNodeUtil.toString(aName) }, aName, bt, tipoElemento);
                    return true;
                }
            }

            for (int bi = 0; bi < bNames.length; ++bi) {
                // tr.Ace.log("b: " + bNames[bi]);

                if (bNames[bi] == null) {
                    // tr.Ace.log("already processed");
                }
                else {
                    ASTName bName = ThrowsUtil.getNameNode(bt, bi);
                    // tr.Ace.log("bName: " + bName);
                    addChanged(THROWS_ADDED, new Object[] { SimpleNodeUtil.toString(bName) }, at, bName,tipoElemento);
                    return true;
                }
            }
        }
        return false;
    }
    
    // TODO: Comparacao do Corpo do Metodo para Diff da Alteracao de um Metodo
    public boolean compararCorpo(ASTMethodDeclaration a, ASTMethodDeclaration b,Relatorio.TipoDeclaracao tipoElemento)
    {
        final int ABSTRACT_METHOD_CHILDREN = 2;

        ASTBlock aBlock = (ASTBlock)SimpleNodeUtil.findChild(a, ASTBlock.class);
        ASTBlock bBlock = (ASTBlock)SimpleNodeUtil.findChild(b, ASTBlock.class);

        if (aBlock == null) {
            if (bBlock == null) {
                // neither has a block, so no change
            	return false;
            }
            else {
                addChanged(METHOD_BLOCK_ADDED, a, b, tipoElemento);
                return true;
            }
        }
        else if (bBlock == null) {
            addChanged(METHOD_BLOCK_REMOVED, a, b, tipoElemento);
            return true;
        }
        else {
            String aName = MethodUtil.getFullName(a);
            String bName = MethodUtil.getFullName(b);
            
            return compararCorpo(aName, aBlock, bName, bBlock, tipoElemento);
        }
    }
    
    protected boolean compararCorpo(String aName, ASTBlock aBlock, String bName, ASTBlock bBlock, Relatorio.TipoDeclaracao tipoElemento)
    {
        List a = SimpleNodeUtil.getChildrenSerially(aBlock);
        List b = SimpleNodeUtil.getChildrenSerially(bBlock);

        return compararCorpo(aName, a, bName, b, tipoElemento);
    }


}
