package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class PositionTest {

    @Test
    public void testInitDefault() {
        Position thePosition = new Position();
        assertEquals(0f, thePosition.x, 0);
        assertEquals(0f, thePosition.y, 0);
    }

    @Test
    public void testInitFloat() {
        Position thePosition = new Position(1.3f, 1.4f);
        assertEquals(1.3f, thePosition.x, 0);
        assertEquals(1.4f, thePosition.y, 0);
    }

    @Test
    public void testInitDouble() {
        Position thePosition = new Position(1.3d, 1.4d);
        assertEquals(1.3f, thePosition.x, 0);
        assertEquals(1.4f, thePosition.y, 0);
    }

    @Test
    public void testGetClassInformation() throws Exception {
        Position thePosition = new Position(10, 20);
        assertTrue(thePosition.getClassInformation() instanceof PositionClassInformation);
    }

    @Test
    public void testAdd() throws Exception {
        Position thePosition = new Position(10, 20);
        Position theMutated = thePosition.add(new Position(5, 5));
        assertNotSame(thePosition, theMutated);
        assertEquals(15f, theMutated.x, 0);
        assertEquals(25f, theMutated.y, 0);
    }

    @Test
    public void testChangeX() throws Exception {
        Position thePosition = new Position(10, 20);
        Position theMutated = thePosition.changeX(15f);
        assertNotSame(thePosition, theMutated);
        assertEquals(15f, theMutated.x, 0);
        assertEquals(20f, theMutated.y, 0);
    }

    @Test
    public void testChangeY() throws Exception {
        Position thePosition = new Position(10, 20);
        Position theMutated = thePosition.changeY(25f);
        assertNotSame(thePosition, theMutated);
        assertEquals(10f, theMutated.x, 0);
        assertEquals(25f, theMutated.y, 0);
    }

    @Test
    public void testSnapToGrid() throws Exception {
        Position thePosition = new Position(12, 12);
        Position theMutated = thePosition.snapToGrid(10, 10);
        assertNotSame(thePosition, theMutated);
        assertEquals(10f, theMutated.x, 0);
        assertEquals(10f, theMutated.y, 0);
    }

    @Test
    public void testSerialize() throws Exception {
        Position thePosition = new Position(10, 20);
        Map<String, Object> theData = thePosition.serialize();
        assertEquals(2, theData.size());
        assertEquals("10.0", theData.get("x"));
        assertEquals("20.0", theData.get("y"));
    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put("x", "10.0");
        theData.put("y", "20.0");
        Position thePosition = Position.deserialize(theData);
        assertEquals(10f, thePosition.x, 0);
        assertEquals(20f, thePosition.y, 0);
    }

    @Test
    public void testTranslate() throws Exception {
        Position thePosition = new Position(10, 10);
        Position theTranslated = thePosition.translate(new Angle(20), 5);
        assertNotSame(theTranslated, thePosition);
        assertEquals(11, (int) theTranslated.x);
        assertEquals(5, (int) theTranslated.y);
    }

    @Test
    public void testHashCode() {
        Position thePosition = new Position(6, 7);
        assertEquals(0, thePosition.hashCode());
    }

    @Test
    public void testEquals() {
        Position thePosition = new Position(6, 7);
        Position thePosition2 = new Position(6, 7);
        Position thePosition3 = new Position(6, 9);
        Position thePosition4 = new Position(9, 7);
        assertEquals(thePosition, thePosition);
        assertEquals(thePosition, thePosition2);
        assertNotEquals(thePosition, thePosition3);
        assertNotEquals(thePosition, thePosition4);
        assertNotEquals(thePosition, "lala");
        assertNotEquals(thePosition, null);
    }
}