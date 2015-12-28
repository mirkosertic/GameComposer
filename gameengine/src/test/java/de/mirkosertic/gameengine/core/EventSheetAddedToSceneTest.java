package de.mirkosertic.gameengine.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class EventSheetAddedToSceneTest {

    @Test
    public void testInit() {
        EventSheet theEventSheet = mock(EventSheet.class);
        EventSheetAddedToScene theEvent = new EventSheetAddedToScene(theEventSheet);
        assertSame(theEventSheet, theEvent.eventSheet);
        assertEquals("EventSheetAddedToScene", theEvent.type);
    }

}