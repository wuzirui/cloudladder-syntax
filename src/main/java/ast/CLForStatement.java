package ast;

public class CLForStatement implements CLStatement {
    public CLStatement init = null;
    public CLExpression condition = null;
    public CLAssignmentStatement post = null;
    public CLStatement body = null;

    @Override
    public String toString() {
        return "CLForStatement{" +
                "\n\tinit=" + init +
                "\n\t, condition=" + condition +
                "\n\t, post=" + post +
                "\n\t, body=" + body +
                '}';
    }
}
