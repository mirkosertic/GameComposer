package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

import java.util.HashMap;
import java.util.Map;

public class Size implements Reflectable<SizeClassInformation> {

    private static final SizeClassInformation CIINSTANCE = new SizeClassInformation();

    @ReflectiveField
    public final int width;

    @ReflectiveField
    public final int height;

    public Size() {
        this(0, 0);
    }

    public Size(int aWidth, int aHeight) {
        width = aWidth;
        height = aHeight;
    }

    @Override
    public SizeClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @ReflectiveMethod
    public Size changeWidth(int aNewWidth) {
        return new Size(aNewWidth, height);
    }

    @ReflectiveMethod
    public Size changeHeight(int aNewHeight) {
        return new Size(width, aNewHeight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Size size = (Size) o;

        if (height != size.height) return false;
        return width == size.width;

    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        return result;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("width", Integer.toString(width));
        theResult.put("height", Integer.toString(height));
        return theResult;
    }

    public static Size deserialize(Map<String, Object> aSerializedStructure) {
        int theWidth = Integer.valueOf((String) aSerializedStructure.get("width"));
        int theHeight = Integer.valueOf((String) aSerializedStructure.get("height"));
        return new Size(theWidth, theHeight);
    }
}
