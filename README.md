GameComposer
=============

GameComposer is a game authoring tool and also a game runtime environment targeting at desktop and mobile devices.

Started as a JavaFX implementation some years ago, it now supports desktop and mobile devices, with support for modern technologies such as HTML5, WebGL, Apache Cordova/Crosswalk and Electron.

Its core is based on Domain-driven Design and a hexagonal architecture. The main game simulation logic stayed the same for years, but
replacing presentation logic and adapting it to new technologies is a lot easier compared to traditional layered applications.

To see it in action please visit [this page](https://www.mirkosertic.de/blog/2013/11/a-javafx-based-game-authoring-system/)

Current build status: ![Build](https://github.com/mirkosertic/GameComposer/workflows/Build/badge.svg) 

GameEngine-Editor
-----------------

This is the shiny new Web-enabled editor for the GameEngine. It is based on HTML5, Polymer/Webcomponents, Electron and TeaVM. 

![Webeditor in Action](https://raw.githubusercontent.com/mirkosertic/GameComposer/master/docs/images/webeditor.png)

[Edit Dukes Adventure Example Game](https://mirkosertic.github.io/GameComposer/games/editor/index.html)

![Webeditor editing an Event Sheet](https://raw.githubusercontent.com/mirkosertic/GameComposer/master/docs/images/webeventsheet.png)

![Webeditor editing a LUA script](https://raw.githubusercontent.com/mirkosertic/GameComposer/master/docs/images/luawebeditor.png)

The Electron Releases are available for download at the GitHub Releases section.

![Webeditor in Electron](https://raw.githubusercontent.com/mirkosertic/GameComposer/master/docs/images/electron.png)

Example Games
-------------

TeaVM Renderer:

[Bouncing block](https://mirkosertic.github.io/GameComposer/games/teavm/bounce/index.html)

[Camera with Clock](https://mirkosertic.github.io/GameComposer/games/teavm/camera/index.html)

[Gravity and Mouse](https://mirkosertic.github.io/GameComposer/games/teavm/gravity/index.html)

[Duke on the Platform](https://mirkosertic.github.io/GameComposer/games/teavm/platformer/index.html)

[Position test](https://mirkosertic.github.io/GameComposer/games/teavm/positiontest/index.html)

[Rotating actor](https://mirkosertic.github.io/GameComposer/games/teavm/rotatingactor/index.html)

[Networking](https://mirkosertic.github.io/GameComposer/games/teavm/networking/index.html)

[Arcade Racer](https://mirkosertic.github.io/GameComposer/games/teavm/arcaderacer/index.html)


GWT Renderer:

[Bouncing block](https://mirkosertic.github.io/GameComposer/games/gwt/bounce/index.html)

[Camera with Clock](https://mirkosertic.github.io/GameComposer/games/gwt/camera/index.html)

[Gravity and Mouse](https://mirkosertic.github.io/GameComposer/games/gwt/gravity/index.html)

[Duke on the Platform](https://mirkosertic.github.io/GameComposer/games/gwt/platformer/index.html)

[Position test](https://mirkosertic.github.io/GameComposer/games/gwt/positiontest/index.html)

[Rotating actor](https://mirkosertic.github.io/GameComposer/games/gwt/rotatingactor/index.html)

[Arcade Racer](https://mirkosertic.github.io/GameComposer/games/gwt/arcaderacer/index.html)


Dragome Renderer:

[Bouncing block](https://mirkosertic.github.io/GameComposer/games/dragome/bounce/index.html)

[Camera with Clock](https://mirkosertic.github.io/GameComposer/games/dragome/camera/index.html)

[Gravity and Mouse](https://mirkosertic.github.io/GameComposer/games/dragome/gravity/index.html)

[Duke on the Platform](https://mirkosertic.github.io/GameComposer/games/dragome/platformer/index.html)

[Position test](https://mirkosertic.github.io/GameComposer/games/dragome/positiontest/index.html)

[Rotating actor](https://mirkosertic.github.io/GameComposer/games/dragome/rotatingactor/index.html)

[Arcade Racer](https://mirkosertic.github.io/GameComposer/games/dragome/arcaderacer/index.html) 


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

GameEngine-Networking
--------------

Networking support is implemented as a RemoteGameView. Local events are sent between game instances to sync the distributed game models.

Networking is implemented in a very early beta state only by the TeaVM Renderer. Events are sent to a Firebase instance, which
then syncs the events back to other browsers bound to the same Firebase instance. Take a look at the [Networking](https://mirkosertic.github.io/GameComposer/games/teavm/networking/index.html) example
to see it in action. In this case, Firebase acts as a non-authorative game server. The game models and simulation is still run in the local browser.
Only the game state is synchronized using events.

GameEngine-TeaVMRenderer (preferred)
--------------

This is the game presentation logic using the TeaVM Java-to-JavaScript Transpiler Framework. It will render the game view
using [pixi.js](http://www.pixijs.com), which uses WebGL or the HTML5 Canvas API as a fallback. Sound is done using [howler.js](https://github.com/goldfire/howler.js/) 

GameEngine-BytecoderRenderer (experimental)
--------------

This is a first implementation of a [WebAssembly](http://webassembly.org/) based GameRenderer. Under the hood it uses 
[Bytecoder](https://github.com/mirkosertic/Bytecoder) to compile the Game Runtime to WebAssembly and plain old JavaScript as a fallback.
It will render the game view using [pixi.js](http://www.pixijs.com), which uses WebGL or the HTML5 Canvas API as a fallback.

Please note that this is experimental stage!

Example games:

* [Physics Game Example compiled to WebAssembly](https://www.mirkosertic.de/examples/gameengine/index.html)
* [Physics Game Example compiled to JavaScript](https://www.mirkosertic.de/examples/gameengine/indexjs.html)

GameEngine-CordovaRenderer (experimental)
--------------

This is basically the TeaVMRenderer packaged as a Cordova Application backed by Crosswalk(Chromium).

![A game running in Android Emulator](https://raw.githubusercontent.com/mirkosertic/GameComposer/master/docs/images/android.png)


GameEngine-FXRenderer (outdated)
---------------------

This is the game presentation logic by implementing a GameView using JavaFX 2. This also includes a Soundsystem implementation based on Java Sound API.

GameEngine-AndroidRenderer (outdated)
--------------

This is the game presentation logic using the Android Java SDK. This also includes a SoundSystem implementation for Android devices.
We use an Android SurfaceView for rendering. Future versions will include OpenGL ES support.

GameEngine-GWTRenderer (outdated)
--------------

This is the game presentation logic using GWT and HTML5(JavaScript) by implementing a GameView. This also includes a SoundSystem implementation based on HTML5 Media API.
Currently the HTML5 renderer tries to use WebGL. If WebGL is not available, it will fallback to the HTML5 Canvas API.

GameEngine-DragomeRenderer (experimental)
--------------

This is the game presentation logic using the Dragome Java-to-JavaScript Transpiler Framework. It will render the game view
using the HTML5 Canvas API.

GameComposer (outdated)
-----------

GameComposer is the authoring tool, the IDE. It is a JavaFX2 application. Using this tool, games can be designed
and tested. Finally, they can be exported to a target runtime, e.g. HTML5.

![JavaFX IDE](https://raw.githubusercontent.com/mirkosertic/GameComposer/master/docs/images/fxeditor.png)
