/* Generated By:JJTree: Do not edit this line. ASTMultiplicativeExpression.java */

package diffj.net.sourceforge.pmd.ast;

public class ASTMultiplicativeExpression extends SimpleJavaNode {
    public ASTMultiplicativeExpression(int id) {
        super(id);
    }

    public ASTMultiplicativeExpression(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
