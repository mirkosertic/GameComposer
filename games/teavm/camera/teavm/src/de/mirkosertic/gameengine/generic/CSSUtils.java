package de.mirkosertic.gameengine.generic;

import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;

public class CSSUtils {

    public static String toFont(Font aFont) {
        switch (aFont.name) {
            case ARIAL:
                return Integer.toString(aFont.size)+"px arial";
            case VERDANA:
                return Integer.toString(aFont.size)+"px verdana";
        }
        throw new IllegalArgumentException("Wrong font name : "+aFont.name);
    }

    public static String toColor(Color aColor) {
        return "rgb(" + aColor.r + "," + aColor.g + "," + aColor.b + ")";
    }
}