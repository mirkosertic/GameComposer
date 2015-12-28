package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TextExpressionTest {

    @Test
    public void testGet() throws Exception {
        TextExpression theExpression = new TextExpression("test");
        assertEquals("test", theExpression.get());
    }

    @Test
    public void testEquals() throws Exception {
        TextExpression theExpression = new TextExpression("test");
        TextExpression theExpression1 = new TextExpression("test");
        TextExpression theExpression2 = new TextExpression("lala");
        assertEquals(theExpression, theExpression);
        assertEquals(theExpression, theExpression1);
        assertNotEquals(theExpression, theExpression2);
        assertNotEquals(theExpression, "lala");
        assertNotEquals(theExpression, null);
    }

    @Test
    public void testHashCode() throws Exception {
        TextExpression theExpression = new TextExpression("test");
        assertEquals(3556498, theExpression.hashCode());
    }

    @Test
    public void testSerialize() throws Exception {
        TextExpression theExpression = new TextExpression("test");
        Map<String, Object> theData = theExpression.serialize();
        assertEquals(1, theData.size());
        assertEquals("test", theData.get("expression"));
    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put("expression", "test");
        TextExpression theExpression = TextExpression.deserialize(theData);
        assertEquals("test", theExpression.get());
    }
}