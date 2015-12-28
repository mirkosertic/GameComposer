package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class RectangleTest {

    @Test
    public void testContains() throws Exception {
        Rectangle theRectangle = new Rectangle(new Position(10, 10), new Size(20,30));
        assertTrue(theRectangle.contains(new Position(11, 11)));

        assertTrue(theRectangle.contains(new Position(10, 10)));
        assertTrue(theRectangle.contains(new Position(30, 10)));
        assertTrue(theRectangle.contains(new Position(30, 40)));
        assertTrue(theRectangle.contains(new Position(10, 40)));
        assertFalse(theRectangle.contains(new Position(0, 0)));
        assertFalse(theRectangle.contains(new Position(Integer.MAX_VALUE, Integer.MAX_VALUE)));
        assertFalse(theRectangle.contains(new Position(Integer.MIN_VALUE, Integer.MIN_VALUE)));
        assertFalse(theRectangle.contains(new Position(Integer.MAX_VALUE, Integer.MIN_VALUE)));
        assertFalse(theRectangle.contains(new Position(Integer.MIN_VALUE, Integer.MAX_VALUE)));
        assertFalse(theRectangle.contains(new Position(Integer.MAX_VALUE, Integer.MAX_VALUE)));
        assertFalse(theRectangle.contains(new Position(Integer.MIN_VALUE, Integer.MIN_VALUE)));
        assertFalse(theRectangle.contains(new Position(9, 10)));
        assertFalse(theRectangle.contains(new Position(31, 10)));
        assertFalse(theRectangle.contains(new Position(31, 40)));
        assertFalse(theRectangle.contains(new Position(9, 40)));
        assertFalse(theRectangle.contains(new Position(9, 9)));
        assertFalse(theRectangle.contains(new Position(31, 9)));
        assertFalse(theRectangle.contains(new Position(31, 41)));
        assertFalse(theRectangle.contains(new Position(9, 41)));
    }

    @Test
    public void testNewPosition() throws Exception {
        Rectangle theOriginal = new Rectangle(new Position(-10000, 10000), new Size(20,30));
        Rectangle theRectangle = theOriginal.newPosition(new Position(10, 10));

        assertNotSame(theOriginal, theRectangle);
        assertTrue(theRectangle.contains(new Position(11, 11)));

        assertTrue(theRectangle.contains(new Position(10, 10)));
        assertTrue(theRectangle.contains(new Position(30, 10)));
        assertTrue(theRectangle.contains(new Position(30, 40)));
        assertTrue(theRectangle.contains(new Position(10, 40)));
        assertFalse(theRectangle.contains(new Position(9, 10)));
        assertFalse(theRectangle.contains(new Position(31, 10)));
        assertFalse(theRectangle.contains(new Position(31, 40)));
        assertFalse(theRectangle.contains(new Position(9, 40)));
        assertFalse(theRectangle.contains(new Position(9, 9)));
        assertFalse(theRectangle.contains(new Position(31, 9)));
        assertFalse(theRectangle.contains(new Position(31, 41)));
        assertFalse(theRectangle.contains(new Position(9, 41)));
    }

    @Test
    public void testNewSize() throws Exception {
        Rectangle theOriginal = new Rectangle(new Position(10, 10), new Size(5, 7));
        Rectangle theRectangle = theOriginal.newSize(new Size(20, 30));

        assertNotSame(theOriginal, theRectangle);
        assertTrue(theRectangle.contains(new Position(11, 11)));

        assertTrue(theRectangle.contains(new Position(10, 10)));
        assertTrue(theRectangle.contains(new Position(30, 10)));
        assertTrue(theRectangle.contains(new Position(30, 40)));
        assertTrue(theRectangle.contains(new Position(10, 40)));
        assertFalse(theRectangle.contains(new Position(9, 10)));
        assertFalse(theRectangle.contains(new Position(31, 10)));
        assertFalse(theRectangle.contains(new Position(31, 40)));
        assertFalse(theRectangle.contains(new Position(9, 40)));
        assertFalse(theRectangle.contains(new Position(9, 9)));
        assertFalse(theRectangle.contains(new Position(31, 9)));
        assertFalse(theRectangle.contains(new Position(31, 41)));
        assertFalse(theRectangle.contains(new Position(9, 41)));
    }

    @Test
    public void testSerialize() throws Exception {
        Rectangle theRectangle = new Rectangle();
        Map<String, Object> theData = theRectangle.serialize();
        assertEquals(2, theData.size());

        Map<String, Object> thePosition = (Map<String, Object>) theData.get("position");
        assertEquals(2, thePosition.size());
        Position theDePosition = Position.deserialize(thePosition);
        assertEquals(0, theDePosition.x, 0);
        assertEquals(0, theDePosition.y, 0);

        Map<String, Object> theSize = (Map<String, Object>) theData.get("size");
        assertEquals(2, theSize.size());
        Size theDeSize = Size.deserialize(theSize);
        assertEquals(Integer.MAX_VALUE, theDeSize.width);
        assertEquals(Integer.MAX_VALUE, theDeSize.height);
    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put("position", new Position(10, 10).serialize());
        theData.put("size", new Size(20, 30).serialize());
        Rectangle theRectangle = Rectangle.deserialize(theData);

        assertTrue(theRectangle.contains(new Position(11, 11)));

        assertTrue(theRectangle.contains(new Position(10, 10)));
        assertTrue(theRectangle.contains(new Position(30, 10)));
        assertTrue(theRectangle.contains(new Position(30, 40)));
        assertTrue(theRectangle.contains(new Position(10, 40)));
        assertFalse(theRectangle.contains(new Position(9, 10)));
        assertFalse(theRectangle.contains(new Position(31, 10)));
        assertFalse(theRectangle.contains(new Position(31, 40)));
        assertFalse(theRectangle.contains(new Position(9, 40)));
        assertFalse(theRectangle.contains(new Position(9, 9)));
        assertFalse(theRectangle.contains(new Position(31, 9)));
        assertFalse(theRectangle.contains(new Position(31, 41)));
        assertFalse(theRectangle.contains(new Position(9, 41)));
    }
}