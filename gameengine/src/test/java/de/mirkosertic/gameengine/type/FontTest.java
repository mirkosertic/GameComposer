package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class FontTest {

    @Test
    public void testEquals() {
        Font theFont1 = new Font(Font.FontName.ARIAL, 22);
        Font theFont2 = new Font(Font.FontName.ARIAL, 22);
        assertTrue(theFont1.equals(theFont1));
        assertTrue(theFont1.equals(theFont2));
        assertFalse(theFont1.equals(null));
        assertFalse(theFont1.equals(22));
        assertFalse(theFont1.equals(new Font(Font.FontName.ARIAL, 23)));
        assertFalse(theFont1.equals(new Font(Font.FontName.VERDANA, 22)));
    }

    @Test
    public void testHashCode() {
        Font theFont1 = new Font(Font.FontName.ARIAL, 22);
        Font theFont2 = new Font(Font.FontName.ARIAL, 22);
        assertEquals(theFont1.hashCode(), theFont2.hashCode());
        assertFalse(theFont1.hashCode() == new Font(Font.FontName.VERDANA, 22).hashCode());
    }

    @Test
    public void testInit() {
        Font theFont = new Font(Font.FontName.VERDANA, 22);
        assertEquals(Font.FontName.VERDANA, theFont.name);
        assertEquals(22, theFont.size);

        assertEquals(Font.FontName.ARIAL, Font.DEFAULT_FONT.name);
        assertEquals(10, Font.DEFAULT_FONT.size);
    }

    @Test
    public void testChangeSize() throws Exception {
        Font theFont = Font.DEFAULT_FONT;
        Font theMutated = theFont.changeSize(22);
        assertNotSame(theFont, theMutated);
        assertEquals(theFont.name, theMutated.name);
        assertEquals(22, theMutated.size);
    }

    @Test
    public void testChangeName() throws Exception {
        Font theFont = Font.DEFAULT_FONT;
        Font theMutated = theFont.changeName(Font.FontName.VERDANA);
        assertNotSame(theFont, theMutated);
        assertEquals(Font.FontName.VERDANA, theMutated.name);
        assertEquals(theFont.size, theMutated.size);
    }

    @Test
    public void testSerialize() throws Exception {
        Font theDefaultFont = Font.DEFAULT_FONT;
        Map<String, Object> theData = theDefaultFont.serialize();
        assertEquals(2, theData.size());
        assertEquals("ARIAL", theData.get("name"));
        assertEquals("10", theData.get("size"));
    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put("name", "VERDANA");
        theData.put("size", "18");
        Font theFont = Font.deserialize(theData);
        assertEquals(Font.FontName.VERDANA, theFont.name);
        assertEquals(18, theFont.size);
    }
}