package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameObjectInstanceAddedToSceneConditionTest {

    @Test
    public void testGameObjectProperty() throws Exception {
        GameObjectInstanceAddedToSceneCondition theCondition = new GameObjectInstanceAddedToSceneCondition();
        assertEquals(GameObjectInstanceAddedToSceneCondition.GAME_OBJECT_PROPERTY, theCondition.gameObjectProperty().getName());
        assertNull(GameObjectInstanceAddedToSceneCondition.GAME_OBJECT_PROPERTY, theCondition.gameObjectProperty().get());
        assertEquals(GameObject.class, theCondition.gameObjectProperty().getType());
        assertSame(theCondition, theCondition.gameObjectProperty().getOwner());
    }

    @Test
    public void testAppliesTo() throws Exception {
        GameObjectInstanceAddedToSceneCondition theCondition = new GameObjectInstanceAddedToSceneCondition();
        GameScene theScene = mock(GameScene.class);
        GameObject theObject = mock(GameObject.class);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        when(theInstance.getOwnerGameObject()).thenReturn(theObject);
        GameObjectInstanceAddedToScene theEvent = new GameObjectInstanceAddedToScene(theInstance);
        assertSame(ConditionResult.NOT_FULFILLED, theCondition.appliesTo(theScene, new SceneShutdown()));
        assertSame(ConditionResult.NOT_FULFILLED, theCondition.appliesTo(theScene, theEvent));

        theCondition.gameObjectProperty().set(theObject);
        ConditionResult theResult = theCondition.appliesTo(theScene, theEvent);
        assertTrue(theResult.isConditionTrue());
        assertSame(theEvent, theResult.getEvent());
        assertEquals(1, theResult.getAffectedInstances().length);
        assertSame(theInstance, theResult.getAffectedInstances()[0]);
    }

    @Test
    public void testSerialize() throws Exception {
        GameObjectInstanceAddedToSceneCondition theCondition = new GameObjectInstanceAddedToSceneCondition();
        GameObject theObject = mock(GameObject.class);
        Property theUUIDProperty = mock(Property.class);
        when(theUUIDProperty.get()).thenReturn("UUID");
        when(theObject.uuidProperty()).thenReturn(theUUIDProperty);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        when(theInstance.getOwnerGameObject()).thenReturn(theObject);

        Map<String, Object> theData = theCondition.serialize();
        assertEquals(1, theData.size());
        assertEquals(GameObjectInstanceAddedToSceneCondition.TYPE_VALUE, theData.get(GameObjectInstanceAddedToSceneCondition.TYPE_ATTRIBUTE));

        theCondition.gameObjectProperty().set(theObject);
        theData = theCondition.serialize();
        assertEquals(2, theData.size());
        assertEquals(GameObjectInstanceAddedToSceneCondition.TYPE_VALUE, theData.get(GameObjectInstanceAddedToSceneCondition.TYPE_ATTRIBUTE));
        assertEquals("UUID", theData.get("objectUUID"));
   }

    @Test
    public void testUnmarshall() throws Exception {

        Map<String, Object> theData = new HashMap<>();
        theData.put(GameObjectInstanceAddedToSceneCondition.TYPE_ATTRIBUTE, GameObjectInstanceAddedToSceneCondition.TYPE_VALUE);

        GameObjectInstanceAddedToSceneCondition theCondition1 = GameObjectInstanceAddedToSceneCondition.unmarshall(null, theData);
        assertTrue(theCondition1.gameObjectProperty().isNull());

        theData.put("objectUUID", "UUID");

        GameScene theScene = mock(GameScene.class);
        GameObject theObject = mock(GameObject.class);
        when(theScene.findObjectByID(eq("UUID"))).thenReturn(theObject);

        GameObjectInstanceAddedToSceneCondition theCondition2 = GameObjectInstanceAddedToSceneCondition.unmarshall(theScene, theData);
        assertSame(theObject, theCondition2.gameObjectProperty().get());
    }
}