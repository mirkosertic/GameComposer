package de.mirkosertic.gameengine;

import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayUtilsTest {

    @Test
    public void testAsList() throws Exception {
        String[] theData = new String[] {"hallo"};
        List<String> theList = ArrayUtils.asList(theData);
        assertEquals(1, theList.size());
        assertEquals("hallo", theList.get(0));
    }
}