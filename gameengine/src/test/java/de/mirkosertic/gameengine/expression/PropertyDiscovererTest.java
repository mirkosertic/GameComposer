package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertyDiscovererTest {

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
