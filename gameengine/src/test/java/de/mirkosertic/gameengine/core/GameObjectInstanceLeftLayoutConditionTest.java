package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Rectangle;
import de.mirkosertic.gameengine.type.Size;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameObjectInstanceLeftLayoutConditionTest {

    @Test
    public void testGameObjectProperty() throws Exception {
        GameObjectInstanceLeftLayoutCondition theCondition = new GameObjectInstanceLeftLayoutCondition();
        assertTrue(theCondition.gameObjectProperty().isNull());
        assertEquals(theCondition, theCondition.gameObjectProperty().getOwner());
        assertEquals(GameObjectInstanceLeftLayoutCondition.GAME_OBJECT_PROPERTY, theCondition.gameObjectProperty().getName());
        assertEquals(GameObject.class, theCondition.gameObjectProperty().getType());
    }

    @Test
    public void testAppliesTo() throws Exception {
        GameScene theScene = mock(GameScene.class);
        Property theLayoutProperty = mock(Property.class);
        when(theScene.layoutBoundsProperty()).thenReturn(theLayoutProperty);

        GameObjectInstanceLeftLayoutCondition theCondition = new GameObjectInstanceLeftLayoutCondition();
        assertSame(ConditionResult.NOT_FULFILLED, theCondition.appliesTo(theScene, null));

        Property theProperty = mock(Property.class);
        when(theProperty.get()).thenReturn(new Position(10, 10));
        PropertyChanged theChanged = new PropertyChanged(theProperty, null);
        assertSame(ConditionResult.NOT_FULFILLED, theCondition.appliesTo(theScene, theChanged));

        GameObject theObject = mock(GameObject.class);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        when(theInstance.getOwnerGameObject()).thenReturn(theObject);
        when(theProperty.getName()).thenReturn(GameObjectInstance.POSITION_PROPERTY);
        when(theProperty.getOwner()).thenReturn(theInstance);
        theCondition.gameObjectProperty().set(theObject);
        when(theLayoutProperty.get()).thenReturn(new Rectangle(new Position(0, 0), new Size(50,50)));

        assertSame(ConditionResult.NOT_FULFILLED, theCondition.appliesTo(theScene, theChanged));
        verify(theProperty).getOwner();
        verify(theInstance).getOwnerGameObject();

        when(theLayoutProperty.get()).thenReturn(new Rectangle(new Position(100, 100), new Size(50,50)));
        ConditionResult theResult = theCondition.appliesTo(theScene, theChanged);
        assertNotSame(ConditionResult.NOT_FULFILLED, theResult);
        assertTrue(theResult.isConditionTrue());
        assertSame(theChanged, theResult.getEvent());
        assertEquals(1, theResult.getAffectedInstances().length);
        assertSame(theInstance,theResult.getAffectedInstances()[0]);
    }

    @Test
    public void testSerialize() throws Exception {
        GameObjectInstanceLeftLayoutCondition theContition = new GameObjectInstanceLeftLayoutCondition();

        Map<String, Object> theData = theContition.serialize();
        assertEquals(1, theData.size());
        assertEquals(GameObjectInstanceLeftLayoutCondition.TYPE_VALUE, theData.get(GameObjectInstanceLeftLayoutCondition.TYPE_ATTRIBUTE));

        GameObject theObject = mock(GameObject.class);
        Property theUUIDProperty = mock(Property.class);
        when(theUUIDProperty.get()).thenReturn("UUID");
        when(theObject.uuidProperty()).thenReturn(theUUIDProperty);
        theContition.gameObjectProperty().set(theObject);

        Map<String, Object> theData1 = theContition.serialize();
        assertEquals(2, theData1.size());
        assertEquals(GameObjectInstanceLeftLayoutCondition.TYPE_VALUE, theData1.get(GameObjectInstanceLeftLayoutCondition.TYPE_ATTRIBUTE));
        assertEquals("UUID", theData1.get("gameObjectUUID"));
    }

    @Test
    public void testUnmarshall() throws Exception {
        GameScene theScene = mock(GameScene.class);
        GameObject theObject = mock(GameObject.class);
        when(theScene.findObjectByID(eq("UUID"))).thenReturn(theObject);

        Map<String, Object> theData = new HashMap<>();
        theData.put(GameObjectInstanceLeftLayoutCondition.TYPE_ATTRIBUTE, GameObjectInstanceLeftLayoutCondition.TYPE_VALUE);
        GameObjectInstanceLeftLayoutCondition theCondition = GameObjectInstanceLeftLayoutCondition.unmarshall(theScene, theData);
        assertNotNull(theCondition);
        assertTrue(theCondition.gameObjectProperty().isNull());

        theData.put("gameObjectUUID", "UUID");
        GameObjectInstanceLeftLayoutCondition theCondition1 = GameObjectInstanceLeftLayoutCondition.unmarshall(theScene, theData);
        assertSame(theObject, theCondition1.gameObjectProperty().get());
        verify(theScene, times(1)).findObjectByID(eq("UUID"));
    }
}