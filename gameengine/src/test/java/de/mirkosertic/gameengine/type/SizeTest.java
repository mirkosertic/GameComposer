package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class SizeTest {

    @Test
    public void testInitDefault() {
        Size theSize = new Size();
        assertEquals(0, theSize.width);
        assertEquals(0, theSize.height);
    }

    @Test
    public void testInitValues() {
        Size theSize = new Size(10, 20);
        assertEquals(10, theSize.width);
        assertEquals(20, theSize.height);
    }

    @Test
    public void testGetClassInformation() throws Exception {
        Size theSize = new Size(10, 20);
        assertTrue(theSize.getClassInformation() instanceof SizeClassInformation);
    }

    @Test
    public void testChangeWidth() throws Exception {
        Size theSize = new Size(10, 20);
        Size theMutated = theSize.changeWidth(30);
        assertNotSame(theSize, theMutated);
        assertEquals(30, theMutated.width);
        assertEquals(20, theMutated.height);
    }

    @Test
    public void testChangeHeight() throws Exception {
        Size theSize = new Size(10, 20);
        Size theMutated = theSize.changeHeight(30);
        assertNotSame(theSize, theMutated);
        assertEquals(10, theMutated.width);
        assertEquals(30, theMutated.height);
    }

    @Test
    public void testEquals() throws Exception {
        Size theSize = new Size(12, 13);
        Size theSize1 = new Size(12, 13);
        Size theSize3 = new Size(24, 255);
        Size theSize4 = new Size(25, 255);
        assertEquals(theSize, theSize);
        assertEquals(theSize, theSize1);
        assertNotEquals(theSize, theSize3);
        assertNotEquals(theSize, null);
        assertNotEquals(theSize, "lala");
        assertNotEquals(theSize3, theSize4);
    }

    @Test
    public void testHashCode() throws Exception {
        Size theSize = new Size(25, 17);
        assertEquals(792, theSize.hashCode());
    }

    @Test
    public void testSerialize() throws Exception {
        Size theSize = new Size(15, 28);
        Map<String, Object> theData = theSize.serialize();
        assertEquals(2, theData.size());
        assertEquals("15", theData.get("width"));
        assertEquals("28", theData.get("height"));
    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put("width", "28");
        theData.put("height", "15");
        Size theSize = Size.deserialize(theData);
        assertEquals(28, theSize.width);
        assertEquals(15, theSize.height);
    }
}