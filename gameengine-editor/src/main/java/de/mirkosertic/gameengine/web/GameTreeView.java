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

import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLElement;

import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.Spritesheet;
import de.mirkosertic.gameengine.teavm.TeaVMDragEvent;

public class GameTreeView extends ListingElement {

    public interface EventHandler {

        void setEditingObject(Game aGame);
        void setEditingObject(GameScene aScene);
        void setEditingObject(GameObject aObject);
        void setEditingObject(EventSheet aSheet);
        void setEditingObject(Spritesheet aSheet);
        void setEditingObject(GameObjectInstance aInstance);
        void publishToGithub();
    }

    private TreeItemHTMLElement oldSelection;
    private final Map<Object, TreeItemHTMLElement> knownObjects;
    private final Window window;
    private final EditorState editorState;
    private final EventHandler eventHandler;
    private String currentScene;

    public GameTreeView(HTMLElement aHtmlElement, Window aWindow, EditorState aEditorState, EventHandler aEventHandler) {
        super(aHtmlElement);
        knownObjects = new HashMap<>();
        window = aWindow;
        editorState = aEditorState;
        eventHandler = aEventHandler;
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

    public void reloadSceneOnObjectDeletion(GameScene aScene, TreeItemHTMLElement aDeletedElement) {
        if (oldSelection == aDeletedElement) {
            oldSelection=null;
        }

        onGameSceneLoaded();

        if (oldSelection != null) {
            select(oldSelection);
        }
    }

    public void onGameLoaded() {
        clear();

        TreeItemHTMLElement theGameElement = addTreeItem(1);
        theGameElement.setSeparator(true);
        binder.add(theGameElement.bindTo(editorState.getLoadedGame().nameProperty()));
        theGameElement.addEventListener("click", evt -> {
            select(theGameElement);
            eventHandler.setEditingObject(editorState.getLoadedGame());
        });

        if (editorState.getEditorProject().isAuthorizedWith(editorState.getAuthorizationState())) {
            theGameElement.addContextMenuListener(aContextMenuEvent -> {
                ContextMenuHTMLElement theElement = ContextMenuHTMLElement.create();
                theElement.setTitle("Available Actions");

                ContextMenuItemHTMLElement theItem = ContextMenuItemHTMLElement.create();
                theItem.setText("Publish to GitHub");
                theItem.addEventListener("click", aClickEvent -> {
                    eventHandler.publishToGithub();
                });
                theElement.add(theItem);
                theElement.showAt(aContextMenuEvent);
            });
        }

        if (oldSelection != null) {
            select(oldSelection);
        }
    }

    public void onGameSceneLoaded() {

        onGameLoaded();

        for (String theSceneID : editorState.getLoadedScenes()) {
            GameScene theScene = editorState.getGameSceneById(theSceneID);

            TreeItemHTMLElement theGameElement = addTreeItem(1);
            theGameElement.setSeparator(true);
            binder.add(theGameElement.bindTo(theScene.nameProperty()));
            knownObjects.put(theScene, theGameElement);
            theGameElement.addEventListener("click", evt -> {
                select(theGameElement);
                currentScene = theSceneID;

                eventHandler.setEditingObject(theScene);

                onGameSceneLoaded();
            });

            if (currentScene != null && currentScene.equals(theSceneID)) {
                GlobalSeparatorHTMLElement theObjects = addTitleLevel2("Objects");
                theObjects.addContextMenuListener(aContextMenuEvent -> {
                    ContextMenuHTMLElement theElement = ContextMenuHTMLElement.create();
                    theElement.setTitle("Add new");

                    ContextMenuItemHTMLElement theItem = ContextMenuItemHTMLElement.create();
                    theItem.setText("Game object");
                    theItem.addEventListener("click", aClickEvent -> {
                        theElement.hide();
                        GameObject theObject = theScene.createNewGameObject("NEW OBJECT");

                        reloadSceneOnObjectDeletion(theScene, null);

                        setEditingObject(theObject);
                        eventHandler.setEditingObject(theObject);
                    });
                    theElement.add(theItem);
                    theElement.showAt(aContextMenuEvent);
                });
                for (GameObject theObject : theScene.getObjects()) {

                    TreeItemHTMLElement theElement = addTreeItem(1);
                    theElement.setDraggable(true);
                    binder.add(theElement.bindTo(theObject.nameProperty()));
                    knownObjects.put(theObject, theElement);
                    theElement.addEventListener("click", evt -> {
                        select(theElement);
                        eventHandler.setEditingObject(theObject);
                    });
                    theElement.addEventListener("dragstart", new EventListener<TeaVMDragEvent>() {
                        @Override
                        public void handleEvent(TeaVMDragEvent aEvent) {
                            aEvent.getDataTransfer().setData(Constants.DND_OBJECT_ID, theObject.uuidProperty().get());
                            window.getLocalStorage().setItem(Constants.DND_OBJECT_ID, theObject.uuidProperty().get());
                        }
                    });

                    theElement.addDeleteListener(evt -> {

                        theScene.removeGameObject(theObject);
                        reloadSceneOnObjectDeletion(theScene, theElement);

                        Toast.info("Object deleted.");
                    });
                }
                GlobalSeparatorHTMLElement theSpriteSheets = addTitleLevel2("Sprite sheets");
                theSpriteSheets.addContextMenuListener(aContextMenuEvent -> {
                    ContextMenuHTMLElement theElement = ContextMenuHTMLElement.create();
                    theElement.setTitle("Add new");

                    ContextMenuItemHTMLElement theItem = ContextMenuItemHTMLElement.create();
                    theItem.setText("Sprite sheet");
                    theItem.addEventListener("click", aClickEvent -> {
                        theElement.hide();
                        Spritesheet theObject = theScene.createNewSpriteSheet();
                        theObject.nameProperty().set("Sprite sheet #" + theScene.getSpriteSheets().length);

                        reloadSceneOnObjectDeletion(theScene, null);

                        setEditingObject(theObject);
                        eventHandler.setEditingObject(theObject);
                    });
                    theElement.add(theItem);
                    theElement.showAt(aContextMenuEvent);
                });
                for (Spritesheet theSheet : theScene.getSpriteSheets()) {
                    TreeItemHTMLElement theElement = addTreeItem(1);
                    knownObjects.put(theSheet, theElement);
                    binder.add(theElement.bindTo(theSheet.nameProperty()));

                    theElement.addEventListener("click", evt -> {
                        select(theElement);
                        eventHandler.setEditingObject(theSheet);
                    });
                    theElement.addDeleteListener(evt -> {
                        theScene.removeSpriteSheet(theSheet);

                        reloadSceneOnObjectDeletion(theScene, theElement);

                        Toast.info("Sprite sheet deleted.");
                    });
                }

                GlobalSeparatorHTMLElement theEventSheets = addTitleLevel2("Eventsheets");
                theEventSheets.addContextMenuListener(aContextMenuEvent -> {
                    ContextMenuHTMLElement theElement = ContextMenuHTMLElement.create();
                    theElement.setTitle("Add new");

                    ContextMenuItemHTMLElement theItem = ContextMenuItemHTMLElement.create();
                    theItem.setText("Event sheet");
                    theItem.addEventListener("click", aClickEvent -> {
                        theElement.hide();
                        EventSheet theObject = theScene.createNewEventSheet();
                        theObject.nameProperty().set("Event sheet #" + theScene.getEventSheets().length);

                        reloadSceneOnObjectDeletion(theScene, null);

                        setEditingObject(theObject);
                        eventHandler.setEditingObject(theObject);
                    });
                    theElement.add(theItem);
                    theElement.showAt(aContextMenuEvent);
                });
                for (EventSheet theSheet : theScene.getEventSheets()) {
                    TreeItemHTMLElement theElement = addTreeItem(1);
                    knownObjects.put(theSheet, theElement);
                    binder.add(theElement.bindTo(theSheet.nameProperty()));

                    theElement.addEventListener("click", evt -> {
                        select(theElement);
                        eventHandler.setEditingObject(theSheet);
                    });
                    theElement.addDeleteListener(evt -> {
                        theScene.removeEventSheet(theSheet);

                        reloadSceneOnObjectDeletion(theScene, theElement);

                        Toast.info("Event sheet deleted.");
                    });
                }
                addTitleLevel2("Instances");
                for (GameObjectInstance theInstance : theScene.getInstances()) {
                    TreeItemHTMLElement theElement = addTreeItem(1);
                    knownObjects.put(theInstance, theElement);
                    binder.add(theElement.bindTo(theInstance.nameProperty()));
                    theElement.addEventListener("click", evt -> {
                        select(theElement);
                        eventHandler.setEditingObject(theInstance);
                    });
                    theElement.addDeleteListener(evt -> {
                        theScene.removeGameObjectInstance(theInstance);

                        reloadSceneOnObjectDeletion(theScene, theElement);

                        Toast.info("Object instance deleted.");
                    });
                }
            }
        }

        if (oldSelection != null) {
            select(oldSelection);
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