package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class SpriteBehaviorTemplateUnmarshallerTest {

    @Test
    public void testGetTypeKey() throws Exception {
        SpriteBehaviorTemplateUnmarshaller theUnmarshaller = new SpriteBehaviorTemplateUnmarshaller();
        assertEquals(SpriteBehavior.TYPE, theUnmarshaller.getTypeKey());
    }

    @Test
    public void testDeserialize() throws Exception {
        SpriteBehaviorTemplateUnmarshaller theUnmarshaller = new SpriteBehaviorTemplateUnmarshaller();

        GameEventManager theManager = mock(GameEventManager.class);
        GameObject theObject = mock(GameObject.class);

        Map<String, Object> theData = new HashMap<>();
        theData.put(SpriteBehavior.TYPE_ATTRIBUTE, SpriteBehavior.TYPE);
        theData.put("resourcename", new ResourceName("Lala").serialize());

        SpriteBehaviorTemplate theTemplate = theUnmarshaller.deserialize(theManager, theObject, theData);
        assertEquals(Integer.valueOf(5), theTemplate.speedProperty().get());
        assertEquals(1, theTemplate.getAnimations().length);
        Animation theDefaultAnimation = theTemplate.getAnimations()[0];
        assertEquals("Default", theDefaultAnimation.getName());
        assertSame(theDefaultAnimation, theTemplate.currentAnimationProperty().get());
        assertEquals(1, theDefaultAnimation.getSequenceSize());
        assertEquals(new ResourceName("Lala"), theDefaultAnimation.getResourceByIndex(0));
    }
}