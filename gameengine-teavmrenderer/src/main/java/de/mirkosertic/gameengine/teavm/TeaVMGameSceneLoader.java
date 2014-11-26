package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.GameScene;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.teavm.dom.ajax.ReadyStateChangeHandler;
import org.teavm.dom.ajax.XMLHttpRequest;
import org.teavm.dom.browser.Window;
import org.teavm.dom.html.HTMLElement;
import org.teavm.dom.html.HTMLDocument;
import org.teavm.dom.html.HTMLElement;

import java.util.Map;

public class TeaVMGameSceneLoader {

    public static interface GameSceneLoadedListener {
        void onGameSceneLoaded(GameScene aScene);
        void onGameSceneLoadedError(Throwable aThrowable);
    }

    private final GameSceneLoadedListener listener;
    private final AbstractGameRuntimeFactory runtimeFactory;
    private final Window window;

    public TeaVMGameSceneLoader(GameSceneLoadedListener aListener, AbstractGameRuntimeFactory aRuntimeFactory, Window aWindow) {
        listener = aListener;
        runtimeFactory = aRuntimeFactory;
        window = aWindow;
    }

    public void loadFromServer(String aSceneName, final TeaVMGameResourceLoader aResourceLoader) {
        final XMLHttpRequest theRequest = window.createXMLHttpRequest();
        theRequest.open("GET", aSceneName+"/scene.json");
        theRequest.setOnReadyStateChange(new ReadyStateChangeHandler() {
            @Override
            public void stateChanged() {
                switch (theRequest.getReadyState()) {
                    default:
                        break;
                    case XMLHttpRequest.DONE:
                        listener.onGameSceneLoaded(parse(theRequest.getResponseText(), aResourceLoader));
                }
            }
        });
        theRequest.send();
    }

    private GameScene parse(String aResponse, TeaVMGameResourceLoader aResourceLoader) {
        //JSONAPI theAPI = ((JSONAPIProvider) JS.getGlobal()).getJSONAPI();
        //Map<String, Object> theResult = new JSONMap(theAPI.parse(aResponse));

        HTMLDocument document = window.getDocument();

        try {
            HTMLElement div = document.createElement("div");
            div.appendChild(document.createTextNode(aResponse));
            document.getBody().appendChild(div);

            Object theObjectResult = JSONValue.parseWithException(aResponse);

            div = document.createElement("div");
            div.appendChild(document.createTextNode("Loaded " + aResponse));
            document.getBody().appendChild(div);

            Map<String, Object> theResult = (Map<String, Object>) theObjectResult;


            div = document.createElement("div");
            div.appendChild(document.createTextNode("Loaded " + theResult));
            document.getBody().appendChild(div);


            return GameScene.deserialize(runtimeFactory.create(aResourceLoader, new TeaVMGameSoundSystemFactory()), theResult);
        } catch (ParseException e) {
            HTMLElement div = document.createElement("div");
            div.appendChild(document.createTextNode("EX " + e.getPosition()));
            document.getBody().appendChild(div);

            throw new RuntimeException(e);
        }
    }
}
