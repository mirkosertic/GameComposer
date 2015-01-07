package de.mirkosertic.gameengine.gwt;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.SystemException;
import de.mirkosertic.gameengine.sound.GameSoundSystemFactory;
import de.mirkosertic.gameengine.type.Reflectable;

import com.google.gwt.core.client.GWT;

class GWTGameRuntimeFactory extends AbstractGameRuntimeFactory {

    @Override
    protected Reflectable createBuildInFunctions() {
        return new GWTBuiltInFunctions();
    }

    @Override
    public GameRuntime create(GameResourceLoader aResourceLoader, GameSoundSystemFactory aSoundSystemFactory) {
        GameRuntime theRuntime = super.create(aResourceLoader, aSoundSystemFactory);
        theRuntime.getEventManager().register(null, SystemException.class, new GameEventListener<SystemException>() {
            @Override
            public void handleGameEvent(SystemException aEvent) {
                GWT.log("System exception : " + aEvent.exception.getMessage(), aEvent.exception);
            }
        });
        return theRuntime;
    }
}