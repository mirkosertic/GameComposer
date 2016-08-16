package de.mirkosertic.gameengine.generic;

import static org.junit.Assert.assertEquals;

import de.mirkosertic.gameengine.type.Color;
import org.junit.Test;

public class CSSUtilsTest {

    @Test
    public void testToColorHex() throws Exception {
        assertEquals("#100000", CSSUtils.toColorHex(new Color(16, 0, 0)));
        assertEquals("#110000", CSSUtils.toColorHex(new Color(17, 0, 0)));
        assertEquals("#ff0000", CSSUtils.toColorHex(new Color(255, 0, 0)));
    }

    @Test
    public void testFromColorHex() throws Exception {
        assertEquals(16, CSSUtils.fromColorHex("#100000").r);
        assertEquals(17, CSSUtils.fromColorHex("#001100").g);
        assertEquals(18, CSSUtils.fromColorHex("#000012").b);
    }
}