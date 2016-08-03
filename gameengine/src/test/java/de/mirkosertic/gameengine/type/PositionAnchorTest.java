package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PositionAnchorTest {

    @Test
    public void testComputeTopLeft() throws Exception {
        PositionAnchor theAnchor = PositionAnchor.TOP_LEFT;
        Size theScreenSize = new Size(320, 200);
        Position thePosition = new Position(10,10);
        Position thePositionOnScreen = theAnchor.compute(thePosition, new Position(15, 15), theScreenSize);
        assertEquals(10f, thePositionOnScreen.x, 0);
        assertEquals(10f, thePositionOnScreen.y, 0);
    }

    @Test
    public void testComputeCenter() throws Exception {
        PositionAnchor theAnchor = PositionAnchor.CENTER;
        Size theScreenSize = new Size(320, 200);
        Position thePosition = new Position(-10,-10);
        Position thePositionOnScreen = theAnchor.compute(thePosition, new Position(15, 15), theScreenSize);
        assertEquals(150f, thePositionOnScreen.x, 0);
        assertEquals(90f, thePositionOnScreen.y, 0);
    }

    @Test
    public void testComputePercent() throws Exception {
        PositionAnchor theAnchor = PositionAnchor.PERCENT;
        Size theScreenSize = new Size(320, 200);
        Position thePosition = new Position(10,10);
        Position thePositionOnScreen = theAnchor.compute(thePosition, new Position(15, 15), theScreenSize);
        assertEquals(32, thePositionOnScreen.x, 0);
        assertEquals(20, thePositionOnScreen.y, 0);
    }

    @Test
    public void testComputeScene() throws Exception {
        PositionAnchor theAnchor = PositionAnchor.SCENE;
        Size theScreenSize = new Size(320, 200);
        Position thePosition = new Position(10,10);
        Position thePositionOnScreen = theAnchor.compute(thePosition, new Position(15, 15), theScreenSize);
        assertEquals(-5, thePositionOnScreen.x, 0);
        assertEquals(-5, thePositionOnScreen.y, 0);
    }

    @Test
    public void testComputeTopRight() throws Exception {
        PositionAnchor theAnchor = PositionAnchor.TOP_RIGHT;
        Size theScreenSize = new Size(320, 200);
        Position thePosition = new Position(10,10);
        Position thePositionOnScreen = theAnchor.compute(thePosition, new Position(15, 15), theScreenSize);
        assertEquals(310f, thePositionOnScreen.x, 0);
        assertEquals(10f, thePositionOnScreen.y, 0);
    }

    @Test
    public void testComputeBottomLeft() throws Exception {
        PositionAnchor theAnchor = PositionAnchor.BOTTOM_LEFT;
        Size theScreenSize = new Size(320, 200);
        Position thePosition = new Position(10,10);
        Position thePositionOnScreen = theAnchor.compute(thePosition, new Position(15, 15), theScreenSize);
        assertEquals(10f, thePositionOnScreen.x, 0);
        assertEquals(190f, thePositionOnScreen.y, 0);
    }

    @Test
    public void testComputeBottomRight() throws Exception {
        PositionAnchor theAnchor = PositionAnchor.BOTTOM_RIGHT;
        Size theScreenSize = new Size(320, 200);
        Position thePosition = new Position(10,10);
        Position thePositionOnScreen = theAnchor.compute(thePosition, new Position(15, 15), theScreenSize);
        assertEquals(310f, thePositionOnScreen.x, 0);
        assertEquals(190f, thePositionOnScreen.y, 0);
    }
}