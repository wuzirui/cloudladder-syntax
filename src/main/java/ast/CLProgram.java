package ast;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class CLProgram implements ASTNode {
    @Getter
    private List<CLProgramItem> items = new ArrayList<>();

    public void addItem(CLProgramItem item) {
        items.add(item);
    }
}
