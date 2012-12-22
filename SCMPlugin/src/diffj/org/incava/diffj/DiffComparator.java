package diffj.org.incava.diffj;

import java.text.MessageFormat;
import java.util.*;

import diffj.net.sourceforge.pmd.ast.SimpleNode;
import diffj.net.sourceforge.pmd.ast.Token;
import diffj.org.incava.analysis.CodeReference;
import diffj.org.incava.analysis.Report;
import diffj.org.incava.java.SimpleNodeUtil;
import dubaj.tr.Relatorio;
import dubaj.tr.Relatorio.TipoDeclaracao;



public class DiffComparator
{
    private Report _report;
    
    private Collection _differences;

    public DiffComparator(Report report)
    {
        _report = report;
        _differences = _report.getDifferences();
    }

    public DiffComparator(Collection diffs)
    {
        _differences = diffs;
    }

    public DiffComparator()
    {
        this(new ArrayList());
    }

    public Collection get()
    {
        return _differences;
    }

    // -------------------------------------------------------

    public void addAdded(String msg, Object[] params, Token a0, Token a1, Token b0, Token b1, Relatorio.TipoDeclaracao tipoElemento)
    {
        add(CodeReference.ADDED, msg, params, a0, a1, b0, b1, tipoElemento);
    }

    public void addAdded(String msg, Object[] params, Token a, Token b, Relatorio.TipoDeclaracao tipoElemento)
    {
        String str = MessageFormat.format(msg, params);
        add(new CodeReference(CodeReference.ADDED, str, a, b, tipoElemento));
    }

    public void addAdded(String msg, Token a, Token b, Relatorio.TipoDeclaracao tipoElemento)
    {
        Object[] params = toParameters(null, b);
        addAdded(msg, params, a, b, tipoElemento);
    }

    public void addAdded(String msg, Object[] params, SimpleNode a, SimpleNode b, Relatorio.TipoDeclaracao tipoElemento)
    {
        addAdded(msg, params, a.getFirstToken(), a.getFirstToken(), b.getFirstToken(), b.getLastToken(), tipoElemento);
    }

    public void addAdded(String msg, SimpleNode a, SimpleNode b, Relatorio.TipoDeclaracao tipoElemento)
    {
        Object[] params = toParameters(null, b);
        addAdded(msg, params, a, b, tipoElemento);
    }

    public void addAdded(String msg, Token a0, Token a1, Token b0, Token b1, Relatorio.TipoDeclaracao tipoElemento)
    {
        Object[] params = toParameters(a0, b0);
        addAdded(msg, params, a0, a1, b0, b1,tipoElemento);
    }

    // -------------------------------------------------------

    public void addChanged(String msg, Object[] params, Token a0, Token a1, Token b0, Token b1,Relatorio.TipoDeclaracao tipoElemento)
    {
        add(CodeReference.CHANGED, msg, params, a0, a1, b0, b1, tipoElemento);
    }

    public void addChanged(String msg, Object[] params, SimpleNode a, SimpleNode b, Relatorio.TipoDeclaracao tipoElemento)
    {
        addChanged(msg, params, a.getFirstToken(), a.getLastToken(), b.getFirstToken(), b.getLastToken(),tipoElemento);
    }

    public void addChanged(String msg, Object[] params, Token a, Token b, Relatorio.TipoDeclaracao tipoElemento)
    {
        String str = MessageFormat.format(msg, params);
        add(new CodeReference(CodeReference.CHANGED, str, a, b,tipoElemento));
    }

    public void addChanged(String msg, Token a, Token b,Relatorio.TipoDeclaracao tipoElemento)
    {
        Object[] params = toParameters(a, b);
        addChanged(msg, params, a, b, tipoElemento);
    }

    public void addChanged(String msg, SimpleNode a, SimpleNode b, Relatorio.TipoDeclaracao tipoElemento)
    {
        Object[] params = toParameters(a, b);
        addChanged(msg, params, a, b, tipoElemento);
    }

    // -------------------------------------------------------

    public void addDeleted(String msg, Object[] params, Token a0, Token a1, Token b0, Token b1, Relatorio.TipoDeclaracao tipoElemento)
    {
        add(CodeReference.DELETED, msg, params, a0, a1, b0, b1, tipoElemento);
    }

    public void addDeleted(String msg, Object[] params, SimpleNode a, SimpleNode b, Relatorio.TipoDeclaracao tipoElemento)
    {
        addDeleted(msg, params, a.getFirstToken(), a.getLastToken(), b.getFirstToken(), b.getLastToken(), tipoElemento);
    }

    public void addDeleted(String msg, Object[] params, Token a, Token b, Relatorio.TipoDeclaracao tipoElemento)
    {
        String str = MessageFormat.format(msg, params);
        add(new CodeReference(CodeReference.DELETED, str, a, b, tipoElemento));
    }

    public void addDeleted(String msg, Token a, Token b, Relatorio.TipoDeclaracao tipoElemento)
    {
        Object[] params = toParameters(a, null);
        addDeleted(msg, params, a, b, tipoElemento);
    }

    public void addDeleted(String msg, SimpleNode a, SimpleNode b, Relatorio.TipoDeclaracao tipoElemento)
    {
        Object[] params = toParameters(a, null);
        addDeleted(msg, params, a, b, tipoElemento);
    }

    // -------------------------------------------------------

    protected void add(String type, String msg, Token a, Token b, Relatorio.TipoDeclaracao tipoElemento)
    {
        Object[] params = toParameters(a, b);
        add(type, msg, params, a, b, tipoElemento);
    }

    protected void add(String type, String msg, Object[] params, Token a, Token b, Relatorio.TipoDeclaracao tipoElemento)
    {
        String str = MessageFormat.format(msg, params);
        add(new CodeReference(type, str, a, b, tipoElemento));
    }

    protected void add(String type, String msg, SimpleNode a, SimpleNode b, Relatorio.TipoDeclaracao tipoElemento)
    {
        Object[] params = toParameters(a, b);
        add(type, msg, params, a, b, tipoElemento);
    }

    protected void add(String type, String msg, Object[] params, SimpleNode a, SimpleNode b, Relatorio.TipoDeclaracao tipoElemento)
    {
        add(type,
            msg,
            params,
            a == null ? null : a.getFirstToken(),
            a == null ? null : a.getLastToken(),
            b == null ? null : b.getFirstToken(), 
            b == null ? null : b.getLastToken(),
            tipoElemento);
    }

    protected void add(String type, String msg, Object[] params, Token a0, Token a1, Token b0, Token b1, Relatorio.TipoDeclaracao tipoElemento)
    {
        String str = MessageFormat.format(msg, params);
        add(new CodeReference(type, str, a0, a1, b0, b1,tipoElemento));
    }

    protected void add(CodeReference ref)
    {
        dubaj.tr.Ace.stack("ref: " + ref);
        _differences.add(ref);
    }

    protected Object[] toParameters(Token a, Token b)
    {
        List params = new ArrayList();
        if (a != null) {
            params.add(a.image);
        }
        if (b != null) {
            params.add(b.image);
        }
        return params.toArray();
    }

    protected Object[] toParameters(SimpleNode a, SimpleNode b)
    {
        List params = new ArrayList();
        if (a != null) {
            params.add(SimpleNodeUtil.toString(a));
        }
        if (b != null) {
            params.add(SimpleNodeUtil.toString(b));
        }
        return params.toArray();
    }

}
