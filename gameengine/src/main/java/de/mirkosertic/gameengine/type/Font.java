package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;

public class Font {

    public static enum FontName {
        ARIAL, VERDANA
    }

    public static final Font DEFAULT_FONT = new Font(FontName.ARIAL, 10);

    public final FontName name;
    public final int size;

    public Font(FontName aFontName, int aSize) {
        name = aFontName;
        size = aSize;
    }

    public Font changeSize(int aSize) {
        return new Font(name, aSize);
    }

    public Font changeName(FontName aName) {
        return new Font(aName, size);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("name", name.name());
        theResult.put("size", Integer.toString(size));
        return theResult;
    }

    public static Font deserialize(Map<String, Object> aSerializedData) {
        String theFontName = (String) aSerializedData.get("name");
        String theSize = (String) aSerializedData.get("size");
        return new Font(FontName.valueOf(theFontName), Integer.parseInt(theSize));
    }
}
