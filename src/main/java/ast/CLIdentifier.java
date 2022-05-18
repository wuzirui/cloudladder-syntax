package ast;

import lombok.ToString;

@ToString
public class CLIdentifier implements CLLeftVal {
    public CLIdentifier(String name) {
        this.name = name;
    }

    public String name = null;
}
