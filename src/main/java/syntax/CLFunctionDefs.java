package syntax;

import ast.CLFunctionDef;

import java.util.HashMap;
import java.util.Map;

public class CLFunctionDefs {
    public Map<String, Map<String, CLFunctionDef>> def = new HashMap();

    public boolean contains(String funcName, String blockName) {
        return def.containsKey(blockName) && def.get(blockName).containsKey(funcName);
    }

    public void add(CLFunctionDef item) {
        if (!def.containsKey(item.parent.getName())) {
            def.put(item.parent.getName(), new HashMap<>());
        }
        def.get(item.parent.getName()).put(item.getName(), item);
    }
}
