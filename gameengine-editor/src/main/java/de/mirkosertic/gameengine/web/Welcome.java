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

import de.mirkosertic.gameengine.web.electron.LocalProjectDefinition;
import de.mirkosertic.gameengine.web.github.GithubProjectDefinition;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.ArrayList;
import java.util.List;

public class Welcome {

    private static final Window WINDOW = Window.current();
    private static final String OAUTHSTATE = "OAUTHSTATE";

    private final Router router;

    public Welcome(Router aRouter) {
        router = aRouter;
    }

    public void run() {

        String theQuery = WINDOW.getLocation().getFullURL();
        int p = theQuery.lastIndexOf("?");
        if (p>0) {
            String theQueryString = theQuery.substring(p+1);

            QueryStringParser theParser = new QueryStringParser(theQueryString);
            if (theParser.contains("code")) {
                XMLHttpRequest theRequest = XMLHttpRequest.create();

                String theOAuthParams = "client_id=" + determineGithubClientID() + "&code=" + theParser.getValue("code") + "&state=" + WINDOW.getSessionStorage().getItem(OAUTHSTATE);
                String theURL = "http://www.mirkosertic.de/githubtokenexchange.php";

                // Here we get something like "access_token=tokendata&scope=repo&token_type=bearer"

                theRequest.open("POST", theURL);
                theRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                theRequest.overrideMimeType("text/plain");
                theRequest.onComplete(() -> {
                    QueryStringParser theResult = new QueryStringParser(theRequest.getResponseText());

                    WINDOW.getHistory().replaceState(null, WINDOW.getDocument().getHead().getTitle(), "index.html");

                    Window.alert(theResult.getValue("token_type") + " " + theResult.getValue("access_token"));
                });
                theRequest.send(theOAuthParams);

            }
        }

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

        for (ProjectDefinition theDefinition : theProjectDefinitions) {
            HTMLElement theElement = WINDOW.getDocument().createElement("div");
            theElement.setInnerHTML(generateStringRepresentation(theDefinition));

            theElement.addEventListener("click", evt -> router.openWithState("editor.html", theDefinition));

            theParentElement.appendChild(theElement);
        }

        HTMLElement theGithubLogin = WINDOW.getDocument().getElementById("githublogin");
        theGithubLogin.addEventListener("click", evt -> {

            String theState = "" + (int) (Math.random() * 1000000);
            WINDOW.getSessionStorage().setItem(OAUTHSTATE, theState);
            String theAutorizationURL = "https://github.com/login/oauth/authorize?state = " + theState + "&scope=repo&client_id=" + determineGithubClientID();
            WINDOW.open(theAutorizationURL, "_self");
        });
    }

    private String determineGithubClientID() {
        if (WINDOW.getLocation().getFullURL().startsWith("http://localhost")) {
            // Dev environment
            return "8bffca80d0a9e4146b90";
        }
        // Prod environment
        return "cb7f66b0ccea83f17c68";
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