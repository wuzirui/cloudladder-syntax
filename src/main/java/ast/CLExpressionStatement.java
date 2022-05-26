package ast;

import lombok.ToString;

@ToString
public class CLExpressionStatement implements ASTNode, CLStatement {
    public CLExpression expression = null;
}
