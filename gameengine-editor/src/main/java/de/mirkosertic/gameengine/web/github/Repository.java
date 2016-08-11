package de.mirkosertic.gameengine.web.github;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface Repository extends JSObject {

    @JSProperty
    long getId();

    @JSProperty
    String getName();

    @JSProperty
    String getFull_name();

    void getCommit(String aSHA, Callback<Commit> aCallback);
}
