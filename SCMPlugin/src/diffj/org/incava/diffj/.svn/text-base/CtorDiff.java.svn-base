package diffj.org.incava.diffj;

import java.awt.Point;
import java.text.MessageFormat;
import java.util.*;



import diffj.net.sourceforge.pmd.ast.*;
import diffj.org.incava.analysis.CodeReference;
import diffj.org.incava.analysis.Report;
import diffj.org.incava.java.*;
import diffj.org.incava.util.DefaultComparator;
import diffj.org.incava.util.diff.Diff;
import diffj.org.incava.util.diff.Difference;
import dubaj.tr.Relatorio;



public class CtorDiff extends FunctionDiff
{
    public CtorDiff(Report report)
    {
        super(report);
    }

    public CtorDiff(Collection differences)
    {
        super(differences);
    }
    
    public boolean comparar(ASTConstructorDeclaration a, ASTConstructorDeclaration b, Relatorio.TipoDeclaracao tipoElemento)
    {
    	if (compararParametros(a, b, tipoElemento)) {
    		return true;
    	}
    	
    	if (compararLancamentoExcecao(a, b, tipoElemento)) {
    		return true;
    	}
    	
    	if (compararCorpo(a, b, tipoElemento)) {
    		return true;
    	}
    	
    	return false;
    }

    public void compare(ASTConstructorDeclaration a, ASTConstructorDeclaration b, Relatorio.TipoDeclaracao tipoElemento)
    {
        dubaj.tr.Ace.log("a: " + a + "; b: " + b);
        
        compareParameters(a, b, tipoElemento);
        compareThrows(a, b, tipoElemento);
        compareBodies(a, b, tipoElemento);
    }
    
    protected boolean compararParametros(ASTConstructorDeclaration a, ASTConstructorDeclaration b, Relatorio.TipoDeclaracao tipoElemento)
    {
        ASTFormalParameters afp = CtorUtil.getParameters(a);
        ASTFormalParameters bfp = CtorUtil.getParameters(b);

        return compararParametros(afp, bfp, tipoElemento);
    }

    protected void compareParameters(ASTConstructorDeclaration a, ASTConstructorDeclaration b, Relatorio.TipoDeclaracao tipoElemento)
    {
        ASTFormalParameters afp = CtorUtil.getParameters(a);
        ASTFormalParameters bfp = CtorUtil.getParameters(b);

        compareParameters(afp, bfp, tipoElemento);
    }

    protected void compareThrows(ASTConstructorDeclaration a, ASTConstructorDeclaration b, Relatorio.TipoDeclaracao tipoElemento)
    {
        ASTNameList at = CtorUtil.getThrowsList(a);
        ASTNameList bt = CtorUtil.getThrowsList(b);

        compareThrows(a, at, b, bt, tipoElemento);
    }

    protected List getCodeSerially(ASTConstructorDeclaration ctor)
    {
        // removes all tokens up to the first left brace. This is because ctors
        // don't have their own blocks, unlike methods.
        
        List children = SimpleNodeUtil.getChildrenSerially(ctor);
        
        Iterator it = children.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof Token && ((Token)obj).kind == JavaParserConstants.LBRACE) {
                // tr.Ace.log("breaking at: " + obj);
                break;
            }
            else {
                // tr.Ace.log("deleting: " + obj);
                it.remove();
            }
        }

        return children;
    }

    protected void compareBodies(ASTConstructorDeclaration a, ASTConstructorDeclaration b, Relatorio.TipoDeclaracao tipoElemento)
    {
        List aCode = getCodeSerially(a);
        List bCode = getCodeSerially(b);
        
        String aName = CtorUtil.getFullName(a);
        String bName = CtorUtil.getFullName(b);

        compareCode(aName, aCode, bName, bCode, tipoElemento);
    }
    
    protected boolean compararParametros(ASTFormalParameters afp, ASTFormalParameters bfp,Relatorio.TipoDeclaracao tipoElemento)
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
    
    protected boolean compararLancamentoExcecao(ASTConstructorDeclaration a, ASTConstructorDeclaration b, Relatorio.TipoDeclaracao tipoElemento)
    {
        ASTNameList at = CtorUtil.getThrowsList(a);
        ASTNameList bt = CtorUtil.getThrowsList(b);

        return compararLancamentoExcecao(a, at, b, bt, tipoElemento);
    }
    
    protected boolean compararLancamentoExcecao(SimpleNode a, ASTNameList at, SimpleNode b, ASTNameList bt, Relatorio.TipoDeclaracao tipoElemento)
    {
        if (at == null) {
            if (bt == null) {
                // tr.Ace.log("no change in throws");
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
    
    protected boolean compararCorpo(ASTConstructorDeclaration a, ASTConstructorDeclaration b, Relatorio.TipoDeclaracao tipoElemento)
    {
        List aCode = getCodeSerially(a);
        List bCode = getCodeSerially(b);
        
        String aName = CtorUtil.getFullName(a);
        String bName = CtorUtil.getFullName(b);

        return compararCorpo(aName, aCode, bName, bCode, tipoElemento);
    }
    
    protected boolean compararCorpo(String aName, List a, String bName, List b,
			Relatorio.TipoDeclaracao tipoElemento) {
		Diff d = new Diff(a, b, new DefaultComparator() {
			public int doCompare(Object x, Object y) {
				if (x instanceof Token) {
					if (y instanceof Token) {
						Token xt = (Token) x;
						Token yt = (Token) y;
						int cmp = xt.kind < yt.kind ? -1
								: (xt.kind > yt.kind ? 1 : 0);
						if (xt.kind == yt.kind) {
						}
						if (cmp == 0) {
							cmp = xt.image.compareTo(yt.image);
						}
						return cmp;
					} else {
						return 1;
					}
				} else if (x instanceof SimpleNode) {
					if (y instanceof SimpleNode) {
						SimpleNode xn = (SimpleNode) x;
						SimpleNode yn = (SimpleNode) y;
						int cmp = xn.getClass().getName()
								.compareTo(yn.getClass().getName());
						return cmp;
					} else {
						return -1;
					}
				} else {
					return -1;
				}
			}
		});

		CodeReference ref = null;
		List diffList = d.diff();

		// tr.Ace.log("diffList", diffList);

		Iterator dit = diffList.iterator();

		Token lastA = null;
		Token lastB = null;

		while (dit.hasNext()) {
			Difference diff = (Difference) dit.next();

			int delStart = diff.getDeletedStart();
			int delEnd = diff.getDeletedEnd();
			int addStart = diff.getAddedStart();
			int addEnd = diff.getAddedEnd();

			// tr.Ace.log("diff", diff);

			String msg = null;
			Token aStart = null;
			Token aEnd = null;
			Token bStart = null;
			Token bEnd = null;

			if (delEnd == Difference.NONE) {
				if (addEnd == Difference.NONE) {
					// WTF?
					return false;
				} else {
					aStart = getStart(a, delStart);
					aEnd = aStart;
					bStart = (Token) b.get(addStart);
					bEnd = (Token) b.get(addEnd);
					msg = CODE_ADDED;
					return true;
				}
			} else if (addEnd == Difference.NONE) {
				aStart = (Token) a.get(delStart);
				aEnd = (Token) a.get(delEnd);
				bStart = getStart(b, addStart);
				bEnd = bStart;
				msg = CODE_REMOVED;
				return true;
			} else {
				aStart = (Token) a.get(delStart);
				aEnd = (Token) a.get(delEnd);
				bStart = (Token) b.get(addStart);
				bEnd = (Token) b.get(addEnd);
				msg = CODE_CHANGED;
				return true;
			}
		}
		return false;
	}

}
