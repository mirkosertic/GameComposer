package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;

public class TextExpression implements Distributable {

    public final String expression;

    public TextExpression(String aExpression) {
        expression = aExpression;
    }

    public String get() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextExpression that = (TextExpression) o;

        return expression.equals(that.expression);

    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("expression", expression);
        return theResult;
    }

    public static TextExpression deserialize(Map<String,Object> aSerializedData) {
        String theExpression = (String) aSerializedData.get("expression");
        return new TextExpression(theExpression);
    }
}
