package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ConditionResultTest {

    @Test
    public void testNotFulfilled() throws Exception {
        ConditionResult theResult = ConditionResult.NOT_FULFILLED;
        assertFalse(theResult.isConditionTrue());
        assertNull(theResult.getEvent());
        assertEquals(0, theResult.getAffectedInstances().length);
    }


    @Test
    public void testIsConditionTrue() throws Exception {
        GameEvent theEvent = mock(GameEvent.class);
        GameObjectInstance[] theInstances = new GameObjectInstance[0];
        ConditionResult thePositive = new ConditionResult(true, theEvent, theInstances);
        assertTrue(thePositive.isConditionTrue());
        assertSame(theEvent, thePositive.getEvent());
        assertSame(theInstances, thePositive.getAffectedInstances());

        ConditionResult theNegative = new ConditionResult(false, theEvent, theInstances);
        assertFalse(theNegative.isConditionTrue());
        assertSame(theEvent, theNegative.getEvent());
        assertSame(theInstances, theNegative.getAffectedInstances());
    }
}