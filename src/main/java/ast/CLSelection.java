package ast;

public class CLSelection implements CLStatement {
    public CLExpression condition = null;
    public CLStatement ThenClause = null;
    public CLStatement ElseClause = null;

    @Override
    public String toString() {
        return "CLSelection{" +
                "\n\tcondition=" + condition +
                "\n\t, ThenClause=" + ThenClause +
                "\n\t, ElseClause=" + ElseClause +
                "\n}";
    }
}
