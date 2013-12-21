package de.mirkosertic.gameengine.types;

import java.util.HashMap;
import java.util.Map;

public class TextExpression {

    public final String expression;

    public TextExpression(String aExpression) {
        expression = aExpression;
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
