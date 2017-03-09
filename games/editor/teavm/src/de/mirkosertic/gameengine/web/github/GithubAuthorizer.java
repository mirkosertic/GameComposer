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
package de.mirkosertic.gameengine.web.github;

import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.web.AuthorizationState;
import de.mirkosertic.gameengine.web.QueryStringParser;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.json.JSON;

import java.util.ArrayList;
import java.util.List;

public class GithubAuthorizer {

    private static final String AUTHORIZATIONSTATE = "AUTHORIZATIONSTATE";
    private static final String OAUTHSTATE = "OAUTHSTATE";
    private static final Window WINDOW = Window.current();

    public Promise<List<GithubRepository>, String> getRepositories() {
        return new Promise<>((aResolver, aRejector) -> {

            String theCurrentState = WINDOW.getLocalStorage().getItem(AUTHORIZATIONSTATE);
            AuthorizationState theState = (AuthorizationState) JSON.parse(theCurrentState);

            XMLHttpRequest theRepositoriesRequest = XMLHttpRequest.create();
            theRepositoriesRequest.open("GET", "https://api.github.com/users/" + theState.getLogin() + "/repos");
            theRepositoriesRequest.setRequestHeader("Authorization", "token " + theState.getToken());
            theRepositoriesRequest.overrideMimeType("text/plain");
            theRepositoriesRequest.onComplete(() -> {
                JSArray<GithubRepository> theArray = (JSArray<GithubRepository>)JSON.parse(theRepositoriesRequest.getResponseText());
                List<GithubRepository> theRepos = new ArrayList<>();
                for (int i=0;i<theArray.getLength();i++) {
                    theRepos.add(theArray.get(i));
                }
                aResolver.resolve(theRepos);
            });
            theRepositoriesRequest.send();
        });
    }

    public Promise<AuthorizationState, String> getAuthorizationState() {
        return new Promise<>((aResolver, aRejector) -> {
            String theCurrentUser = WINDOW.getLocalStorage().getItem(AUTHORIZATIONSTATE);
            if (theCurrentUser != null) {
                AuthorizationState theDetails = (AuthorizationState) JSON.parse(theCurrentUser);
                aResolver.resolve(theDetails);
            } else {
                String theQuery = WINDOW.getLocation().getFullURL();
                int p = theQuery.lastIndexOf("?");
                if (p>0) {
                    String theQueryString = theQuery.substring(p+1);
                    QueryStringParser theParser = new QueryStringParser(theQueryString);
                    if (theParser.contains("code")) {
                        // OAuth Callback
                        XMLHttpRequest theRequest = XMLHttpRequest.create();

                        String theOAuthParams = "client_id=" + determineGithubClientID() + "&code=" + theParser.getValue("code") + "&state=" + WINDOW.getSessionStorage().getItem(OAUTHSTATE);
                        String theURL = "https://tools.mirkosertic.de/githubtokenexchange.php";

                        theRequest.open("POST", theURL);
                        theRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                        theRequest.overrideMimeType("text/plain");
                        theRequest.onComplete(() -> {
                            QueryStringParser theResult = new QueryStringParser(theRequest.getResponseText());

                            String theAccessToken = theResult.getValue("access_token");

                            XMLHttpRequest theUserDetailsRequest = XMLHttpRequest.create();
                            theUserDetailsRequest.open("GET", "https://api.github.com/user");
                            theUserDetailsRequest.setRequestHeader("Authorization", "token " + theAccessToken);
                            theUserDetailsRequest.overrideMimeType("text/plain");
                            theUserDetailsRequest.onComplete(() -> {

                                WINDOW.getHistory().replaceState(null, WINDOW.getDocument().getHead().getTitle(), "index.html");

                                GithubUser theGithubUser = (GithubUser) JSON.parse(theUserDetailsRequest.getResponseText());

                                AuthorizationState theDetails = AuthorizationState.githubUser(theGithubUser.getLogin(), theGithubUser.getName(), theAccessToken);
                                WINDOW.getLocalStorage().setItem(AUTHORIZATIONSTATE, JSON.stringify(theDetails));
                                aResolver.resolve(theDetails);
                            });
                            theUserDetailsRequest.send();
                        });
                        theRequest.send(theOAuthParams);
                    } else {
                        // Unknown user
                        aResolver.resolve(AuthorizationState.NOT_LOGGED_IN());
                    }
                } else {
                    // Unknown user
                    aResolver.resolve(AuthorizationState.NOT_LOGGED_IN());
                }
            }
        });
    }

    public void requestOAuthLogin(String aState) {
        String theAutorizationURL = "https://github.com/login/oauth/authorize?state = " + aState + "&scope=repo&client_id=" + determineGithubClientID();
        WINDOW.getSessionStorage().setItem(OAUTHSTATE, aState);
        WINDOW.open(theAutorizationURL, "_self");
    }

    private String determineGithubClientID() {
        if (WINDOW.getLocation().getFullURL().startsWith("http://localhost")) {
            // Dev environment
            return "8bffca80d0a9e4146b90";
        }
        // Prod environment
        return "cb7f66b0ccea83f17c68";
    }
}