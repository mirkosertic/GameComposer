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

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.web.electron.Dialog;
import de.mirkosertic.gameengine.web.electron.DialogOptions;
import de.mirkosertic.gameengine.web.electron.Electron;
import de.mirkosertic.gameengine.web.electron.LocalEditorProject;
import de.mirkosertic.gameengine.web.electron.Menu;
import de.mirkosertic.gameengine.web.electron.MenuItem;
import de.mirkosertic.gameengine.web.electron.Remote;
import de.mirkosertic.gameengine.web.electron.fs.FS;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLElement;

public class Editor {

    private static final Window window = Window.current();
    private static final HTML5Document document = (HTML5Document) window.getDocument();

    @JSBody(params = "aValue", script = "return (typeof aValue !== 'undefined');")
    public static native boolean isDefined(JSObject aValue);

    public Editor() {
        if (Electron.available()) {
            Electron theElectron = Electron.require();
            Remote theRemote = theElectron.getRemote();
            FS theFilesystem = theRemote.require("fs");

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

    public void boot(EditorProject aProject) {

        SceneEditorHTMLElement theSceneEditor = SceneEditorHTMLElement.create();

        // Initialize object editor
        TabbedPaneHTMLElement theTabbedPanne = (TabbedPaneHTMLElement) document.getElementById("editortabbedpane");
        TabbedPaneHTMLElement.Manager theManager = new TabbedPaneHTMLElement.Manager(theTabbedPanne);
        theManager.clearAll();
        theManager.addTab("Editor", theSceneEditor, "scene");

        HTMLElement thePropertyEditorElement = (HTMLElement) document.getElementById("objectEditor");
        HTMLElement theTreeElement = (HTMLElement) document.getElementById("objecttree");
        GameObjectEditor theObjectEditor = new GameObjectEditor(thePropertyEditorElement, theManager);
        GameTreeView theTreeView = new GameTreeView(theTreeElement, theObjectEditor, window);

        GameEditor theGameEditor = new GameEditor(theSceneEditor, window, aProject) {

            @Override
            protected void playScene(GameScene aGameScene) {
                theTreeView.onGameSceneLoaded(aGameScene);
                super.playScene(aGameScene);
            }

            @Override
            protected void setSelectedInstance(GameObjectInstance aInstance) {
                super.setSelectedInstance(aInstance);
                theObjectEditor.setEditingObject(aInstance);
                theTreeView.setEditingObject(aInstance);
            }
        };

        window.addEventListener("resize", evt -> theGameEditor.handleResize(), true);

        theObjectEditor.clear();
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
                boot(new LocalEditorProject(aFilesystem, thePath));
            }
        }
    }
}
