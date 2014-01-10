package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.type.Size;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertyDiscovererTest {

    @Test
    public void testSetProperty() {
        GameEventManager theEventManager = new GameEventManager();
        GameRuntime theRuntine = mock(GameRuntime.class);
        when(theRuntine.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntine);

        GameObject theObject = new GameObject(theScene, "name");
        SpriteBehaviorTemplate theSpriteComponent = new SpriteBehaviorTemplate(theEventManager, theObject);
        theObject.add(theSpriteComponent);

        PropertyDiscoverer theDiscoverer = new PropertyDiscoverer();
        theDiscoverer.setVariable(theObject, "size", new Size(80, 80));

        Assert.assertEquals(new Size(80, 80), theObject.sizeProperty().get());

        theDiscoverer.setVariable(theObject, "sprite.resourceName", new ResourceName("abc"));
        Assert.assertEquals(new ResourceName("abc"), theSpriteComponent.resourceNameProperty().get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidProperty1() {
        GameEventManager theEventManager = new GameEventManager();
        GameRuntime theRuntine = mock(GameRuntime.class);
        when(theRuntine.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntine);

        GameObject theObject = new GameObject(theScene, "name");
        SpriteBehaviorTemplate theSpriteComponent = new SpriteBehaviorTemplate(theEventManager, theObject);
        theObject.add(theSpriteComponent);

        PropertyDiscoverer theDiscoverer = new PropertyDiscoverer();
        theDiscoverer.setVariable(theObject, "a", "b");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidProperty2() {
        GameEventManager theEventManager = new GameEventManager();
        GameRuntime theRuntine = mock(GameRuntime.class);
        when(theRuntine.getEventManager()).thenReturn(theEventManager);
        GameScene theScene = mock(GameScene.class);
        when(theScene.getRuntime()).thenReturn(theRuntine);

        GameObject theObject = new GameObject(theScene, "name");
        SpriteBehaviorTemplate theSpriteComponent = new SpriteBehaviorTemplate(theEventManager, theObject);
        theObject.add(theSpriteComponent);

        PropertyDiscoverer theDiscoverer = new PropertyDiscoverer();
        theDiscoverer.setVariable(theObject, "sprite.a", "b");
    }
}
