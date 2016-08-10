package de.mirkosertic.gameengine.web.github;

import org.teavm.jso.JSObject;

public interface User extends JSObject {

    void listRepos(Callback<Repository[]> aCallback);
}
