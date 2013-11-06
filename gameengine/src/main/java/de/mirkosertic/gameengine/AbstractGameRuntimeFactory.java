package de.mirkosertic.gameengine;

import de.mirkosertic.gameengine.camera.CameraComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.camera.CameraComponentUnmarshaller;
import de.mirkosertic.gameengine.core.GameEventManager;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.physics.*;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.processes.GameProcessManager;
import de.mirkosertic.gameengine.processes.GameProcessManagerFactory;
import de.mirkosertic.gameengine.resource.GameResourceLoader;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.sprites.SpriteComponentUnmarshaller;

public abstract class AbstractGameRuntimeFactory {

    public GameRuntime create(GameResourceLoader aResourceLoader) {
        GameEventManager theEventManager = new GameEventManager();
        GameProcessManagerFactory theProcessManagerFactory = new GameProcessManagerFactory();
        GameProcessManager theProcessManager = theProcessManagerFactory.create(theEventManager);

        // Physics system
        GamePhysicsManagerFactory thePhysicsManagerFactory = new JBox2DGamePhysicsManagerFactory();
        GamePhysicsManager thePhysicsManager = thePhysicsManagerFactory.create(theEventManager);

        // Runtime
        GameRuntime theGameRuntime = new GameRuntime(theEventManager, aResourceLoader);
        theGameRuntime.addSystem(theProcessManager);
        theGameRuntime.addSystem(thePhysicsManager);
        theGameRuntime.registeredTemplateUnmarshaller(new PhysicsComponentTemplateUnmarshaller());
        theGameRuntime.registeredTemplateUnmarshaller(new CameraComponentTemplateUnmarshaller());
        theGameRuntime.registeredTemplateUnmarshaller(new SpriteComponentTemplateUnmarshaller());
        theGameRuntime.registeredTemplateUnmarshaller(new StaticComponentTemplateUnmarshaller());
        theGameRuntime.registeredTemplateUnmarshaller(new PlatformComponentTemplateUnmarshaller());

        theGameRuntime.registeredComponentUnmarshaller(new PhysicsComponentUnmarshaller());
        theGameRuntime.registeredComponentUnmarshaller(new CameraComponentUnmarshaller());
        theGameRuntime.registeredComponentUnmarshaller(new SpriteComponentUnmarshaller());
        theGameRuntime.registeredComponentUnmarshaller(new StaticComponentUnmarshaller());
        theGameRuntime.registeredComponentUnmarshaller(new PlatformComponentUnmarshaller());

        return theGameRuntime;
    }
}
