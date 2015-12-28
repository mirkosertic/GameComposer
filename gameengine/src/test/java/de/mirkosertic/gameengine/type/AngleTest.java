package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

public class AngleTest {

    @Test
    public void testGet() throws Exception {
        assertEquals(350, new Angle(350).get().intValue());
        assertEquals(1, new Angle(361).get().intValue());
    }

    @Test
    public void testAdd() throws Exception {
        assertEquals(350, new Angle(340).add(10).get().intValue());
        assertEquals(330, new Angle(340).add(-10).get().intValue());
    }

    @Test
    public void testInvert() throws Exception {
        assertEquals(350, new Angle(10).invert().get().intValue());
    }

    @Test
    public void testToRadians() throws Exception {
        assertEquals(Math.PI, new Angle(180).toRadians(), 0.001);
    }

    @Test
    public void testResetToZero() {
        Angle theAngle = new Angle(19);
        Angle theZero = theAngle.resetToZero();
        assertNotSame(theAngle, theZero);
        assertEquals(0, theZero.get(), 0);
    }

    @Test
    public void testEquals() throws Exception {
        Angle theAngle1 = new Angle(10);
        Angle theAngle2 = new Angle(10);
        Angle theAngle3 = new Angle(11);
        assertEquals(theAngle1, theAngle1);
        assertEquals(theAngle1, theAngle2);
        assertNotEquals(theAngle1, "lala");
        assertNotEquals(theAngle1, null);
        assertNotEquals(theAngle1, theAngle3);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(10, new Angle(10).hashCode());
    }

    @Test
    public void testSerialize() throws Exception {
        Angle theAngle = new Angle(10);
        Map<String, Object> theData = theAngle.serialize();
        assertEquals(1, theData.size());
        assertEquals("10", theData.get("angleindegrees"));
    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put("angleindegrees","10");
        Angle theAngle = Angle.deserialize(theData);
        assertEquals(10, theAngle.get().intValue());
    }

    @Test
    public void testFromRadians() throws Exception {
        assertEquals(180, Angle.fromRadians((float) Math.PI).get().intValue());
    }

    @Test
    public void testSnap() {
        assertEquals(0, new Angle(0).snapTo(15).get(), 0);
        assertEquals(345, new Angle(-13).snapTo(15).get(), 0);
        assertEquals(15, new Angle(13).snapTo(15).get(), 0);
    }
}