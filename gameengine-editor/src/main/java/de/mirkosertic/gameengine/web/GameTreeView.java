package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.teavm.TeaVMDragEvent;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.HashMap;
import java.util.Map;

public class GameTreeView extends ListingElement {

    private final GameObjectEditor editor;
    private EditorHTMLElement oldSelection;
    private final Map<Object, EditorHTMLElement> knownObjects;
    private final Window window;

    public GameTreeView(HTMLElement aHtmlElement, HTMLTemplateEngine aTemplateEngine, GameObjectEditor aEditor, Window aWindow) {
        super(aHtmlElement, aTemplateEngine);
        editor = aEditor;
        knownObjects = new HashMap<>();
        window = aWindow;
    }

    @Override
    protected void clear() {
        super.clear();
        knownObjects.clear();
    }

    private void select(EditorHTMLElement aElement) {
        if (oldSelection != null) {
            String theClass = oldSelection.getAttribute("class");
            oldSelection.setAttribute("class", theClass.replace("selected", ""));
        }
        String theClass = aElement.getAttribute("class");
        aElement.setAttribute("class", theClass + " selected");
        aElement.scrollIntoView(false);
        oldSelection = aElement;
    }

    public void onGameSceneLoaded(GameScene aGameScene) {
        clear();
        EditorHTMLElement theSceneElement = addTitleLevel1(aGameScene.nameProperty().get());
        binder.add(HTMLInputBinder.forElementContent(theSceneElement, aGameScene.nameProperty()));
        knownObjects.put(aGameScene, theSceneElement);
        theSceneElement.addEventListener("click", evt -> {
            select(theSceneElement);
            editor.setEditingObject(aGameScene);
        });

        addTitleLevel2("Objects");
        for (GameObject theObject : aGameScene.getObjects()) {
            EditorHTMLElement theElement = addDraggable(theObject.nameProperty().get(), 1);
            binder.add(HTMLInputBinder.forElementContent(theElement, theObject.nameProperty()));
            knownObjects.put(theObject, theElement);
            theElement.addEventListener("click", evt -> {
                select(theElement);
                editor.setEditingObject(theObject);
            });
            theElement.addEventListener("dragstart", new EventListener<TeaVMDragEvent>() {
                        @Override
                        public void handleEvent(TeaVMDragEvent aEvent) {
                            aEvent.getDataTransfer().setData(Constants.DND_OBJECT_ID, theObject.uuidProperty().get());
                            window.getLocalStorage().setItem(Constants.DND_OBJECT_ID, theObject.uuidProperty().get());
                        }
                    });
        }
        addTitleLevel2("Eventsheets");
        for (EventSheet theSheet : aGameScene.getEventSheets()) {
            EditorHTMLElement theElement = add(theSheet.nameProperty().get(), 1);
            knownObjects.put(theSheet, theElement);
            binder.add(HTMLInputBinder.forElementContent(theElement, theSheet.nameProperty()));
            theElement.addEventListener("click", evt -> {
                select(theElement);
                editor.setEditingObject(theSheet);
            });
        }
        addTitleLevel2("Instances");
        for (GameObjectInstance theInstance : aGameScene.getInstances()) {
            EditorHTMLElement theElement = add(theInstance.nameProperty().get(), 1);
            knownObjects.put(theInstance, theElement);
            binder.add(HTMLInputBinder.forElementContent(theElement, theInstance.nameProperty()));
            theElement.addEventListener("click", evt -> {
                select(theElement);
                editor.setEditingObject(theInstance);
            });
        }
    }

    protected EditorHTMLElement add(String aTitle, int aLevel) {
        Map<String, Object> theParams = new HashMap<>();
        theParams.put("title", aTitle);
        theParams.put("level", Integer.toString(aLevel));
        EditorHTMLElement theElement = templateEngine.renderToElement("treeitem", theParams);
        htmlElement.appendChild(theElement);
        return theElement;
    }

    protected EditorHTMLElement addDraggable(String aTitle, int aLevel) {
        Map<String, Object> theParams = new HashMap<>();
        theParams.put("title", aTitle);
        theParams.put("level", Integer.toString(aLevel));
        EditorHTMLElement theElement = templateEngine.renderToElement("treeitemdraggable", theParams);
        htmlElement.appendChild(theElement);
        return theElement;
    }

    public void setEditingObject(Object aObject) {
        EditorHTMLElement theNewElement = knownObjects.get(aObject);
        if (theNewElement != null) {
            select(theNewElement);
        }
    }
}
