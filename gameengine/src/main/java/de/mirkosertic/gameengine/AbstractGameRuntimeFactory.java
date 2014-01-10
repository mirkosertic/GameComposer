package de.mirkosertic.gameengine;

import de.mirkosertic.gameengine.action.ActionManagerFactory;
import de.mirkosertic.gameengine.arcade.ConstantMovementActionUnmarshaller;
import de.mirkosertic.gameengine.camera.CameraBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.camera.CameraBehaviorUnmarshaller;
import de.mirkosertic.gameengine.core.DeleteGameObjectInstanceActionUnmarshaller;
import de.mirkosertic.gameengine.core.ExpressionParserFactory;
import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToSceneConditionUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstanceLeftLayoutConditionUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstanceRemovedFromSceneConditionUnmarshaller;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.IORegistry;
import de.mirkosertic.gameengine.core.RunSceneActionUnmarshaller;
import de.mirkosertic.gameengine.core.SetPropertyActionUnmarshaller;
import de.mirkosertic.gameengine.core.SpawnGameObjectInstanceActionUnmarshaller;
import de.mirkosertic.gameengine.core.SystemTickConditionUnmarshaller;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.expression.GeExpressionParserFactory;
import de.mirkosertic.gameengine.input.KeyEventConditionUnmarshaller;
import de.mirkosertic.gameengine.physic.GamePhysicsManager;
import de.mirkosertic.gameengine.physic.GamePhysicsManagerFactory;
import de.mirkosertic.gameengine.physic.ObjectCollisionConditionUnmarshaller;
import de.mirkosertic.gameengine.physic.PhysicsBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.physic.PhysicsBehaviorUnmarshaller;
import de.mirkosertic.gameengine.physic.PlatformBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.physic.PlatformBehaviorUnmarshaller;
import de.mirkosertic.gameengine.physic.StaticBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.physic.StaticBehaviorUnmarshaller;
import de.mirkosertic.gameengine.physic.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehaviorUnmarshaller;
import de.mirkosertic.gameengine.process.GameProcessManager;
import de.mirkosertic.gameengine.process.GameProcessManagerFactory;
import de.mirkosertic.gameengine.process.KillProcessesForInstanceUnmarshaller;
import de.mirkosertic.gameengine.sound.GameSoundManager;
import de.mirkosertic.gameengine.sound.GameSoundManagerFactory;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;
import de.mirkosertic.gameengine.sound.PlaySoundActionUnmarshaller;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorUnmarshaller;
import de.mirkosertic.gameengine.text.TextBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.text.TextBehaviorUnmarshaller;

public abstract class AbstractGameRuntimeFactory {

    public GameRuntime create(GameResourceLoader aResourceLoader, GameSoundSystemFactory aSoundSystemFactory) {

        GameEventManager theEventManager = new GameEventManager();
        GameProcessManagerFactory theProcessManagerFactory = new GameProcessManagerFactory();
        GameProcessManager theProcessManager = theProcessManagerFactory.create(theEventManager);

        // Physics system
        GamePhysicsManagerFactory thePhysicsManagerFactory = new JBox2DGamePhysicsManagerFactory();
        GamePhysicsManager thePhysicsManager = thePhysicsManagerFactory.create(theEventManager);

        // Expression parser
        ExpressionParserFactory theExpressionParserFactory = new GeExpressionParserFactory();

        // Runtime
        GameRuntime theGameRuntime = new GameRuntime(theEventManager, aResourceLoader, theExpressionParserFactory);

        // Sound
        GameSoundManager theSoundManager = GameSoundManagerFactory.create(theEventManager, aSoundSystemFactory.create(theGameRuntime));

        theGameRuntime.addSystem(theProcessManager);
        theGameRuntime.addSystem(thePhysicsManager);
        theGameRuntime.addSystem(theSoundManager);

        IORegistry theRegistry = theGameRuntime.getIORegistry();

        theRegistry.registerBehaviorTemplateUnmarshaller(new PhysicsBehaviorTemplateUnmarshaller());
        theRegistry.registerBehaviorTemplateUnmarshaller(new CameraBehaviorTemplateUnmarshaller());
        theRegistry.registerBehaviorTemplateUnmarshaller(new SpriteBehaviorTemplateUnmarshaller());
        theRegistry.registerBehaviorTemplateUnmarshaller(new StaticBehaviorTemplateUnmarshaller());
        theRegistry.registerBehaviorTemplateUnmarshaller(new PlatformBehaviorTemplateUnmarshaller());
        theRegistry.registerBehaviorTemplateUnmarshaller(new TextBehaviorTemplateUnmarshaller());
        theRegistry.registerBehaviorTemplateUnmarshaller(new PlayerScoreBehaviorTemplateUnmarshaller());

        theRegistry.registerBehaviorUnmarshaller(new PhysicsBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new CameraBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new SpriteBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new StaticBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new PlatformBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new TextBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new PlayerScoreBehaviorUnmarshaller());

        theRegistry.registerConditionUnmarshaller(new KeyEventConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new ObjectCollisionConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new SystemTickConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new GameObjectInstanceAddedToSceneConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new GameObjectInstanceRemovedFromSceneConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new GameObjectInstanceLeftLayoutConditionUnmarshaller());

        theRegistry.registerActionUnmarshaller(new PlaySoundActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new SetPropertyActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new RunSceneActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new SpawnGameObjectInstanceActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new DeleteGameObjectInstanceActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new ConstantMovementActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new KillProcessesForInstanceUnmarshaller());

        return theGameRuntime;
    }

    public void loadingFinished(GameScene aLoadesScene) {
        // Finally we need to initialize the Action system, as now the scene is completely loaded
        ActionManagerFactory theActionManagerFactory = new ActionManagerFactory();
        aLoadesScene.getRuntime().addSystem(theActionManagerFactory.create(aLoadesScene, aLoadesScene.getRuntime().getEventManager()));
    }
}