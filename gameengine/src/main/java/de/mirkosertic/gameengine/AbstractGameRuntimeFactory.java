package de.mirkosertic.gameengine;

import de.mirkosertic.gameengine.action.*;
import de.mirkosertic.gameengine.camera.CameraComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.camera.CameraComponentUnmarshaller;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.physics.ConstantMovementTemplateUnmarshaller;
import de.mirkosertic.gameengine.physics.ConstantMovementUnmarshaller;
import de.mirkosertic.gameengine.physics.*;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.processes.GameProcessManager;
import de.mirkosertic.gameengine.processes.GameProcessManagerFactory;
import de.mirkosertic.gameengine.sound.GameSoundManager;
import de.mirkosertic.gameengine.sound.GameSoundManagerFactory;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.sprites.SpriteComponentUnmarshaller;

public abstract class AbstractGameRuntimeFactory {

    public GameRuntime create(GameResourceLoader aResourceLoader, GameSoundSystemFactory aSoundSystemFactory) {

        GameEventManager theEventManager = new GameEventManager();
        GameProcessManagerFactory theProcessManagerFactory = new GameProcessManagerFactory();
        GameProcessManager theProcessManager = theProcessManagerFactory.create(theEventManager);

        // Physics system
        GamePhysicsManagerFactory thePhysicsManagerFactory = new JBox2DGamePhysicsManagerFactory();
        GamePhysicsManager thePhysicsManager = thePhysicsManagerFactory.create(theEventManager);

        // Runtime
        GameRuntime theGameRuntime = new GameRuntime(theEventManager, aResourceLoader);

        // Sound
        GameSoundManager theSoundManager = GameSoundManagerFactory.create(theEventManager, aSoundSystemFactory.create(theGameRuntime));

        theGameRuntime.addSystem(theProcessManager);
        theGameRuntime.addSystem(thePhysicsManager);
        theGameRuntime.addSystem(theSoundManager);

        IORegistry theRegistry = theGameRuntime.getIORegistry();

        theRegistry.registerTemplateUnmarshaller(new PhysicsComponentTemplateUnmarshaller());
        theRegistry.registerTemplateUnmarshaller(new CameraComponentTemplateUnmarshaller());
        theRegistry.registerTemplateUnmarshaller(new SpriteComponentTemplateUnmarshaller());
        theRegistry.registerTemplateUnmarshaller(new StaticComponentTemplateUnmarshaller());
        theRegistry.registerTemplateUnmarshaller(new PlatformComponentTemplateUnmarshaller());
        theRegistry.registerTemplateUnmarshaller(new ConstantMovementTemplateUnmarshaller());

        theRegistry.registerComponentUnmarshaller(new PhysicsComponentUnmarshaller());
        theRegistry.registerComponentUnmarshaller(new CameraComponentUnmarshaller());
        theRegistry.registerComponentUnmarshaller(new SpriteComponentUnmarshaller());
        theRegistry.registerComponentUnmarshaller(new StaticComponentUnmarshaller());
        theRegistry.registerComponentUnmarshaller(new PlatformComponentUnmarshaller());
        theRegistry.registerComponentUnmarshaller(new ConstantMovementUnmarshaller());

        theRegistry.registerConditionUnmarshaller(new KeyEventConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new ObjectCollisionConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new SystemTickConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new GameObjectInstanceAddedToSceneConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new GameObjectInstanceRemovedFromSceneConditionUnmarshaller());

        theRegistry.registerActionUnmarshaller(new PlaySoundActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new SetPropertyActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new RunSceneActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new SpawnGameObjectInstanceActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new DeleteGameObjectInstanceActionUnmarshaller());

        return theGameRuntime;
    }
}