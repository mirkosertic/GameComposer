package de.mirkosertic.gameengine.web.github;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

@JSFunctor
public interface Callback<T> extends JSObject {

    void handle(Boolean aError, T aResult);
}
