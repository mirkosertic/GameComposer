package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;

public class Font implements Distributable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Font font = (Font) o;

        if (size != font.size) return false;
        if (name != font.name) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Font changeSize(int aSize) {
        return new Font(name, aSize);
    }

    public Font changeName(FontName aName) {
        return new Font(aName, size);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
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
