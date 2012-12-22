package diffj.org.incava.diffj;

import java.awt.Point;
import java.io.*;
import java.util.*;

import diffj.net.sourceforge.pmd.ast.*;
import diffj.org.incava.analysis.*;
import diffj.org.incava.java.*;
import diffj.org.incava.util.*;
import diffj.org.incava.util.diff.*;
import dubaj.tr.Relatorio;



public class FunctionDiff extends ItemDiff
{
    

    public static final String PARAMETER_REMOVED = "parameter removed: {0}";

    public static final String PARAMETER_ADDED = "parameter added: {0}";

    public static final String PARAMETER_REORDERED = "parameter {0} reordered from argument {1} to {2}";

    public static final String PARAMETER_TYPE_CHANGED = "parameter type changed from {0} to {1}";
    
    public static final String PARAMETER_NAME_CHANGED = "parameter name changed from {0} to {1}";

    public static final String PARAMETER_REORDERED_AND_RENAMED = "parameter {0} reordered from argument {1} to {2} and renamed {3}";

    public static final String THROWS_REMOVED = "throws removed: {0}";

    public static final String THROWS_ADDED = "throws added: {0}";

    public static final String THROWS_REORDERED = "throws {0} reordered from argument {1} to {2}";

    public FunctionDiff(Report report)
    {
        super(report);
    }

    public FunctionDiff(Collection differences)
    {
        super(differences);
    }

    protected void compareReturnTypes(SimpleNode a, SimpleNode b,Relatorio.TipoDeclaracao tipoElemento)
    {
        SimpleNode art    = (SimpleNode)a.jjtGetChild(0);
        SimpleNode brt    = (SimpleNode)b.jjtGetChild(0);
        String     artStr = SimpleNodeUtil.toString(art);
        String     brtStr = SimpleNodeUtil.toString(brt);
        // tr.Ace.log("art: " + art + "; brt: " + brt);

        if (artStr.equals(brtStr)) {
            // tr.Ace.log("no change in return types");
        }
        else {
            addChanged(RETURN_TYPE_CHANGED, new Object[] { artStr, brtStr }, art, brt, tipoElemento);
        }
    }

    protected void compareParameters(ASTFormalParameters afp, ASTFormalParameters bfp,Relatorio.TipoDeclaracao tipoElemento)
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
                    }
                }
                else if (paramMatch[1] >= 0) {
                    System.out.println("misordered match by name");
                    
                    dubaj.tr.Ace.log("misordered match by name");

                    ASTFormalParameter bParam = ParameterUtil.getParameter(bfp, paramMatch[1]);

                    addChanged(PARAMETER_REORDERED, new Object[] { aNameTk.image, new Integer(ai), new Integer(paramMatch[1]) }, aParam, bParam,tipoElemento);
                }
                else {
                    // tr.Ace.log("not a match");
                    // tr.Ace.log("aNameTk: " + aNameTk);
                    addChanged(PARAMETER_REMOVED, new Object[] { aNameTk.image }, aParam, bfp,tipoElemento);
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
                }
            }
        }
    }

    protected void compareThrows(SimpleNode a, ASTNameList at, SimpleNode b, ASTNameList bt, Relatorio.TipoDeclaracao tipoElemento)
    {
        if (at == null) {
            if (bt == null) {
                // tr.Ace.log("no change in throws");
            }
            else {
                ASTName[] names = (ASTName[])SimpleNodeUtil.findChildren(bt, ASTName.class);
                for (int ni = 0; ni < names.length; ++ni) {
                    addChanged(THROWS_ADDED, new Object[] { SimpleNodeUtil.toString(names[ni]) }, a, names[ni],tipoElemento);
                }
            }
        }
        else if (bt == null) {
            ASTName[] names = (ASTName[])SimpleNodeUtil.findChildren(at, ASTName.class);
            for (int ni = 0; ni < names.length; ++ni) {
                addChanged(THROWS_REMOVED, new Object[] { SimpleNodeUtil.toString(names[ni]) }, names[ni], b, tipoElemento);
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
                }
                else {
                    // tr.Ace.log("not a match; aName: " + aName);
                    addChanged(THROWS_REMOVED, new Object[] { SimpleNodeUtil.toString(aName) }, aName, bt, tipoElemento);
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
                }
            }
        }
    }

    protected int getMatch(ASTName[] aNames, int aIndex, ASTName[] bNames)
    {
        String aNameStr = SimpleNodeUtil.toString(aNames[aIndex]);

        // // tr.Ace.log("aNameStr: " + aNameStr);

        for (int bi = 0; bi < bNames.length; ++bi) {
            if (bNames[bi] == null) {
                // // tr.Ace.log("already consumed");
            }
            else if (SimpleNodeUtil.toString(bNames[bi]).equals(aNameStr)) {
                aNames[aIndex] = null;
                bNames[bi]     = null;
                return bi;
            }
        }

        return -1;
    }

}
