import antlr.CL;
import antlr.CLLexer;
import ast.CLProgram;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import syntax.CLCompiler;

import java.io.IOException;

public class Interpreter {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("usage: parser [filename]");
            return;
        }
        System.out.printf("Analyzing filename: %s\n", args[0]);
        CLLexer lexer = new CLLexer(CharStreams.fromFileName(args[0]));
        CL parser = new CL(new CommonTokenStream(lexer));
        ParseTree parseTree = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        CLCompiler listener = new CLCompiler();
        walker.walk(listener, parseTree);

        if (!listener.getErrors().isEmpty()) {
            System.out.println("Error occurred");
            System.out.println(listener.getErrors());
        }
        assert listener.getAst().peek() instanceof CLProgram;
        CLProgram CLProgram = (CLProgram) listener.getAst().peek();
        System.out.println(CLProgram);
        System.out.println(listener.getVars());
        System.out.println(listener.getGlobal());
    }
}
