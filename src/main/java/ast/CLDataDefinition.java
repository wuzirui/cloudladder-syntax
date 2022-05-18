package ast;

import lombok.ToString;
import syntax.CLVars;

public class CLDataDefinition implements CLStatement {
    public String name;
    public boolean isInitialized;
    public CLBlock belongsTo;
    public CLExpression initVal = null;

    public CLDataDefinition(String name, boolean isInitialized, CLBlock belongsTo) {
        this.name = name;
        this.isInitialized = isInitialized;
        this.belongsTo = belongsTo;
    }

    public void addInitExpression(CLVars varTable, CLExpression expression) {
        varTable.addInitExpression(name, belongsTo.getName(), expression);
        isInitialized = true;
        initVal = expression;
    }

    @Override
    public String toString() {
        return "CLDataDefinition{" +
                "\n\tvar name='" + name + '\'' +
                "\n\t, isInitialized=" + isInitialized +
                "\n\t, belongsToBlock=" + belongsTo.getName() +
                "\n\t, initVal=" + initVal +
                "\n}";
    }
}

