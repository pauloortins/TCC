/* Generated By:JJTree: Do not edit this line. ASTRelationalExpression.java */

package diffj.net.sourceforge.pmd.ast;

public class ASTRelationalExpression extends SimpleJavaNode {
    public ASTRelationalExpression(int id) {
        super(id);
    }

    public ASTRelationalExpression(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
