package de.mirkosertic.gameengine.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.PropertyChanged;
import de.mirkosertic.gameengine.type.Size;

public class GameObjectTest {

    @Test
    public void testGetGameScene() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);

        GameObject theObject = new GameObject(theScene, "Test");
        assertSame(theScene, theObject.getGameScene());
    }

    @Test
    public void testUuidProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);

        GameObject theObject = new GameObject(theScene, "Test");
        assertNotNull(theObject.uuidProperty().get());
        assertEquals(GameObject.UUID_PROPERTY, theObject.uuidProperty().getName());
        assertEquals(theObject, theObject.uuidProperty().getOwner());
        assertEquals(String.class, theObject.uuidProperty().getType());
        theObject.uuidProperty().set("lala");
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testNameProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);

        GameObject theObject = new GameObject(theScene, "Test");
        assertEquals("Test", theObject.nameProperty().get());
        assertEquals(GameObject.NAME_PROPERTY, theObject.nameProperty().getName());
        assertEquals(theObject, theObject.nameProperty().getOwner());
        assertEquals(String.class, theObject.nameProperty().getType());
        theObject.nameProperty().set("lala");
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testSizeProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);

        GameObject theObject = new GameObject(theScene, "Test");
        assertEquals(new Size(64, 64), theObject.sizeProperty().get());
        assertEquals(GameObject.SIZE_PROPERTY, theObject.sizeProperty().getName());
        assertEquals(theObject, theObject.sizeProperty().getOwner());
        assertEquals(Size.class, theObject.sizeProperty().getType());
        theObject.sizeProperty().set(new Size(128, 128));
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testVisibleProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);

        GameObject theObject = new GameObject(theScene, "Test");
        assertTrue(theObject.visibleProperty().get());
        assertEquals(GameObject.VISIBLE_PROPERTY, theObject.visibleProperty().getName());
        assertEquals(theObject, theObject.visibleProperty().getOwner());
        assertEquals(Boolean.class, theObject.visibleProperty().getType());
        theObject.visibleProperty().set(false);
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testEquals() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);

        GameObject theObject = new GameObject(theScene, "Test");
        assertEquals(theObject, theObject);
        assertNotEquals(theObject, null);
        assertNotEquals(theObject, "lala");

        GameObject theObject2 = new GameObject(theScene, "Test");
        theObject2.uuidProperty().set(theObject.uuidProperty().get());
        assertEquals(theObject, theObject2);
    }

    @Test
    public void testHashCode() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);

        GameObject theObject = new GameObject(theScene, "Test");
        theObject.uuidProperty().set("lala");
        assertEquals(3314090, theObject.hashCode());
    }

    @Test
    public void testGetClassInformation() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);

        GameObject theObject = new GameObject(theScene, "Test");
        assertTrue(theObject.getClassInformation() instanceof GameObjectClassInformation);
    }

    @Test
    public void testGetBehaviorTemplates() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);

        BehaviorTemplate theBehavior = mock(BehaviorTemplate.class);

        GameObject theObject = new GameObject(theScene, "Test");
        Set<BehaviorTemplate> theBehaviors = theObject.getBehaviorTemplates();
        assertEquals(0, theBehaviors.size());
        theObject.add(theBehavior);
        theBehaviors = theObject.getBehaviorTemplates();
        assertEquals(1, theBehaviors.size());
        assertSame(theBehavior, theBehaviors.iterator().next());
    }

    @Test
    public void testSerialize() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);

        BehaviorTemplate theBehavior = mock(BehaviorTemplate.class);

        GameObject theObject = new GameObject(theScene, "Test");
        theObject.add(theBehavior);

        Map<String, Object> theData = theObject.serialize();
        assertEquals(5, theData.size());
        assertNotNull(theData.get(GameObject.UUID_PROPERTY));
        assertEquals("Test", theData.get(GameObject.NAME_PROPERTY));
        assertEquals("true", theData.get(GameObject.VISIBLE_PROPERTY));
        Map<String, Object> theSize = (Map<String, Object>) theData.get(GameObject.SIZE_PROPERTY);
        assertEquals(new Size(64, 64), Size.deserialize(theSize));

        List<Map<String, Object>> theTemplates = (List<Map<String, Object>>) theData.get("templates");
        assertEquals(1, theTemplates.size());
        assertTrue(theTemplates.get(0) instanceof Map);
        verify(theBehavior).serialize();
    }

    @Test
    public void testDeserialize() throws Exception {

        BehaviorTemplate theBehavior = mock(BehaviorTemplate.class);

        BehaviorTemplateUnmarshaller theUnmarshaller = mock(BehaviorTemplateUnmarshaller.class);
        when(theUnmarshaller.deserialize(any(GameEventManager.class), any(GameObject.class), anyMap())).thenReturn(theBehavior);

        IORegistry theRegistry = mock(IORegistry.class);
        when(theRegistry.getBehaviorTemplateUnmarshallerFor(eq(new BehaviorType("behav")))).thenReturn(theUnmarshaller);

        GameEventManager theEventManager = mock(GameEventManager.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getIORegistry()).thenReturn(theRegistry);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntime);

        Map<String, Object> theData = new HashMap<>();
        theData.put(GameObject.NAME_PROPERTY, "Test");
        theData.put(GameObject.UUID_PROPERTY, "UID");
        theData.put(GameObject.VISIBLE_PROPERTY, "false");
        theData.put(GameObject.SIZE_PROPERTY, new Size(17, 18).serialize());
        List<Map<String, Object>> theTemplates = new ArrayList<>();
        Map<String, Object> theSingleBehavior = new HashMap<>();
        theSingleBehavior.put(Behavior.TYPE_ATTRIBUTE, "behav");
        theTemplates.add(theSingleBehavior);
        theData.put("templates", theTemplates);

        GameObject theObject = GameObject.deserialize(theRuntime, theScene, theData);
        assertEquals("Test", theObject.nameProperty().get());
        assertEquals("UID", theObject.uuidProperty().get());
        assertEquals(new Size(17, 18), theObject.sizeProperty().get());
        assertFalse(theObject.visibleProperty().get());

        assertEquals(1, theObject.getBehaviorTemplates().size());
        assertTrue(theObject.getBehaviorTemplates().contains(theBehavior));
    }
}