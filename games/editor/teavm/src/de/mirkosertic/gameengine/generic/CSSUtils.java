package de.mirkosertic.gameengine.generic;

import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;

public class CSSUtils {

    private static final char HEXCODES[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    public static String toFont(Font aFont) {
        switch (aFont.name) {
            case ARIAL:
                return aFont.size + "px arial";
            case VERDANA:
                return aFont.size +"px verdana";
        }
        throw new IllegalArgumentException("Wrong font name : "+aFont.name);
    }

    public static String toFontFamily(Font aFont) {
        switch (aFont.name) {
            case ARIAL:
                return "arial";
            case VERDANA:
                return "verdana";
        }
        throw new IllegalArgumentException("Wrong font name : "+aFont.name);
    }

    public static String toColor(Color aColor) {
        return "rgb(" + aColor.r + "," + aColor.g + "," + aColor.b + ")";
    }

    private static String toHex(int aValue) {
        return "" + HEXCODES[aValue / 16] + HEXCODES[aValue % 16];
    }

    private static int hexIndex(char aChar) {
        for (int i=0;i<HEXCODES.length;i++) {
            if (HEXCODES[i] == aChar) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid hex char : " + aChar);
    }

    private static int fromHex(String aValue) {
        return 16 * hexIndex(aValue.charAt(0)) + hexIndex(aValue.charAt(1));
    }

    public static String toColorHex(Color aColor) {
        return "#" + toHex(aColor.r) + toHex(aColor.g) + toHex(aColor.b);
    }

    public static int toInt(Color aColor) {
        return aColor.b + aColor.g * 256 + aColor.r * 65536;
    }

    public static Color fromColorHex(String aValue) {
        String theR = aValue.substring(1, 3);
        String theG = aValue.substring(3, 5);
        String theB = aValue.substring(5);
        return new Color(fromHex(theR), fromHex(theG), fromHex(theB));
    }
}