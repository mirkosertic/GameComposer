package de.mirkosertic.gameengine.generic;

import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;

import java.util.HashMap;
import java.util.Map;

public class CSSCache {

    private final Map<Font, String> fontCache;
    private final Map<Color, String> colorCache;

    public CSSCache() {
        fontCache = new HashMap<>();
        colorCache = new HashMap<>();
    }

    public String toFont(Font aFont) {
        String theCSS = fontCache.get(aFont);
        if (theCSS == null) {
            theCSS = CSSUtils.toFont(aFont);
            fontCache.put(aFont, theCSS);
        }
        return theCSS;
    }

    public String toColor(Color aColor) {
        String theCSS = colorCache.get(aColor);
        if (theCSS == null) {
            theCSS = CSSUtils.toColor(aColor);
            colorCache.put(aColor, theCSS);
        }
        return theCSS;
    }
}