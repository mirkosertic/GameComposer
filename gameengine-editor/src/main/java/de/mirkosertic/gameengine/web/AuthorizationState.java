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
import org.teavm.jso.JSProperty;

public abstract class AuthorizationState implements JSObject {

    @JSBody(params = {}, script = "return {id: 'NOT_LOGGED_IN'}")
    public static native AuthorizationState NOT_LOGGED_IN();

    @JSBody(params = {"aLogin", "aRealName", "aToken"}, script = "return {id: 'GITHUB',login: aLogin, realName: aRealName, token: aToken}")
    public static native AuthorizationState githubUser(String aLogin, String aRealName, String aToken);

    @JSProperty
    public abstract String getId();

    @JSProperty
    public abstract String getLogin();

    @JSProperty
    public abstract String getRealName();

    @JSProperty
    public abstract String getToken();

    public boolean isNotLoggedIn() {
        return "NOT_LOGGED_IN".equals(getId());
    }
}