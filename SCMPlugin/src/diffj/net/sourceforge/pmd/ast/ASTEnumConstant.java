/* Generated By:JJTree: Do not edit this line. ASTEnumConstant.java */

package diffj.net.sourceforge.pmd.ast;

public class ASTEnumConstant extends SimpleJavaNode {
    public ASTEnumConstant(int id) {
        super(id);
    }

    public ASTEnumConstant(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}