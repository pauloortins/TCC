/* Generated By:JJTree: Do not edit this line. ASTAdditiveExpression.java */

package diffj.net.sourceforge.pmd.ast;

public class ASTAdditiveExpression extends SimpleJavaNode {
    public ASTAdditiveExpression(int id) {
        super(id);
    }

    public ASTAdditiveExpression(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

}
