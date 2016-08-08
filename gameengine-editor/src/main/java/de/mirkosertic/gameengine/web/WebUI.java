package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.physic.GamePhysicsManagerFactory;
import de.mirkosertic.gameengine.physics.jbox2d.JBox2DGamePhysicsManagerFactory;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngineFactory;
import de.mirkosertic.gameengine.scriptengine.luaj.LuaJScriptEngineFactory;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;
import de.mirkosertic.gameengine.type.BuiltInFunctions;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.ResourceName;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

import java.io.IOException;

public class WebUI {

    private static final Window window = Window.current();
    private static final HTML5Document document = (HTML5Document) window.getDocument();

    private ObjectEditor objectEditor;

    public static void main(String[] args) {
        WebUI theRenderer = new WebUI();
        theRenderer.boot();
    }

    private void boot() {
        HTMLElement theElement = (HTMLElement) document.getElementById("objectEditor");
        TemplateEngine theTemplateEngine = new TemplateEngine(document);
        objectEditor = new ObjectEditor(theElement, theTemplateEngine);

        AbstractGameRuntimeFactory theFactory = new AbstractGameRuntimeFactory() {
            @Override
            protected ThreadingManager createThreadingManager() {
                return new NoThreadingThreadingManager();
            }

            @Override
            protected LUAScriptEngineFactory createScriptEngine() {
                return new LuaJScriptEngineFactory(new BuiltInFunctions() {
                    @Override
                    public String formatTime(Number aTimeInMilis, String aPattern) {
                        return aPattern;
                    }

                    @Override
                    public ClassInformation getClassInformation() {
                        return null;
                    }
                });
            }

            @Override
            protected GamePhysicsManagerFactory createPhysicsManagerFactory() {
                return new JBox2DGamePhysicsManagerFactory();
            }
        };

        GameResourceLoader theLoader = new GameResourceLoader() {
            @Override
            public GameResource load(ResourceName aResourceName) throws IOException {
                return null;
            }

            @Override
            public void flush() {
            }
        };

        GameSoundSystemFactory theSoundSystemFactory = aGameRuntime -> new GameSoundSystem() {
            @Override
            public Object play(ResourceName aResourceName) {
                return null;
            }

            @Override
            public void stop(Object aSoundObject) {
            }
        };

        Game theGame = new Game();
        GameScene theScene = new GameScene(theGame, theFactory.create(theLoader, theSoundSystemFactory));
        GameObject theObject = theScene.createNewGameObject("Test");

        objectEditor.setEditingObject(theObject);
    }
}
