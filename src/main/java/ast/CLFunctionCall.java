package ast;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class CLFunctionCall implements CLExpression {
    public CLExpression func = null;
    public List<CLExpression> args = new ArrayList<>();
}
