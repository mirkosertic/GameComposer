package de.mirkosertic.gameengine.expression.ge;

public class DefaultVariableResolver implements VariableResolver {

    @Override
    public Object resolveVariable(String aVariableName) {
        return aVariableName;
    }
}
