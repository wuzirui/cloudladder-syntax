package ast;

import lombok.ToString;

@ToString
public class CLAssignmentStatement implements CLStatement {
    public CLLeftVal dest;
    public CLExpression expression;
}
