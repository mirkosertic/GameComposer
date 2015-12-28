package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.PropertyChanged;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EventSheetTest {

    @Test
    public void testInit() {
        GameScene theScene = mock(GameScene.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        GameEventManager theManager = mock(GameEventManager.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);
        when(theRuntime.getEventManager()).thenReturn(theManager);

        EventSheet theSheet = new EventSheet(theScene);
        assertEquals(theSheet.getGameScene(), theScene);
        assertEquals(0, theSheet.getRules().length);
    }

    @Test
    public void testNameProperty() throws Exception {
        GameScene theScene = mock(GameScene.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        GameEventManager theManager = mock(GameEventManager.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);
        when(theRuntime.getEventManager()).thenReturn(theManager);

        EventSheet theSheet = new EventSheet(theScene);
        assertEquals(EventSheet.NAME_PROPERTY, theSheet.nameProperty().getName());
        assertSame(theSheet, theSheet.nameProperty().getOwner());
        assertEquals(String.class, theSheet.nameProperty().getType());
        assertEquals(1, theSheet.nameProperty().getChangeListener().size());
        assertTrue(theSheet.nameProperty().getChangeListener().contains(theManager));
    }

    @Test
    public void testSerialize() throws Exception {
        GameScene theScene = mock(GameScene.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        GameEventManager theManager = mock(GameEventManager.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);
        when(theRuntime.getEventManager()).thenReturn(theManager);

        EventSheet theSheet = new EventSheet(theScene);
        theSheet.nameProperty().set("name");
        verify(theManager).handleGameEvent(any(PropertyChanged.class));
        GameRule theRule = new GameRule();
        theSheet.addRule(theRule);
        assertEquals(1, theSheet.getRules().length);
        assertSame(theRule, theSheet.getRules()[0]);

        Map<String, Object> theData = theSheet.serialize();
        assertEquals(2, theData.size());
        assertEquals("name", theData.get(EventSheet.NAME_PROPERTY));

        List<Map<String, Object>> theRules = (List<Map<String, Object>>) theData.get("rules");
        assertEquals(1, theRules.size());
        Map<String, Object> theFirstRule = theRules.get(0);
        assertEquals(1, theFirstRule.size());
    }

    @Test
    public void testUnmarshall() throws Exception {
        GameScene theScene = mock(GameScene.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        GameEventManager theManager = mock(GameEventManager.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);
        when(theRuntime.getEventManager()).thenReturn(theManager);

        EventSheet theSheet = new EventSheet(theScene);
        theSheet.nameProperty().set("name");
        GameRule theRule = new GameRule();
        theSheet.addRule(theRule);
        assertEquals(1, theSheet.getRules().length);
        assertSame(theRule, theSheet.getRules()[0]);

        Map<String, Object> theData = theSheet.serialize();
        assertEquals(2, theData.size());
        assertEquals("name", theData.get(EventSheet.NAME_PROPERTY));

        IORegistry theRegistry = new IORegistry();

        GameScene theScene2 = mock(GameScene.class);
        GameRuntime theRuntime2 = mock(GameRuntime.class);
        GameEventManager theManager2 = mock(GameEventManager.class);
        when(theScene2.getRuntime()).thenReturn(theRuntime2);
        when(theRuntime2.getEventManager()).thenReturn(theManager2);

        EventSheet theDeserialized = EventSheet.unmarshall(theRegistry, theScene2, theData);
        assertEquals("name", theDeserialized.nameProperty().get());
        assertEquals(1, theDeserialized.getRules().length);
        assertTrue(theDeserialized.getRules()[0] instanceof GameRule);
    }

    @Test
    public void testUnmarshall2() throws Exception {

        Map<String, Object> theData = new HashMap<>();

        IORegistry theRegistry = new IORegistry();

        GameScene theScene2 = mock(GameScene.class);
        GameRuntime theRuntime2 = mock(GameRuntime.class);
        GameEventManager theManager2 = mock(GameEventManager.class);
        when(theScene2.getRuntime()).thenReturn(theRuntime2);
        when(theRuntime2.getEventManager()).thenReturn(theManager2);

        EventSheet theDeserialized = EventSheet.unmarshall(theRegistry, theScene2, theData);
        assertNull(theDeserialized.nameProperty().get());
        assertEquals(0, theDeserialized.getRules().length);
    }


    @Test
    public void testRemoveRule() throws Exception {
        GameScene theScene = mock(GameScene.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        GameEventManager theManager = mock(GameEventManager.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);
        when(theRuntime.getEventManager()).thenReturn(theManager);

        EventSheet theSheet = new EventSheet(theScene);
        GameRule theRule = new GameRule();
        theSheet.addRule(theRule);
        assertEquals(1, theSheet.getRules().length);
        assertSame(theRule, theSheet.getRules()[0]);

        theSheet.removeRule(theRule);
        assertEquals(0, theSheet.getRules().length);
    }
}