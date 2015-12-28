package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ForceTest {

    @Test
    public void testInit() {
        Force theForce = new Force(11.4f, 22.3f);
        assertEquals(11.4f, theForce.forceX, 0);
        assertEquals(22.3f, theForce.forceY, 0);
    }

    @Test
    public void testGetClassInformation() throws Exception {
        Force theForce = new Force(0,0);
        assertTrue(theForce.getClassInformation() instanceof ForceClassInformation);
    }
}