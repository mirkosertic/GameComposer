package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.*;

public class CameraComponentFactory implements GameComponentFactory<CameraComponent> {

    public CameraComponentFactory() {
    }

    public CameraComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        final CameraComponent theResult = new CameraComponent(aInstance, aGameRuntime);
        aGameRuntime.getEventManager().register(aInstance, SetScreenResolutionEvent.class, new GameEventListener<SetScreenResolutionEvent>() {
            public void handleGameEvent(SetScreenResolutionEvent aEvent) {
                theResult.setScreenResolution(aEvent.getScreenWidth(), aEvent.getScreenHeight());
            }
        });
        return theResult;
    }
}