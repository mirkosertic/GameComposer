package de.mirkosertic.gameengine.web.github;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface Commit extends JSObject {

    @JSProperty
    String getSha();
}
