package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameObjectInstanceRemovedFromSceneConditionTest {

    @Test
    public void testGameObjectProperty() throws Exception {
        GameObjectInstanceRemovedFromSceneCondition theCondition = new GameObjectInstanceRemovedFromSceneCondition();
        assertTrue(theCondition.gameObjectProperty().isNull());
        assertSame(theCondition, theCondition.gameObjectProperty().getOwner());
        assertEquals(GameObjectInstanceRemovedFromSceneCondition.GAME_OBJECT_PROPERTY, theCondition.gameObjectProperty().getName());
        assertEquals(GameObject.class, theCondition.gameObjectProperty().getType());
    }

    @Test
    public void testAppliesTo() throws Exception {
        GameObjectInstanceRemovedFromSceneCondition theCondition = new GameObjectInstanceRemovedFromSceneCondition();
        assertSame(ConditionResult.NOT_FULFILLED, theCondition.appliesTo(null, null));

        GameScene theScene = mock(GameScene.class);
        GameObjectInstance theInstance2 = mock(GameObjectInstance.class);
        when(theInstance2.getOwnerGameObject()).thenReturn(mock(GameObject.class));
        GameObjectInstanceRemovedFromScene theEvent = new GameObjectInstanceRemovedFromScene(theScene, theInstance2);
        assertSame(ConditionResult.NOT_FULFILLED, theCondition.appliesTo(theScene, theEvent));

        GameObject theObject = mock(GameObject.class);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        when(theInstance.getOwnerGameObject()).thenReturn(theObject);

        GameObjectInstanceRemovedFromScene theEvent2 = new GameObjectInstanceRemovedFromScene(theScene, theInstance);
        theCondition.gameObjectProperty().set(theObject);
        ConditionResult theResult = theCondition.appliesTo(theScene, theEvent2);
        assertNotSame(ConditionResult.NOT_FULFILLED, theResult);
        assertTrue(theResult.isConditionTrue());
        assertEquals(theEvent2, theResult.getEvent());
        assertEquals(1, theResult.getAffectedInstances().length);
        assertSame(theInstance, theResult.getAffectedInstances()[0]);
    }

    @Test
    public void testSerialize() throws Exception {
        GameObjectInstanceRemovedFromSceneCondition theCondition = new GameObjectInstanceRemovedFromSceneCondition();
        Map<String, Object> theData = theCondition.serialize();
        assertEquals(1, theData.size());
        assertEquals(GameObjectInstanceRemovedFromSceneCondition.TYPE_VALUE, theData.get(GameObjectInstanceRemovedFromSceneCondition.TYPE_ATTRIBUTE));

        GameObject theObject = mock(GameObject.class);
        Property theUUIDProperty = mock(Property.class);
        when(theUUIDProperty.get()).thenReturn("UUID");
        when(theObject.uuidProperty()).thenReturn(theUUIDProperty);
        theCondition.gameObjectProperty().set(theObject);

        Map<String, Object> theData2 = theCondition.serialize();
        assertEquals(2, theData2.size());
        assertEquals(GameObjectInstanceRemovedFromSceneCondition.TYPE_VALUE, theData2.get(GameObjectInstanceRemovedFromSceneCondition.TYPE_ATTRIBUTE));
        assertEquals("UUID", theData2.get("objectUUID"));
    }

    @Test
    public void testUnmarshall() throws Exception {

        GameScene theScene = mock(GameScene.class);
        GameObject theObject = mock(GameObject.class);
        when(theScene.findObjectByID(eq("UUID"))).thenReturn(theObject);

        Map<String, Object> theData = new HashMap<>();
        theData.put(GameObjectInstanceRemovedFromSceneCondition.TYPE_ATTRIBUTE, GameObjectInstanceRemovedFromSceneCondition.TYPE_VALUE);
        GameObjectInstanceRemovedFromSceneCondition theCondition = GameObjectInstanceRemovedFromSceneCondition.unmarshall(theScene, theData);
        assertTrue(theCondition.gameObjectProperty().isNull());

        theData.put("objectUUID", "UUID");
        GameObjectInstanceRemovedFromSceneCondition theCondition2 = GameObjectInstanceRemovedFromSceneCondition.unmarshall(theScene, theData);
        assertSame(theObject, theCondition2.gameObjectProperty().get());

        verify(theScene, times(1)).findObjectByID(eq("UUID"));
    }
}