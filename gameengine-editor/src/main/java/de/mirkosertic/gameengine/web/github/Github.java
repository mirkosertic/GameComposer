package de.mirkosertic.gameengine.web.github;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public abstract class Github implements JSObject {

    @JSBody(params = {"aOptions"}, script = "new GitHub(aOptions);")
    public static native Github create(AuthenticationOptions aOptions);

    public abstract User getUser();
}
