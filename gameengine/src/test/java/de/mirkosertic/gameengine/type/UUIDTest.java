package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class UUIDTest {

    @Test
    public void testRandomUID() throws Exception {
        String theUUID = UUID.randomUID();
        assertNotNull(theUUID);
        assertTrue(theUUID.length() > 0);
        Long.valueOf(theUUID);
    }
}