package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;

public class TextExpression {

    public final String expression;

    public TextExpression(String aExpression) {
        expression = aExpression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextExpression that = (TextExpression) o;

        if (!expression.equals(that.expression)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("expression", expression);
        return theResult;
    }

    public static TextExpression deserialize(Map<String,Object> aSerializedData) {
        String theExpression = (String) aSerializedData.get("expression");
        return new TextExpression(theExpression);
    }
}
