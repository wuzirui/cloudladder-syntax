package ast;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class CLCompoundStatment implements ASTNode, CLProgramItem, CLStatement {
    @Getter
    private final CLBlock namespace;

    @Getter
    private final List<CLStatement> children = new ArrayList<>();

    public CLCompoundStatment(CLBlock namespace) {
        this.namespace = namespace;
    }
}
