package de.mirkosertic.gameengine.web.github;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;

public interface User extends JSObject {

    void listRepos(Callback<JSArray<Repository>> aCallback);
}
