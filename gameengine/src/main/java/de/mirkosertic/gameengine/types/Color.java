package de.mirkosertic.gameengine.types;

import java.util.HashMap;
import java.util.Map;

public class Color {

    private int r;
    private int g;
    private int b;

    public Color(int aR, int aG, int aB) {
        r = aR;
        g = aG;
        b = aB;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public Map<String, String> serialize() {
        Map<String, String> theResult = new HashMap<String, String>();
        theResult.put("r", Integer.toString(r));
        theResult.put("g", Integer.toString(g));
        theResult.put("b", Integer.toString(b));
        return theResult;
    }

    public static Color deserialize(Map<String, Object> aSerializedData) {
        int theR = Integer.parseInt((String) aSerializedData.get("r"));
        int theG = Integer.parseInt((String) aSerializedData.get("g"));
        int theB = Integer.parseInt((String) aSerializedData.get("b"));
        return new Color(theR, theG, theB);
    }
}
