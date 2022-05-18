package ast;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import syntax.CLVars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CLBlock {
    @Getter
    private final String name;

    @Getter
    private final List<CLBlock> children = new ArrayList<>();

    @Getter
    @Setter
    private CLBlock parent = null;

    public CLBlock() {
        name = UUID.randomUUID().toString();
    }

    public void autoRegisterMember(CLVars varTable, String varname) {
        varTable.insert(varname, this.name, false);
    }

    @Override
    public String toString() {
        return "CLBlock{\n\t" +
                "name='" + name + "'\n\t"+
                ", children=[" + children.stream().map((e) -> e.name).reduce("", (x, y) ->  x + " (" + y + ")") +
                "]\n\t, parent=" + (parent != null ? parent.name : "null" )+
                "\n}";
    }
}
