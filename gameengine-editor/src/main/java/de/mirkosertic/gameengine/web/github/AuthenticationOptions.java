package de.mirkosertic.gameengine.web.github;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.JSObject;

public abstract class AuthenticationOptions implements JSObject {

    @JSBody(params = {}, script = "return {};")
    public native static AuthenticationOptions create();

    @JSProperty
    public abstract void setUsername(String aUsername);

    @JSProperty
    public abstract void setPassword(String aPassword);
}
