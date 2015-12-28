package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollisionPositionTest {

    @Test
    public void testDetectEverywhere() throws Exception {
        CollisionPosition theColPos = CollisionPosition.EVERYWHERE;
        Position thePosition1 = new Position(10, 10);
        Position thePosition2 = new Position(20, 20);
        assertTrue(theColPos.detect(thePosition1, thePosition2));
    }

    @Test
    public void testDetectTop() throws Exception {
        CollisionPosition theColPos = CollisionPosition.TOP;
        Position thePosition1 = new Position(10, 10);
        Position thePosition2 = new Position(20, 20);
        Position thePosition3 = new Position(20, 0);
        assertFalse(theColPos.detect(thePosition1, thePosition1));
        assertTrue(theColPos.detect(thePosition2, thePosition1));
        assertFalse(theColPos.detect(thePosition3, thePosition2));
    }

    @Test
    public void testDetectBottom() throws Exception {
        CollisionPosition theColPos = CollisionPosition.BOTTOM;
        Position thePosition1 = new Position(10, 10);
        Position thePosition2 = new Position(20, 20);
        Position thePosition3 = new Position(20, 0);
        assertFalse(theColPos.detect(thePosition1, thePosition1));
        assertTrue(theColPos.detect(thePosition1, thePosition2));
        assertFalse(theColPos.detect(thePosition2, thePosition3));
    }

    @Test
    public void testDetectLeft() throws Exception {
        CollisionPosition theColPos = CollisionPosition.LEFT;
        Position thePosition1 = new Position(10, 10);
        Position thePosition2 = new Position(20, 10);
        Position thePosition3 = new Position(30, 10);
        assertFalse(theColPos.detect(thePosition1, thePosition1));
        assertTrue(theColPos.detect(thePosition2, thePosition1));
        assertFalse(theColPos.detect(thePosition2, thePosition3));
    }

    @Test
    public void testDetectRight() throws Exception {
        CollisionPosition theColPos = CollisionPosition.RIGHT;
        Position thePosition1 = new Position(10, 10);
        Position thePosition2 = new Position(20, 10);
        Position thePosition3 = new Position(30, 10);
        assertFalse(theColPos.detect(thePosition1, thePosition1));
        assertTrue(theColPos.detect(thePosition2, thePosition3));
        assertFalse(theColPos.detect(thePosition2, thePosition1));
    }
}