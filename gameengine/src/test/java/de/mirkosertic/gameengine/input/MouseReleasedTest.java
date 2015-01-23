package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.type.Position;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class MouseReleasedTest {

    @Test
    public void testCreate() {
        GameObjectInstance[] theInstances = new GameObjectInstance[0];
        Position thePosition = new Position(10, 20);
        MouseReleased theEvent = new MouseReleased(thePosition, theInstances);

        assertSame(thePosition, theEvent.position);
        assertSame(theInstances, theEvent.affectedInstances);
        assertEquals("MouseReleased", theEvent.type);
    }
}