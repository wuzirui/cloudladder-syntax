package ast;

import lombok.ToString;

@ToString
public class CLUnaryExpression implements CLExpression {
    public String op;
    public CLExpression operand = null;

    public CLUnaryExpression(String op) {
        this.op = op;
    }
}
