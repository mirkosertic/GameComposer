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

import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.json.JSON;

public class Github {

    @JSBody(params = {"aString"}, script = "return aString;")
    public static native String btoa(String aString);

    public interface ResponseHandler {
        void handle(JSObject aObject, int aReturnCode, String aETag);
    }

    private final AuthenticationOptions authenticationOptions;

    public Github(AuthenticationOptions aOptions) {
        authenticationOptions = aOptions;
    }

    public Repository getRepo(String aUser, String aName) {
        return new Repository(this, aUser, aName);
    }

    void open(String aPath, ResponseHandler aHandler, boolean aPrefixWithAPI, String aETag) {
        final XMLHttpRequest theRequest = XMLHttpRequest.create();
        if (aETag != null) {
            theRequest.setRequestHeader("If-None-Match", aETag);
        }
        theRequest.onComplete(() -> aHandler.handle(JSON.parse(theRequest.getResponseText()), theRequest.getStatus(), theRequest.getResponseHeader("ETag")));
        if (authenticationOptions.getUsername() != null) {
            TeaVMLogger.info("Requesting " + aPath + " authorized with " + aPrefixWithAPI);
            theRequest.open("GET", (aPrefixWithAPI ? "https://api.github.com" : "") + aPath, true, authenticationOptions.getUsername(), authenticationOptions.getPassword());
        } else {
            TeaVMLogger.info("Requesting " + aPath + " unauthorized with " + aPrefixWithAPI);
            theRequest.open("GET", (aPrefixWithAPI ? "https://api.github.com" : "") + aPath, true);
        }
        theRequest.send();
    }
}