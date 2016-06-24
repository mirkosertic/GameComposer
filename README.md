FXGameComposer
=============

FXGameComposer is a game authoring tool and also a game runtime environment, targeting at JavaFX2 or HTML5 capable devices.

To see it in action please visit [this page](http://www.mirkosertic.de/doku.php/javastuff/javafxgameauthoring)

GameComposer
-----------

GameComposer is the authoring tool, the IDE. It is a JavaFX2 application. Using this tool, games can be designed
and tested. Finally, they can be exported to a target runtime, e.g. HTML5.

GameEngine
----------

GameEngine is the cross-platform game engine.

Core concepts and components of the game engine are:

| Concept /  component   | Description
|------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Game                   | A Game is the top level container. Each Game is split into several GameScenes.
| GameScene              | A Scene is like a level of a game. A Scene has a collection of GameObjects, Assets and Events.
| GameSystem             | A GameSystem defines a subsystem of the game engine. There are GameSystems for physics, sounds and other stuff
| GameView               | A GameView is the presentation of a GameScene to some kind of consumer. This can be a screen renderer or even a remote consumer.
| GameObject             | A GameObject is a template for visible objects. The behavior of a GameObject is defined by GameComponentTemplates
| BehaviorTemplate       | A BehaviorTemplate defines some kind of behavior, e.g. if it is static, a sprite or driven by physics.
| GameObjectInstance     | A GameObjectInstance is an instance of a GameObject. It inherits the defined BehaviorTemplates by copying them into a Behavior
| Behavior               | A Behavior is the behavior status for a BehaviorTemplate and a GameObjectInstance.
| Event                  | An Event can be triggered by a consumer by sending it via the GameView to the engine. An Event can also be triggered or consumed by a GameComponent or GameSystem.
| EventSheet             | An EventSheet is the rule engine. Each rule has a condition and some actions.
| GameLoop               | The GameLoop drives the engine by calling the GameSystems, rendering the GameView and dispatching events.
| GameProcess            | A GameProcess is something that takes multiple GameLoop cycles to complete, for instance playing background music or playing an animation.

Current travis-ci build status: [![Build Status](https://travis-ci.org/mirkosertic/GameComposer.svg?branch=master)](https://travis-ci.org/mirkosertic/GameComposer) [![Gitter](https://badges.gitter.im/mirkosertic/GameComposer.svg)](https://gitter.im/mirkosertic/GameComposer?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)


GameEngine-FXRenderer
---------------------

This is the game presentation logic by implementing a GameView using JavaFX 2. This also includes a Soundsystem implementation based on Java Sound API.

GameEngine-GWTRenderer
--------------

This is the game presentation logic using GWT and HTML5(JavaScript) by implementing a GameView. This also includes a SoundSystem implementation based on HTML5 Media API.
Currently the HTML5 renderer tries to use WebGL. If WebGL is not available, it will fallback to the HTML5 Canvas API.

GameEngine-AndroidRenderer
--------------

This is the game presentation logic using the Android Java SDK. This also includes a SoundSystem implementation for Android devices.
We use an Android SurfaceView for rendering. Future versions will include OpenGL ES support.

GameEngine-TeaVMRenderer
--------------

This is the game presentation logic using the TeaVM Java-to-JavaScript Transpiler Framework. It will render the game view
using the HTML5 Canvas API.

GameEngine-Networking
--------------

Networking support is implemented as a RemoteGameView. Local events are sent between game instances to sync the distributed game models.

Networking is implemented in a very early beta state only by the TeaVM Renderer. Events are sent to a Firebase instance, which
then syncs the events back to other browsers bound to the same Firebase instance. Take a look at the [Networking](http://mirkosertic.github.io/GameComposer/games/teavm/networking/index.html) example
to see it in action. In this case, Firebase acts as a non-authorative game server. The game models and simulation is still run in the local browser.
Only the game state is synchronized using events.


Example Games
-------------

TeaVM Renderer:

[Bouncing block](http://mirkosertic.github.io/GameComposer/games/teavm/bounce/index.html)

[Camera with Clock](http://mirkosertic.github.io/GameComposer/games/teavm/camera/index.html)

[Gravity and Mouse](http://mirkosertic.github.io/GameComposer/games/teavm/gravity/index.html)

[Duke on the Platform](http://mirkosertic.github.io/GameComposer/games/teavm/platformer/index.html)

[Position test](http://mirkosertic.github.io/GameComposer/games/teavm/positiontest/index.html)

[Rotating actor](http://mirkosertic.github.io/GameComposer/games/teavm/rotatingactor/index.html)

[Networking](http://mirkosertic.github.io/GameComposer/games/teavm/networking/index.html)


GWT Renderer:

[Bouncing block](http://mirkosertic.github.io/GameComposer/games/gwt/bounce/index.html)

[Camera with Clock](http://mirkosertic.github.io/GameComposer/games/gwt/camera/index.html)

[Gravity and Mouse](http://mirkosertic.github.io/GameComposer/games/gwt/gravity/index.html)

[Duke on the Platform](http://mirkosertic.github.io/GameComposer/games/gwt/platformer/index.html)

[Position test](http://mirkosertic.github.io/GameComposer/games/gwt/positiontest/index.html)

[Rotating actor](http://mirkosertic.github.io/GameComposer/games/gwt/rotatingactor/index.html)


Dragome Renderer (no longer Supported):

[Bouncing block](http://mirkosertic.github.io/GameComposer/games/dragome/bounce/index.html)

[Camera with Clock](http://mirkosertic.github.io/GameComposer/games/dragome/camera/index.html)

[Gravity and Mouse](http://mirkosertic.github.io/GameComposer/games/dragome/gravity/index.html)

[Duke on the Platform](http://mirkosertic.github.io/GameComposer/games/dragome/platformer/index.html)

[Position test](http://mirkosertic.github.io/GameComposer/games/dragome/positiontest/index.html)

[Rotating actor](http://mirkosertic.github.io/GameComposer/games/dragome/rotatingactor/index.html)

