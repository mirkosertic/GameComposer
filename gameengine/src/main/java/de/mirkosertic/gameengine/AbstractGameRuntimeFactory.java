/*
 * Copyright 2013 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine;

import de.mirkosertic.gameengine.action.ActionManagerFactory;
import de.mirkosertic.gameengine.action.SystemTickConditionUnmarshaller;
import de.mirkosertic.gameengine.arcade.ConstantMovementBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.arcade.ConstantMovementBehaviorUnmarshaller;
import de.mirkosertic.gameengine.arcaderacer.ArcadeRacerGameScaneEffectUnmarshaller;
import de.mirkosertic.gameengine.camera.CameraBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.camera.CameraBehaviorUnmarshaller;
import de.mirkosertic.gameengine.core.DefaultLogger;
import de.mirkosertic.gameengine.core.DeleteGameObjectInstanceActionUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToSceneConditionUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstanceLeftLayoutConditionUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstanceRemovedFromSceneConditionUnmarshaller;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.IORegistry;
import de.mirkosertic.gameengine.core.Logger;
import de.mirkosertic.gameengine.core.RunSceneActionUnmarshaller;
import de.mirkosertic.gameengine.core.SceneStartedConditionUnmarshaller;
import de.mirkosertic.gameengine.core.SpawnGameObjectInstanceActionUnmarshaller;
import de.mirkosertic.gameengine.core.ThreadingManager;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.input.KeyEventConditionUnmarshaller;
import de.mirkosertic.gameengine.input.MouseEventConditionUnmarshaller;
import de.mirkosertic.gameengine.physic.GamePhysicsManager;
import de.mirkosertic.gameengine.physic.GamePhysicsManagerFactory;
import de.mirkosertic.gameengine.physic.ObjectCollisionConditionUnmarshaller;
import de.mirkosertic.gameengine.physic.PhysicsBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.physic.PhysicsBehaviorUnmarshaller;
import de.mirkosertic.gameengine.physic.PlatformBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.physic.PlatformBehaviorUnmarshaller;
import de.mirkosertic.gameengine.physic.StaticBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.physic.StaticBehaviorUnmarshaller;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehaviorUnmarshaller;
import de.mirkosertic.gameengine.process.GameProcessManager;
import de.mirkosertic.gameengine.process.GameProcessManagerFactory;
import de.mirkosertic.gameengine.process.KillProcessesForInstanceUnmarshaller;
import de.mirkosertic.gameengine.script.RunScriptActionUnmarshaller;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngineFactory;
import de.mirkosertic.gameengine.sound.GameSoundManager;
import de.mirkosertic.gameengine.sound.GameSoundManagerFactory;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;
import de.mirkosertic.gameengine.sound.PlaySoundActionUnmarshaller;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorUnmarshaller;
import de.mirkosertic.gameengine.starfield.StarfieldGameSceneEffectUnmarshaller;
import de.mirkosertic.gameengine.text.TextBehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.text.TextBehaviorUnmarshaller;

public abstract class AbstractGameRuntimeFactory {

    protected abstract ThreadingManager createThreadingManager();

    protected abstract LUAScriptEngineFactory createScriptEngine();

    protected abstract GamePhysicsManagerFactory createPhysicsManagerFactory();

    public GameRuntime create(GameResourceLoader aResourceLoader, GameSoundSystemFactory aSoundSystemFactory) {

        Logger theLogger = createLogger();

        GameEventManager theEventManager = new GameEventManager(theLogger);
        GameProcessManagerFactory theProcessManagerFactory = new GameProcessManagerFactory();
        GameProcessManager theProcessManager = theProcessManagerFactory.create(theLogger, theEventManager);

        ThreadingManager theThreadingManager = createThreadingManager();

        // Physics system
        GamePhysicsManagerFactory thePhysicsManagerFactory = createPhysicsManagerFactory();
        GamePhysicsManager thePhysicsManager = thePhysicsManagerFactory.create(theLogger, theEventManager, theThreadingManager);

        // By default we use Lua as a script engine
        LUAScriptEngineFactory theScriptEngineFactory = createScriptEngine();

        // Runtime
        GameRuntime theGameRuntime = new GameRuntime(theEventManager, aResourceLoader, theScriptEngineFactory, theLogger);

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
        theRegistry.registerBehaviorTemplateUnmarshaller(new ConstantMovementBehaviorTemplateUnmarshaller());

        theRegistry.registerBehaviorUnmarshaller(new PhysicsBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new CameraBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new SpriteBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new StaticBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new PlatformBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new TextBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new PlayerScoreBehaviorUnmarshaller());
        theRegistry.registerBehaviorUnmarshaller(new ConstantMovementBehaviorUnmarshaller());

        theRegistry.registerConditionUnmarshaller(new SceneStartedConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new KeyEventConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new ObjectCollisionConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new SystemTickConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new GameObjectInstanceAddedToSceneConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new GameObjectInstanceRemovedFromSceneConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new GameObjectInstanceLeftLayoutConditionUnmarshaller());
        theRegistry.registerConditionUnmarshaller(new MouseEventConditionUnmarshaller());

        theRegistry.registerActionUnmarshaller(new PlaySoundActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new RunSceneActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new SpawnGameObjectInstanceActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new DeleteGameObjectInstanceActionUnmarshaller());
        theRegistry.registerActionUnmarshaller(new KillProcessesForInstanceUnmarshaller());
        theRegistry.registerActionUnmarshaller(new RunScriptActionUnmarshaller());

        theRegistry.registerGameSceneEffectUnmarshaller(new StarfieldGameSceneEffectUnmarshaller());
        theRegistry.registerGameSceneEffectUnmarshaller(new ArcadeRacerGameScaneEffectUnmarshaller());

        return theGameRuntime;
    }

    public void loadingFinished(GameScene aLoadesScene) {
        // Finally we need to initialize the Action system, as now the scene is completely loaded
        ActionManagerFactory theActionManagerFactory = new ActionManagerFactory();
        aLoadesScene.getRuntime().addSystem(theActionManagerFactory.create(aLoadesScene, aLoadesScene.getRuntime().getEventManager()));
    }

    protected Logger createLogger() {
        return new DefaultLogger();
    }
}