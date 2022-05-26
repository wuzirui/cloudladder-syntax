package ast;

public class CLWhileStatement implements CLStatement {
    public CLExpression condition = null;
    public CLStatement body = null;

    @Override
    public String toString() {
        return "CLWhileStatement{" +
                "\n\tcondition=" + condition +
                "\n\t, body=" + body +
                '}';
    }
}
