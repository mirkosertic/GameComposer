package de.mirkosertic.gameengine.event;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class PropertyTest {

    @Test
    public void testSetWithChange() throws Exception {
        GameEventListener theListener = mock(GameEventListener.class);
        Property<String> theProperty = new Property<String>(String.class, this, "property", "value", theListener);
        assertEquals("value", theProperty.get());
        theProperty.set("value1");
        assertEquals("value1", theProperty.get());
        verify(theListener).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testSetWithoutChange() throws Exception {
        GameEventListener theListener = mock(GameEventListener.class);
        Property<String> theProperty = new Property<String>(String.class, this, "property", "value", theListener);
        assertEquals("value", theProperty.get());
        theProperty.set("value");
        assertEquals("value", theProperty.get());
        verifyZeroInteractions(theListener);
    }

    @Test
    public void testSetQuietly() throws Exception {
        GameEventListener theListener = mock(GameEventListener.class);
        Property<String> theProperty = new Property<String>(String.class, this, "property", "value", theListener);
        assertEquals("value", theProperty.get());
        theProperty.setQuietly("value1");
        assertEquals("value1", theProperty.get());
        verifyZeroInteractions(theListener);
    }

    @Test
    public void testGetLastChanged() throws Exception {
        Property<String> theProperty = new Property<String>(String.class, this, "property", "value");
        long theLastChange = theProperty.getLastChanged();
        assertTrue(theLastChange > 0);
        Thread.sleep(20);
        theProperty.set("value1");
        assertTrue(theProperty.getLastChanged() > theLastChange);
    }

    @Test
    public void testGetChangeListener() throws Exception {
        GameEventListener theListener = mock(GameEventListener.class);

        Property<String> theProperty = new Property<String>(String.class, this, "property", "value");
        assertTrue(theProperty.getChangeListener().isEmpty());

        theProperty = new Property<String>(String.class, this, "property", "value", theListener);
        assertTrue(theProperty.getChangeListener().size() == 1);
        assertTrue(theProperty.getChangeListener().contains(theListener));
    }

    @Test
    public void testAddChangeListener() throws Exception {
        Property<String> theProperty = new Property<String>(String.class, this, "property", "value");

        assertTrue(theProperty.getChangeListener().isEmpty());

        GameEventListener theListener = mock(GameEventListener.class);
        theProperty.addChangeListener(theListener);

        assertTrue(theProperty.getChangeListener().size() == 1);
        assertTrue(theProperty.getChangeListener().contains(theListener));
    }

    @Test
    public void testRemoveChangeListener() throws Exception {
        GameEventListener theListener = mock(GameEventListener.class);

        Property<String> theProperty = new Property<String>(String.class, this, "property", "value", theListener);
        assertTrue(theProperty.getChangeListener().size() == 1);
        assertTrue(theProperty.getChangeListener().contains(theListener));
        theProperty.removeChangeListener(theListener);

        assertTrue(theProperty.getChangeListener().isEmpty());
    }
}