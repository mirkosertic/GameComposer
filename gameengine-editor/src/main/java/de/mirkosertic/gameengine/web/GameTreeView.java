/*
 * Copyright 2016 Mirko Sertic
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
    private TreeItemHTMLElement oldSelection;
    private final Map<Object, TreeItemHTMLElement> knownObjects;
    private final Window window;

    public GameTreeView(HTMLElement aHtmlElement, GameObjectEditor aEditor, Window aWindow) {
        super(aHtmlElement);
        editor = aEditor;
        knownObjects = new HashMap<>();
        window = aWindow;
    }

    @Override
    protected void clear() {
        super.clear();
        knownObjects.clear();
    }

    private void select(TreeItemHTMLElement aElement) {
        if (oldSelection != null) {
            oldSelection.setSelected(false);
        }
        aElement.setSelected(true);
        aElement.scrollIntoView(false);
        oldSelection = aElement;
    }

    public void onGameSceneLoaded(GameScene aGameScene) {
        clear();

        TreeItemHTMLElement theSceneElement = addTreeItem(1);
        theSceneElement.setSeparator(true);
        binder.add(theSceneElement.bindTo(aGameScene.nameProperty()));
        knownObjects.put(aGameScene, theSceneElement);
        theSceneElement.addEventListener("click", evt -> {
            select(theSceneElement);
            editor.setEditingObject(aGameScene);
        });

        addTitleLevel2("Objects");
        for (GameObject theObject : aGameScene.getObjects()) {

            TreeItemHTMLElement theElement = addTreeItem(1);
            theElement.setDraggable(true);
            binder.add(theElement.bindTo(theObject.nameProperty()));
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
            TreeItemHTMLElement theElement = addTreeItem(1);
            knownObjects.put(theSheet, theElement);
            binder.add(theElement.bindTo(theSheet.nameProperty()));

            theElement.addEventListener("click", evt -> {
                select(theElement);
                editor.setEditingObject(theSheet);
            });
        }
        addTitleLevel2("Instances");
        for (GameObjectInstance theInstance : aGameScene.getInstances()) {
            TreeItemHTMLElement theElement = addTreeItem(1);
            knownObjects.put(theInstance, theElement);
            binder.add(theElement.bindTo(theInstance.nameProperty()));
            theElement.addEventListener("click", evt -> {
                select(theElement);
                editor.setEditingObject(theInstance);
            });
        }
    }

    protected TreeItemHTMLElement addTreeItem(int aLevel) {
        TreeItemHTMLElement theElement = TreeItemHTMLElement.create();
        theElement.setLevel(aLevel);
        htmlElement.appendChild(theElement);
        return theElement;
    }

    public void setEditingObject(Object aObject) {
        TreeItemHTMLElement theNewElement = knownObjects.get(aObject);
        if (theNewElement != null) {
            select(theNewElement);
        }
    }
}