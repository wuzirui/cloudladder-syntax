package ast;

import lombok.ToString;

@ToString
public class CLError {
    public String type;
    public String msg;

    public CLError(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
