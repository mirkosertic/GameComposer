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

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.Spritesheet;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.teavm.TeaVMGameRuntimeFactory;
import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import de.mirkosertic.gameengine.type.Script;
import de.mirkosertic.gameengine.web.electron.Dialog;
import de.mirkosertic.gameengine.web.electron.DialogOptions;
import de.mirkosertic.gameengine.web.electron.Electron;
import de.mirkosertic.gameengine.web.electron.LocalEditorProject;
import de.mirkosertic.gameengine.web.electron.LocalProjectDefinition;
import de.mirkosertic.gameengine.web.electron.LocalResourceAccessor;
import de.mirkosertic.gameengine.web.electron.Menu;
import de.mirkosertic.gameengine.web.electron.MenuItem;
import de.mirkosertic.gameengine.web.electron.Remote;
import de.mirkosertic.gameengine.web.electron.fs.FS;
import de.mirkosertic.gameengine.web.github.GithubAuthorizer;
import de.mirkosertic.gameengine.web.github.GithubResourceAccessor;

public class Editor {

    private static final Window window = Window.current();
    private static final HTMLDocument document = (HTMLDocument) window.getDocument();

    @JSBody(params = "aValue", script = "return (typeof aValue !== 'undefined');")
    public static native boolean isDefined(JSObject aValue);

    private TabbedPaneHTMLElement.Manager tabbedPageManager;
    private GameObjectEditor objectEditor;
    private EditorState editorState;
    private GameTreeView treeView;
    private final Router router;
    private final GithubAuthorizer authorizer;

    public Editor(Router aRouter, GithubAuthorizer aAuthorizer) {
        router = aRouter;
        authorizer = aAuthorizer;
        if (Electron.available()) {
            Electron theElectron = Electron.require();
            Remote theRemote = theElectron.getRemote();
            FS theFilesystem = Remote.require("fs");

            Menu theApplicationMenu = Menu.createMenu(theRemote);

            Menu theFileMenu = Menu.createMenu(theRemote);
            MenuItem theOpen = MenuItem.createMenuItem(theRemote, "Open", () -> {
                openLocalProject(theFilesystem, theRemote);
            });
            theFileMenu.append(theOpen);

            MenuItem theFileItem = MenuItem.createMenuItem(theRemote, "File", theFileMenu);
            theApplicationMenu.append(theFileItem);

            Menu.setApplicationMenu(theRemote, theApplicationMenu);
        }
    }

    public void boot(EditorProject aProject, ResourceAccessor aResourceAccessor) {
        if (aResourceAccessor instanceof GithubResourceAccessor) {
            authorizer.getAuthorizationState().thenContinue(aResult -> {
                boot(aProject, aResourceAccessor, aResult);
            });
        } else {
            boot(aProject, aResourceAccessor, AuthorizationState.NOT_LOGGED_IN());
        }
    }


    public void boot(EditorProject aProject, ResourceAccessor aResourceAccessor, AuthorizationState aAuthorizationState) {

        TeaVMGameRuntimeFactory theRuntimeFactory = new TeaVMGameRuntimeFactory(
                !window.getLocation().getFullURL().contains("nothreading"),
                window.getLocation().getFullURL().contains("profiling")) {

            @Override
            public void loadingFinished(GameScene aLoadesScene) {
                // No Action Manager
            }
        };

        TabbedPaneHTMLElement theTabbedPanne = (TabbedPaneHTMLElement) document.getElementById("editortabbedpane");

        tabbedPageManager = new TabbedPaneHTMLElement.Manager(theTabbedPanne);
        tabbedPageManager.clearAll();

        window.addEventListener("resize", evt -> tabbedPageManager.notifyResize(), true);

        editorState = new EditorState(aProject, theRuntimeFactory, aResourceAccessor, aAuthorizationState);

        // Initialize object editor
        HTMLElement thePropertyEditorElement = document.getElementById("objectEditor");
        HTMLElement theTreeElement = document.getElementById("objecttree");

        treeView = new GameTreeView(theTreeElement, window, editorState, new GameTreeView.EventHandler() {
            @Override
            public void setEditingObject(Game aGame) {

                TeaVMLogger.info("Editing game " + aGame.nameProperty().get());

                objectEditor.setEditingObject(aGame);
            }

            @Override
            public void setEditingObject(GameScene aScene) {

                TeaVMLogger.info("Editing scene " + aScene.nameProperty().get());

                objectEditor.setEditingObject(aScene);
                openSceneInEditor(aScene);
            }

            @Override
            public void setEditingObject(GameObject aObject) {
                objectEditor.setEditingObject(aObject);
            }

            @Override
            public void setEditingObject(EventSheet aSheet) {
                objectEditor.setEditingObject(aSheet);

                openEventSheetInEditor(aSheet);
            }

            @Override
            public void setEditingObject(Spritesheet aSheet) {
                objectEditor.setEditingObject(aSheet);
            }

            @Override
            public void setEditingObject(GameObjectInstance aInstance) {
                objectEditor.setEditingObject(aInstance);
            }

            @Override
            public void publishToGithub() {
                Toast.info("Pushing to Github, this may take while...");
                editorState.saveAll().thenContinue(aNothing -> {
                    GithubResourceAccessor theAccessor = (GithubResourceAccessor) aResourceAccessor;
                    theAccessor.publish(aAuthorizationState, "Updated by Web Editor").thenContinue(aResult -> {
                        Toast.info("New Commit " + aResult.getSha() + " created.");
                    });
                });
            }
        });

        objectEditor = new GameObjectEditor(thePropertyEditorElement);

        editorState.load(new EditorState.LoadingListener() {

            @Override
            public void onGameLoaded(EditorState aEditorState) {
                treeView.onGameLoaded();
            }

            @Override
            public void onSceneLoaded(EditorState aState, String aSceneID) {
                treeView.onGameSceneLoaded();
            }

            @Override
            public void onSceneLoadingError(EditorState aState, String aSceneID, Throwable aThrowable) {
                TeaVMLogger.error("Error loading scene " + aSceneID);
            }

            @Override
            public void onGameLoadingError(Throwable aThrowable) {
                TeaVMLogger.error("Error loading game " + aThrowable.getMessage());
            }
        });

        objectEditor.clear();
    }

