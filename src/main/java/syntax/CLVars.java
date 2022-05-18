package syntax;

import ast.CLExpression;
import lombok.ToString;
import org.antlr.v4.runtime.misc.Pair;

import java.util.HashMap;
import java.util.Map;

@ToString
public class CLVars {
    private Map<String, Map<String, Boolean>> initialized = new HashMap<>();
    private Map<String, Map<String, CLExpression>> value = new HashMap<>();

    public void insert(String varName, String blockName, boolean isInitialized) {
        if (initialized.containsKey(blockName)) {
            initialized.get(blockName).put(varName, isInitialized);
        }
        else {
            initialized.put(blockName, new HashMap<>());
            value.put(blockName, new HashMap<>());
            initialized.get(blockName).put(varName, isInitialized);
        }
    }

    public void addInitExpression(String varName, String blockName, CLExpression expression) {
        if (!contains(varName, blockName)) {
            throw new RuntimeException("no such variable");
        }
        initialized.get(blockName).put(varName, true);
        value.get(blockName).put(varName, expression);
    }

    public boolean isInitialized(String varName, String blockName) {
        if (!contains(varName, blockName)) {
            throw new RuntimeException("no such variable");
        }
        return initialized.get(blockName).get(varName);
    }

    public boolean contains(String varName, String blockName) {
        return initialized.containsKey(blockName) && initialized.get(blockName).containsKey(varName);
    }

}
