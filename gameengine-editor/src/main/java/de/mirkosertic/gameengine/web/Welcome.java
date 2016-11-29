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

import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import de.mirkosertic.gameengine.web.electron.LocalProjectDefinition;
import de.mirkosertic.gameengine.web.github.GithubAuthorizer;
import de.mirkosertic.gameengine.web.github.GithubProjectDefinition;
import de.mirkosertic.gameengine.web.html5.FileReader;
import de.mirkosertic.gameengine.web.indexeddb.IndexedDBFilesystem;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.json.JSON;

import java.util.ArrayList;
import java.util.List;

public class Welcome {

    private static final Window WINDOW = Window.current();

    private final Router router;

    public Welcome(Router aRouter) {
        router = aRouter;
    }

    public void run() {

        GithubAuthorizer theAuthorizer = new GithubAuthorizer();
        Promise<AuthorizationState, String> thePromise = theAuthorizer.getAuthorizationState();

        thePromise.thenContinue(aAuthorizationState -> {
            HTMLElement theParentElement = WINDOW.getDocument().getElementById("infoblockcontent");

            List<ProjectDefinition> theProjectDefinitions = new ArrayList<>();
            theProjectDefinitions.add(GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/platformer"));
            theProjectDefinitions.add(GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/bounce"));
            theProjectDefinitions.add(GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/camera"));
            theProjectDefinitions.add(GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/gravity"));
            theProjectDefinitions.add(GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/networking"));
            theProjectDefinitions.add(GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/positiontest"));
            theProjectDefinitions.add(GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/rotatingactor"));
            theProjectDefinitions.add(GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/arcaderacer"));

            Promise<String[], String> theAllDatabases = IndexedDBFilesystem.listDatabases();
            theAllDatabases.catchError((aResult, aOptionalException) -> TeaVMLogger.info("Error getting databases : " + aResult));
            theAllDatabases.thenContinue(aResult -> {
                for (String theDatabaseName : aResult) {
                    Promise<IndexedDBFilesystem, String> theFilesystem = IndexedDBFilesystem.open(theDatabaseName);
                    theFilesystem.thenContinue(aResult1 -> {
                        aResult1.openFile(EditorProject.DEFINITION_FILENAME).thenContinue(aResult11 -> {
                            FileReader theReader = FileReader.create();
                            theReader.setOnload(() -> {
                                ProjectDefinition theDefinition = (ProjectDefinition) JSON.parse(theReader.getResult());
                                TeaVMLogger.info("Project " + theDatabaseName + " is " + generateStringRepresentation(theDefinition));
                            });
                            theReader.readAsText(aResult11.getContent());
                        });
                    });
                }
            });

            for (ProjectDefinition theDefinition : theProjectDefinitions) {
                HTMLElement theElement = WINDOW.getDocument().createElement("div");
                theElement.setInnerHTML(generateStringRepresentation(theDefinition));

                theElement.addEventListener("click", evt -> router.openWithState("editor.html", theDefinition));

                theParentElement.appendChild(theElement);
            }

            HTMLElement theWelcomeMessage = WINDOW.getDocument().getElementById("welcomemessage");

            HTMLElement theGithubLogin = WINDOW.getDocument().getElementById("githublogin");
            if (aAuthorizationState.isNotLoggedIn()) {
                theWelcomeMessage.setInnerHTML("Welcome to Online Game Composer!");
                theGithubLogin.addEventListener("click", evt -> {
                    String theState = "" + (int) (Math.random() * 1000000);
                    theAuthorizer.requestOAuthLogin(theState);
                });
            } else {
                theWelcomeMessage.setInnerHTML("Welcome to Online Game Composer, " + aAuthorizationState.getRealName() + "!");
                theGithubLogin.getStyle().setProperty("display", "none");
            }
        });
    }

    private String generateStringRepresentation(ProjectDefinition aDefinition) {
        switch (aDefinition.getType()) {
            case GithubProjectDefinition.TYPE:
                GithubProjectDefinition theGithubProject = (GithubProjectDefinition) aDefinition;
                return theGithubProject.getUser()+"@" + theGithubProject.getRepository() + ":" + theGithubProject.getRelativePath();
            case LocalProjectDefinition.TYPE:
                LocalProjectDefinition theLocalDefinition = (LocalProjectDefinition) aDefinition;
                return "Local directory : " + theLocalDefinition.getPath();
        }
        throw new IllegalStateException();
    }
}