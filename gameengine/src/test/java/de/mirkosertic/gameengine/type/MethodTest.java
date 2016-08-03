package de.mirkosertic.gameengine.type;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MethodTest {

    private Method method;

    @Before
    public void setup() {
        method = new Method("lala", new Class[] {String.class, Integer.class}) {
            @Override
            public Object invoke(Object aTarget, Object[] aArguments) {
                return null;
            }
        };
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("lala", method.getName());
    }

    @Test
    public void testGetArgument() throws Exception {
        Class[] theArguments = method.getArgument();
        assertEquals(2, theArguments.length);
        assertEquals(String.class, theArguments[0]);
        assertEquals(Integer.class, theArguments[1]);
    }
}