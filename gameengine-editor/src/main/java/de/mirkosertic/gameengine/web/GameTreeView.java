package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.HashMap;
import java.util.Map;

public class GameTreeView extends ListingElement {

    private final GameObjectEditor editor;
    private HTMLElement oldSelection;

    public GameTreeView(HTMLElement aHtmlElement, HTMLTemplateEngine aTemplateEngine, GameObjectEditor aEditor) {
        super(aHtmlElement, aTemplateEngine);
        editor = aEditor;
    }

    private void select(HTMLElement aElement) {
        if (oldSelection != null) {
            String theClass = oldSelection.getAttribute("class");
            oldSelection.setAttribute("class", theClass.replace("selected", ""));
        }
        String theClass = aElement.getAttribute("class");
        aElement.setAttribute("class", theClass + " selected");
        oldSelection = aElement;
    }

    public void onGameSceneLoaded(GameScene aGameScene) {
        clear();
        HTMLElement theSceneElement = addTitleLevel1(aGameScene.nameProperty().get());
        theSceneElement.addEventListener("click", evt -> {
            select(theSceneElement);
            editor.setEditingObject((GameScene) aGameScene);
        });

        addTitleLevel2("Objects");
        for (GameObject theObject : aGameScene.getObjects()) {
            HTMLElement theElement = add(theObject.nameProperty().get(), 1);
            theElement.addEventListener("click", evt -> {
                select(theElement);
                editor.setEditingObject((GameObject) theObject);
            });
        }
        addTitleLevel2("Eventsheets");
        for (EventSheet theSheet : aGameScene.getEventSheets()) {
            HTMLElement theElement = add(theSheet.nameProperty().get(), 1);
            theElement.addEventListener("click", evt -> {
                select(theElement);
                editor.setEditingObject((EventSheet) theSheet);
            });
        }
        addTitleLevel2("Instances");
        for (GameObjectInstance theInstance : aGameScene.getInstances()) {
            HTMLElement theElement = add(theInstance.nameProperty().get(), 1);
            theElement.addEventListener("click", evt -> {
                select(theElement);
                editor.setEditingObject((GameObjectInstance) theInstance);
            });
        }
    }

    protected HTMLElement add(String aTitle, int aLevel) {
        Map<String, Object> theParams = new HashMap<>();
        theParams.put("title", aTitle);
        theParams.put("level", Integer.toString(aLevel));
        HTMLElement theElement = templateEngine.renderToElement("treeitem", theParams);
        htmlElement.appendChild(theElement);
        return theElement;
    }
}
