package ast;

import antlr.CL;
import lombok.ToString;

public class CLPrimaryExpression implements ASTNode, CLExpression {
    public CL.PrimaryExpressionContext expression = null;

    public CLPrimaryExpression(CL.PrimaryExpressionContext expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "CLPrimaryExpression{" +
                "content='" + expression.getText() + "'" +
                '}';
    }
}
