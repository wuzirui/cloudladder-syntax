package ast;

import lombok.Getter;
import syntax.CLVars;

import java.util.ArrayList;
import java.util.List;

public class CLFunctionDef implements ASTNode, CLProgramItem {
    @Getter
    private String name;
    @Getter
    private List<String> args = new ArrayList<>();
    public CLBlock parent;
    @Getter
    private CLBlock body;

    public CLCompoundStatment children;

    public CLFunctionDef(String name, CLBlock parent) {
        this.name = name;
        this.parent = parent;
        body = new CLBlock();
        body.setParent(parent);
    }

    public void addArg(CLVars varTable, String name) {
        args.add(name);
        body.autoRegisterMember(varTable, name);
    }

    @Override
    public String toString() {
        return "CLFunctionDef{" +
                "name='" + name + '\'' +
                "\n\t, args=" + args +
                "\n\t, parent=" + parent.getName() +
                "\n\t, body=" + body.getName() +
                "\n\t, children=" + children +
                "\n}";
    }
}
