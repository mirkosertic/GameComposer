package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class AbsolutePositionAnchorTest {

    @Test
    public void testComputeTopLeft() throws Exception {
        AbsolutePositionAnchor theAnchor = AbsolutePositionAnchor.TOP_LEFT;
        Size theScreenSize = new Size(320, 200);
        Position thePosition = new Position(10,10);
        Position thePositionOnScreen = theAnchor.compute(thePosition, theScreenSize);
        assertEquals(10f, thePositionOnScreen.x, 0);
        assertEquals(10f, thePositionOnScreen.y, 0);
    }

    @Test
    public void testComputeTopRight() throws Exception {
        AbsolutePositionAnchor theAnchor = AbsolutePositionAnchor.TOP_RIGHT;
        Size theScreenSize = new Size(320, 200);
        Position thePosition = new Position(10,10);
        Position thePositionOnScreen = theAnchor.compute(thePosition, theScreenSize);
        assertEquals(310f, thePositionOnScreen.x, 0);
        assertEquals(10f, thePositionOnScreen.y, 0);
    }

    @Test
    public void testComputeBottomLeft() throws Exception {
        AbsolutePositionAnchor theAnchor = AbsolutePositionAnchor.BOTTOM_LEFT;
        Size theScreenSize = new Size(320, 200);
        Position thePosition = new Position(10,10);
        Position thePositionOnScreen = theAnchor.compute(thePosition, theScreenSize);
        assertEquals(10f, thePositionOnScreen.x, 0);
        assertEquals(190f, thePositionOnScreen.y, 0);
    }

    @Test
    public void testComputeBottomRight() throws Exception {
        AbsolutePositionAnchor theAnchor = AbsolutePositionAnchor.BOTTOM_RIGHT;
        Size theScreenSize = new Size(320, 200);
        Position thePosition = new Position(10,10);
        Position thePositionOnScreen = theAnchor.compute(thePosition, theScreenSize);
        assertEquals(310f, thePositionOnScreen.x, 0);
        assertEquals(190f, thePositionOnScreen.y, 0);
    }
}