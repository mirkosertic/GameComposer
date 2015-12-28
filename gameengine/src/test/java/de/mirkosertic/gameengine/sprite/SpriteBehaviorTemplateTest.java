package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectConfigurationChanged;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SpriteBehaviorTemplateTest {

    @Test
    public void testGetClassInformation() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameObject theOwner = mock(GameObject.class);

        SpriteBehaviorTemplate theTemplate = new SpriteBehaviorTemplate(theEventManager, theOwner);
        assertTrue(theTemplate.getClassInformation() instanceof SpriteClassInformation);
    }

    @Test
    public void testGetOwner() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameObject theOwner = mock(GameObject.class);

        SpriteBehaviorTemplate theTemplate = new SpriteBehaviorTemplate(theEventManager, theOwner);
        assertSame(theOwner, theTemplate.getOwner());
    }

    @Test
    public void testCreate() throws Exception {

        GameEventManager theEventManager = mock(GameEventManager.class);
        GameScene theScene = mock(GameScene.class);
        GameObject theOwner = mock(GameObject.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theOwner.getGameScene()).thenReturn(theScene);
        when(theScene.getRuntime()).thenReturn(theRuntime);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        when(theInstance.getOwnerGameObject()).thenReturn(theOwner);

        SpriteBehaviorTemplate theTemplate = new SpriteBehaviorTemplate(theEventManager, theOwner);
        Animation theAnimation = new Animation("Test");
        theTemplate.currentAnimationProperty().set(theAnimation);
        theTemplate.speedProperty().set(10);

        SpriteBehavior theBehavior = theTemplate.create(theInstance, theRuntime);
        assertSame(theAnimation, theBehavior.currentAnimationProperty().get());
        assertEquals(Integer.valueOf(10), theBehavior.speedProperty().get());
    }

    @Test
    public void testCurrentAnimationProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameObject theOwner = mock(GameObject.class);

        SpriteBehaviorTemplate theTemplate = new SpriteBehaviorTemplate(theEventManager, theOwner);
        Animation theAnimation = new Animation("Test");

        assertEquals("currentAnimation", theTemplate.currentAnimationProperty().getName());
        assertEquals(Animation.class, theTemplate.currentAnimationProperty().getType());
        assertTrue(theTemplate.currentAnimationProperty().isNull());
        theTemplate.currentAnimationProperty().set(theAnimation);
        assertSame(theAnimation, theTemplate.currentAnimationProperty().get());
        assertFalse(theTemplate.currentAnimationProperty().isNull());
    }

    @Test
    public void testSpeedProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameObject theOwner = mock(GameObject.class);

        SpriteBehaviorTemplate theTemplate = new SpriteBehaviorTemplate(theEventManager, theOwner);

        assertEquals("speed", theTemplate.speedProperty().getName());
        assertFalse(theTemplate.speedProperty().isNull());
        assertEquals(Integer.class, theTemplate.speedProperty().getType());
        assertEquals(Integer.valueOf(5), theTemplate.speedProperty().get());
        theTemplate.speedProperty().set(17);
        assertEquals(Integer.valueOf(17), theTemplate.speedProperty().get());
        assertFalse(theTemplate.speedProperty().isNull());
    }

    @Test
    public void testAddAnimation() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameObject theOwner = mock(GameObject.class);

        SpriteBehaviorTemplate theTemplate = new SpriteBehaviorTemplate(theEventManager, theOwner);
        assertEquals(0, theTemplate.getAnimations().length);
        Animation theAnimation = new Animation("Test");
        theTemplate.addAnimation(theAnimation);
        assertEquals(1, theTemplate.getAnimations().length);
        assertSame(theAnimation, theTemplate.getAnimations()[0]);

        verify(theEventManager).fire(any(GameObjectConfigurationChanged.class));
    }

    @Test
    public void testDelete() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameObject theOwner = mock(GameObject.class);
        GameScene theScene = mock(GameScene.class);
        when(theOwner.getGameScene()).thenReturn(theScene);

        SpriteBehaviorTemplate theTemplate = new SpriteBehaviorTemplate(theEventManager, theOwner);
        theTemplate.delete();

        verify(theScene).removeBehaviorFrom(eq(theOwner), eq(theTemplate));
    }

    @Test
    public void testGetGameScene() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameObject theOwner = mock(GameObject.class);
        GameScene theScene = mock(GameScene.class);
        when(theOwner.getGameScene()).thenReturn(theScene);

        SpriteBehaviorTemplate theTemplate = new SpriteBehaviorTemplate(theEventManager, theOwner);
        assertSame(theScene, theTemplate.getGameScene());
    }

    @Test
    public void testReplaceAnimation() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameObject theOwner = mock(GameObject.class);
        GameScene theScene = mock(GameScene.class);
        when(theOwner.getGameScene()).thenReturn(theScene);

        SpriteBehaviorTemplate theTemplate = new SpriteBehaviorTemplate(theEventManager, theOwner);
        Animation theOldAnimation = new Animation("Lala");
        Animation theOldAnimation2 = new Animation("Lala2");
        theTemplate.addAnimation(theOldAnimation);
        theTemplate.addAnimation(theOldAnimation2);
        Animation theNewAnimation = theOldAnimation.changeName("Lala3");
        theTemplate.replaceAnimation(theOldAnimation, theNewAnimation);
        assertEquals(2, theTemplate.getAnimations().length);
        assertSame(theNewAnimation, theTemplate.getAnimations()[0]);
        assertSame(theOldAnimation2, theTemplate.getAnimations()[1]);

        verify(theEventManager, times(3)).fire(any(GameObjectConfigurationChanged.class));
    }

    @Test
    public void testSerialize() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameObject theOwner = mock(GameObject.class);
        GameScene theScene = mock(GameScene.class);
        when(theOwner.getGameScene()).thenReturn(theScene);

        SpriteBehaviorTemplate theTemplate = new SpriteBehaviorTemplate(theEventManager, theOwner);
        Animation theOldAnimation = new Animation("Lala");
        Animation theOldAnimation2 = new Animation("Lala2");
        theTemplate.addAnimation(theOldAnimation);
        theTemplate.addAnimation(theOldAnimation2);
        theTemplate.currentAnimationProperty().set(theOldAnimation);

        Map<String, Object> theData = theTemplate.serialize();
        assertEquals(4, theData.size());
        assertEquals(SpriteBehavior.TYPE, theData.get(SpriteBehavior.TYPE_ATTRIBUTE));
        assertEquals("5", theData.get("speed"));
        assertEquals(theOldAnimation.getUuid(), theData.get("currentAnimation"));
        List<Map<String, Object>> theAnimations = (List<Map<String, Object>>) theData.get("animations");
        assertEquals(2, theAnimations.size());
        assertEquals("Lala", Animation.deserialize(theAnimations.get(0)).getName());
        assertEquals("Lala2", Animation.deserialize(theAnimations.get(1)).getName());

        theTemplate.currentAnimationProperty().set(null);
        theData = theTemplate.serialize();
        assertEquals(3, theData.size());
        assertEquals(SpriteBehavior.TYPE, theData.get(SpriteBehavior.TYPE_ATTRIBUTE));
        assertEquals("5", theData.get("speed"));
        theAnimations = (List<Map<String, Object>>) theData.get("animations");
        assertEquals(2, theAnimations.size());
        assertEquals("Lala", Animation.deserialize(theAnimations.get(0)).getName());
        assertEquals("Lala2", Animation.deserialize(theAnimations.get(1)).getName());

    }

    @Test
    public void testDeserialize1() throws Exception {
        GameEventManager theManager = mock(GameEventManager.class);
        GameObject theObject = mock(GameObject.class);

        Map<String, Object> theData = new HashMap<>();
        theData.put(SpriteBehavior.TYPE_ATTRIBUTE, SpriteBehavior.TYPE);
        theData.put("resourcename", new ResourceName("Lala").serialize());

        SpriteBehaviorTemplate theTemplate = SpriteBehaviorTemplate.deserialize(theManager, theObject, theData);
        assertEquals(Integer.valueOf(5), theTemplate.speedProperty().get());
        assertEquals(1, theTemplate.getAnimations().length);
        Animation theDefaultAnimation = theTemplate.getAnimations()[0];
        assertEquals("Default", theDefaultAnimation.getName());
        assertSame(theDefaultAnimation, theTemplate.currentAnimationProperty().get());
        assertEquals(1, theDefaultAnimation.getSequenceSize());
        assertEquals(new ResourceName("Lala"), theDefaultAnimation.getResourceByIndex(0));
    }

    @Test
    public void testDeserialize2() throws Exception {
        GameEventManager theManager = mock(GameEventManager.class);
        GameObject theObject = mock(GameObject.class);

        Map<String, Object> theData = new HashMap<>();
        theData.put(SpriteBehavior.TYPE_ATTRIBUTE, SpriteBehavior.TYPE);

        Animation theAnim1 = new Animation("1");
        Animation theAnim2 = new Animation("2");
        List<Map<String, Object>> theAnimations = new ArrayList<>();
        theAnimations.add(theAnim1.serialize());
        theAnimations.add(theAnim2.serialize());
        theData.put("animations", theAnimations);
        theData.put("currentAnimation", theAnim1.getUuid());
        theData.put("speed", "17");

        SpriteBehaviorTemplate theTemplate = SpriteBehaviorTemplate.deserialize(theManager, theObject, theData);
        assertEquals(Integer.valueOf(17), theTemplate.speedProperty().get());
        assertEquals(2, theTemplate.getAnimations().length);
        assertEquals(theAnim1.getName(), theTemplate.getAnimations()[0].getName());
        assertEquals(theAnim1.getUuid(), theTemplate.getAnimations()[0].getUuid());
        assertEquals(theAnim2.getName(), theTemplate.getAnimations()[1].getName());
        assertEquals(theAnim2.getUuid(), theTemplate.getAnimations()[1].getUuid());
        assertEquals(theAnim1.getName(), theTemplate.currentAnimationProperty().get().getName());
    }
}