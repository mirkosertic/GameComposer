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

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameRuntimeFactory;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGenericPlayer;
import de.mirkosertic.gameengine.web.electron.Dialog;
import de.mirkosertic.gameengine.web.electron.DialogOptions;
import de.mirkosertic.gameengine.web.electron.Electron;
import de.mirkosertic.gameengine.web.electron.Menu;
import de.mirkosertic.gameengine.web.electron.MenuItem;
import de.mirkosertic.gameengine.web.electron.Remote;
import de.mirkosertic.gameengine.web.electron.fs.FS;
import de.mirkosertic.gameengine.web.github.GithubEditorProject;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;

public class WebUI {

    private static final Window window = Window.current();

    @JSBody(params = "aValue", script = "return (typeof aValue !== 'undefined');")
    public static native boolean isDefined(JSObject aValue);

    public static void main(String[] args) {

        if (Electron.available()) {
            Electron theElectron = Electron.require();
            Remote theRemote = theElectron.getRemote();
            FS theFilesystem = theRemote.require("fs");

            Menu theApplicationMenu = Menu.createMenu(theRemote);

            Menu theFileMenu = Menu.createMenu(theRemote);
            MenuItem theOpen = MenuItem.createMenuItem(theRemote, "Open", new MenuItem.ActionListener() {
                @Override
                public void run() {
                    window.alert("Clicked!!!");
                }
            });
            theFileMenu.append(theOpen);

            MenuItem theFileItem = MenuItem.createMenuItem(theRemote, "File", theFileMenu);
            theApplicationMenu.append(theFileItem);

            //Menu.setApplicationMenu(theRemote, theApplicationMenu);

            DialogOptions theOptions = DialogOptions.create();
            theOptions.setTitle("Select a file!");
            theOptions.setProperties(new String[] {"openFile", "openDirectory"});

            // http://stackoverflow.com/questions/20619488/how-to-convert-local-file-path-to-a-file-url-safely-in-node-js
            Dialog theDialog = theRemote.getDialog();

/*            JSObject aPaths = theDialog.showOpenDialog(theOptions);
            if (isDefined(aPaths) && aPaths!= null) {
                String[] theResults = (String[]) (Object) aPaths;
                if (theResults.length == 1) {
                    Stats theStats = theFilesystem.statSync(theResults[0]);
                    if (theStats != null) {
                        window.alert(
                                theResults[0] + " isFile : " + theStats.isFile() + " isDirectory : " + theStats.isDirectory());
                    } else {
                        window.alert("no fsstats for " + theResults[0]);
                    }
                } else {
                    window.alert(theResults.length + "Files selected");
                }
            } else {
                window.alert("No selected");
            }*/
        }

        EditorProject theProject = new GithubEditorProject("mirkosertic", "GameComposer", "/examples/platformer");

        if (window.getLocation().getPathName().endsWith("/index.html")) {

            Editor theEditor = new Editor();
            theEditor.boot(theProject);

        } else {
            HTMLCanvasElement theCanvasElement = (HTMLCanvasElement) window.getDocument().getElementById("html5canvas");
            TeaVMGenericPlayer thePlayer = new TeaVMGenericPlayer() {
                @Override
                protected TeaVMGameSceneLoader createSceneLoader(TeaVMGameRuntimeFactory aRuntimeFactory) {

                    String thePreviewData = theProject.getPreviewDataAsJSON();
                    if (thePreviewData != null) {
                        return new TeaVMGameSceneLoader(createSceneLoaderListener(), aRuntimeFactory) {
                            @Override
                            public void loadFromServer(Game aGame, String aSceneName, TeaVMGameResourceLoader aResourceLoader) {
                                String thePreviewData = window.getLocalStorage().getItem("previewscene");
                                listener.onGameSceneLoaded(parse(aGame, thePreviewData, aResourceLoader));
                            }
                        };
                    }
                    return theProject.createSceneLoader(createSceneLoaderListener(), aRuntimeFactory);
                }

                @Override
                protected TeaVMGameLoader createGameLoader(TeaVMGameLoader.GameLoadedListener aListener) {
                    return theProject.createGameLoader(aListener);
                }

                @Override
                protected TeaVMGameResourceLoader createResourceLoader(String aSceneID) {
                    return theProject.createResourceLoaderFor(aSceneID);
                }

                @Override
                protected void loadOtherSceneFromWithinGame(Game aGame, String aSceneID) {
                }
            };
            thePlayer.boot(theCanvasElement);
        }
    }
}