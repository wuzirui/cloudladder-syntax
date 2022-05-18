package ast;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class CLLeftAssociativeExpression implements CLExpression {
    public List<CLExpression> operands = new ArrayList<>();
    public final String op;

    public CLLeftAssociativeExpression(String op) {
        this.op = op;
    }
}
