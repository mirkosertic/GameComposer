package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.generic.CSSCache;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Size;

import java.util.HashMap;
import java.util.Map;
import org.teavm.jso.canvas.CanvasImageSource;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

public class TeaVMPrerenderedTextCache {

    private static class CacheKey {
        private final Font font;
        private final Color color;
        private final String text;
        private final Size size;

        private CacheKey(Font aFont, Color aColor, String aText, Size aSize) {
            font = aFont;
            color = aColor;
            text = aText;
            size = aSize;
        }

        @Override
        public boolean equals(Object aValue) {
            if (this == aValue) {
                return true;
            }
            if (aValue == null || getClass() != aValue.getClass()) {
                return false;
            }

            CacheKey cacheKey = (CacheKey) aValue;

            if (!color.equals(cacheKey.color)) {
                return false;
            }
            if (!font.equals(cacheKey.font)) {
                return false;
            }
            if (!size.equals(cacheKey.size)) {
                return false;
            }
            if (!text.equals(cacheKey.text)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = font.hashCode();
            result = 31 * result + color.hashCode();
            result = 31 * result + text.hashCode();
            result = 31 * result + size.hashCode();
            return result;
        }
    }

    private static class CacheEntry {
        private final Size size;
        private final HTMLCanvasElement canvasElement;

        private CacheEntry(HTMLCanvasElement aCanvasElement, Size aSize) {
            canvasElement = aCanvasElement;
            size = aSize;
        }

        void drawText(CSSCache aCache, Font aFont, Color aColor, String aText) {
            CanvasRenderingContext2D theContext = (CanvasRenderingContext2D) canvasElement.getContext("2d");
            // Make the Canvas fully transparent
            theContext.setFillStyle("rgba(0,0,0,0)");
            theContext.fillRect(0,0, size.width, size.height);

            String theTextColor = aCache.getCSS(aColor);
            theContext.setFillStyle(theTextColor);
            theContext.setStrokeStyle(theTextColor);
            theContext.setFont(aCache.getCSS(aFont));
            theContext.fillText(aText, 0, aFont.size);
        }
    }

    private final HTMLDocument document;
    private final Map<CacheKey, CacheEntry> canvasCache;

    public TeaVMPrerenderedTextCache(HTMLDocument aDocument) {
        document = aDocument;
        canvasCache = new HashMap<>();
    }

    public CanvasImageSource getImageSourceFor(CSSCache aCSSCache, Size aSize, Font aFont, Color aColor, String aText) {
        CacheKey theKey = new CacheKey(aFont, aColor, aText, aSize);
        CacheEntry theEntry = canvasCache.get(theKey);
        if (theEntry == null) {
            HTMLCanvasElement theNewCanvas = (HTMLCanvasElement) document.createElement("canvas");
            theNewCanvas.setWidth(aSize.width);
            theNewCanvas.setHeight(aSize.height);

            theEntry = new CacheEntry(theNewCanvas, aSize);
            theEntry.drawText(aCSSCache, aFont, aColor, aText);
            canvasCache.put(theKey, theEntry);
        }

        return theEntry.canvasElement;
    }

    public void clearCache() {
        canvasCache.clear();
    }
}