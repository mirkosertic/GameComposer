package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;

public class Size {

    public final int width;
    public final int height;

    public Size(int aWidth, int aHeight) {
        width = aWidth;
        height = aHeight;
    }

    public Map<String, Object> serializeToMap() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("width", "" + width);
        theResult.put("height", "" + height);
        return theResult;
    }

    public static Size deserialize(Map<String, Object> aSerializedStructure) {
        int theWidth = Integer.valueOf((String) aSerializedStructure.get("width"));
        int theHeight = Integer.valueOf((String) aSerializedStructure.get("height"));
        return new Size(theWidth, theHeight);
    }
}
