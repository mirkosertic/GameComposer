package de.mirkosertic.gameengine.expression;

import java.util.HashMap;
import java.util.Map;

public class DefaultFunctionRegistry implements FunctionRegistry {

    private final Map<String, Function> functions;

    public DefaultFunctionRegistry() {
        functions = new HashMap<String, Function>();
        functions.put("max", new MaxFunction());
        functions.put("min", new MinFunction());
    }

    public void register(String aFunctionName, Function aFunction) {
        functions.put(aFunctionName, aFunction);
    }

    @Override
    public Function resolveFunctionByName(String aFunctionName) {
        return functions.get(aFunctionName);
    }
}
