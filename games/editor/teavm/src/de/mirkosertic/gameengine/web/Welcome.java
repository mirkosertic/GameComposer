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
import de.mirkosertic.gameengine.web.github.GithubRepository;
import de.mirkosertic.gameengine.web.html5.FileReader;
import de.mirkosertic.gameengine.web.indexeddb.IndexedDBFilesystem;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.html.HTMLOptionElement;
import org.teavm.jso.dom.html.HTMLSelectElement;
import org.teavm.jso.json.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Welcome {

    private static final Window WINDOW = Window.current();

    private final Router router;
    private final GithubAuthorizer authorizer;

    public Welcome(Router aRouter, GithubAuthorizer aAuthorizer) {
        router = aRouter;
        authorizer = aAuthorizer;
    }

    public Promise<List<ProjectDefinition>, String> computeStoredDefinitions() {
        return new Promise<>((aResolver, aRejector) -> {
            Promise<String[], String> theAllDatabases = IndexedDBFilesystem.listDatabases();
            theAllDatabases.catchError((aResult, aOptionalException) -> TeaVMLogger.info("Error getting databases : " + aResult));
            theAllDatabases.thenContinue(aResult -> {
                Map<String, Promise<ProjectDefinition, String>> theRetriever = new HashMap<>();
                for (String theDatabaseName : aResult) {
                    theRetriever.put(theDatabaseName, new Promise<>((aResolver1, aRejector1) -> {
                        Promise<IndexedDBFilesystem, String> theFilesystem = IndexedDBFilesystem.open(theDatabaseName);
                        theFilesystem.thenContinue(aResult1 -> {
                            aResult1.openFile(EditorProject.DEFINITION_FILENAME).thenContinue(aResult11 -> {
                                FileReader theReader = FileReader.create();
                                theReader.setOnload(() -> {
                                    ProjectDefinition theDefinition = (ProjectDefinition) JSON.parse(theReader.getResult());
                                    aResolver1.resolve(theDefinition);
                                });
                                theReader.readAsText(aResult11.getContent());
                            });
                        });
                    }));
                }
                return theRetriever;
            }).thenContinue(aResult -> {
                Promise.all(new ArrayList<>(aResult.values())).thenContinue(aResult12 -> {
                    List<ProjectDefinition> theDefinition = new ArrayList<>();
                    for (Promise thePromise : aResult12) {
                        theDefinition.add((ProjectDefinition) thePromise.getResolvedValue());
                    }
                    aResolver.resolve(theDefinition);
                });
            });
        });
    }

    public void run() {

        Promise<AuthorizationState, String> thePromise = authorizer.getAuthorizationState();
        thePromise.thenContinue(aAuthorizationState -> {
            HTMLElement theParentElement = WINDOW.getDocument().getElementById("infoblockcontent");

            if (!aAuthorizationState.isNotLoggedIn()) {
                authorizer.getRepositories().thenContinue(aResult -> {
                    final HTMLSelectElement theSelect = (HTMLSelectElement) WINDOW.getDocument().getElementById("githubrepositories");
                    for (GithubRepository theRepository : aResult) {
                        HTMLOptionElement theOption = (HTMLOptionElement) WINDOW.getDocument().createElement("option");
                        theOption.setValue(theRepository.getName());
                        theOption.setText(theRepository.getFull_name());
                        theSelect.appendChild(theOption);
                    }

                    HTMLInputElement thePathElement = (HTMLInputElement) WINDOW.getDocument().getElementById("githubrepositorypath");
                    HTMLElement theClicker = WINDOW.getDocument().getElementById("opengithubrepository");
                    theClicker.addEventListener("click", aEvent -> {
                        String theRepository = theSelect.getValue();
                        String theUser = aAuthorizationState.getLogin();
                        String thePath = thePathElement.getValue();
                        router.openWithState("editor.html", GithubProjectDefinition.create(theUser, theRepository, thePath));
                    });
                });
            }

            computeStoredDefinitions().thenContinue(aResult -> {
                addTo(aResult, GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/platformer"));
                addTo(aResult, GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/bounce"));
                addTo(aResult, GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/camera"));
                addTo(aResult, GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/gravity"));
                addTo(aResult, GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/networking"));
                addTo(aResult, GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/positiontest"));
                addTo(aResult, GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/rotatingactor"));
                addTo(aResult, GithubProjectDefinition.create("mirkosertic", "GameComposer", "/examples/arcaderacer"));

                for (ProjectDefinition theDefinition : aResult) {
                    HTMLElement theElement = WINDOW.getDocument().createElement("div");
                    theElement.setInnerHTML(generateStringRepresentation(theDefinition));
                    switch (theDefinition.getType()) {
                        case GithubProjectDefinition.TYPE:
                            theElement.setAttribute("class", "project github");
                            break;
                        case LocalProjectDefinition.TYPE:
                            theElement.setAttribute("class", "project local");
                            break;
                    }

                    theElement.addEventListener("click", evt -> router.openWithState("editor.html", theDefinition));

                    theParentElement.appendChild(theElement);
                }
            });

            HTMLElement theWelcomeMessage = WINDOW.getDocument().getElementById("welcomemessage");

            HTMLElement theGithubLogin = WINDOW.getDocument().getElementById("githublogin");
            HTMLElement theGithubRepositories = WINDOW.getDocument().getElementById("githubrepositoryselection");
            if (aAuthorizationState.isNotLoggedIn()) {
                theGithubRepositories.getStyle().setProperty("display", "none");
                theWelcomeMessage.setInnerHTML("Welcome to Online Game Composer!");
                theGithubLogin.addEventListener("click", evt -> {
                    String theState = "" + (int) (Math.random() * 1000000);
                    authorizer.requestOAuthLogin(theState);
                });
            } else {
                theWelcomeMessage.setInnerHTML("Welcome to Online Game Composer, " + aAuthorizationState.getRealName() + "!");
                theGithubLogin.getStyle().setProperty("display", "none");
                theGithubRepositories.getStyle().setProperty("display", "visible");
            }
        });
    }

    private boolean matches(ProjectDefinition a1, ProjectDefinition a2) {
        if (!a1.getType().equals(a2.getType())) {
            return false;
        }
        switch (a1.getType()) {
            case GithubProjectDefinition.TYPE: {
                GithubProjectDefinition theDefinition = (GithubProjectDefinition) a1;
                GithubProjectDefinition theCurrentDefinition = (GithubProjectDefinition) a2;
                return theCurrentDefinition.getUser().equals(theDefinition.getUser()) &&
                        theCurrentDefinition.getRepository().equals(theDefinition.getRepository()) &&
                        theCurrentDefinition.getRelativePath().equals(theDefinition.getRelativePath());
            }
            case LocalProjectDefinition.TYPE: {
                LocalProjectDefinition theDefinition = (LocalProjectDefinition) a1;
                LocalProjectDefinition theCurrentDefinition = (LocalProjectDefinition) a2;
                return theCurrentDefinition.getPath().equals(theDefinition.getPath());
            }
        }
        throw new IllegalStateException("Nicht unterstützter Typ : " + a1.getType());
    }

    private void addTo(List<ProjectDefinition> aList, ProjectDefinition aDefinition) {
        for (ProjectDefinition theDefinition : aList) {
            if (matches(aDefinition, theDefinition)) {
                return;
            }
        }
        aList.add(aDefinition);
    }

    private String generateStringRepresentation(ProjectDefinition aDefinition) {
        switch (aDefinition.getType()) {
            case GithubProjectDefinition.TYPE:
                GithubProjectDefinition theGithubProject = (GithubProjectDefinition) aDefinition;
                return theGithubProject.getUser()+"/" + theGithubProject.getRepository() + " " + theGithubProject.getRelativePath();
            case LocalProjectDefinition.TYPE:
                LocalProjectDefinition theLocalDefinition = (LocalProjectDefinition) aDefinition;
                return "Local directory : " + theLocalDefinition.getPath();
        }
        throw new IllegalStateException();
    }
}