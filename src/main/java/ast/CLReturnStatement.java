package ast;

import lombok.ToString;

@ToString
public class CLReturnStatement implements CLStatement {
    public CLExpression value;
}