    private void openSceneInEditor(GameScene aScene) {

        if (tabbedPageManager.ensureSelected(aScene)) {
            return;
        }

        SceneEditorHTMLElement theSceneEditor = SceneEditorHTMLElement.create();
        final GameSceneEditor theGameEditor = new GameSceneEditor(theSceneEditor, window, editorState, router) {

            @Override
            protected void setSelectedInstance(GameObjectInstance aInstance) {
                super.setSelectedInstance(aInstance);
                objectEditor.setEditingObject(aInstance);
                treeView.setEditingObject(aInstance);
            }
        };

        tabbedPageManager.addTab("Scene", new TabbedPaneHTMLElement.TabHandler() {
            @Override
            public HTMLElement getElement() {
                return theSceneEditor;
            }

            @Override
            public Object getOwner() {
                return aScene;
            }

            @Override
            public void handleClosed() {
                theGameEditor.shutdownRunningGameLoop();
            }

            @Override
            public void handleResize() {
                theGameEditor.handleResize();
            }
        });

        theGameEditor.playScene(aScene);
    }

    private void openEventSheetInEditor(EventSheet aEventSheet) {

        if (tabbedPageManager.ensureSelected(aEventSheet)){
            return;
        }

        EventsheetEditorHTMLElement theEventsheetEditor = EventsheetEditorHTMLElement.create();
        theEventsheetEditor.bindTo(aEventSheet, this);
        tabbedPageManager.addTab("Event sheet", new TabbedPaneHTMLElement.TabHandler() {
            @Override
            public HTMLElement getElement() {
                return theEventsheetEditor;
            }

            @Override
            public Object getOwner() {
                return aEventSheet;
            }

            @Override
            public void handleClosed() {
            }

            @Override
            public void handleResize() {
            }
        });
    }

    private void openLocalProject(FS aFilesystem, Remote aRemote) {
        DialogOptions theOptions = DialogOptions.create();
        theOptions.setTitle("Select a file!");
        theOptions.setProperties(new String[] {"openDirectory" });

        // http://stackoverflow.com/questions/20619488/how-to-convert-local-file-path-to-a-file-url-safely-in-node-js
        Dialog theDialog = aRemote.getDialog();

        JSObject aPaths = theDialog.showOpenDialog(theOptions);
        if (isDefined(aPaths) && aPaths != null) {
            JSArray<JSString> theResults = (JSArray<JSString>) aPaths;
            if (theResults.getLength() == 1) {
                String thePath = theResults.get(0).stringValue();
                boot(new LocalEditorProject(aFilesystem, LocalProjectDefinition.create(thePath)), new LocalResourceAccessor(aFilesystem, thePath));
            }
        }
    }

    public void editLUAScriptProperty(Property<Script> aScriptProperty) {
        Script theScript = aScriptProperty.get();

        AceEditorHTMLElement theEditor = AceEditorHTMLElement.create();
        theEditor.initWithScript(theScript);

        tabbedPageManager.addTab("LUA Script", new TabbedPaneHTMLElement.TabHandler() {
            @Override
            public HTMLElement getElement() {
                return theEditor;
            }

            @Override
            public Object getOwner() {
                return aScriptProperty;
            }

            @Override
            public void handleClosed() {
            }

            @Override
            public void handleResize() {
            }
        });
        theEditor.addEventListener("change", evt1 -> aScriptProperty.set(new Script(theEditor.getValue())));
    }
}