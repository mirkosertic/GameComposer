package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.teavm.TeaVMGameView;
import de.mirkosertic.gameengine.teavm.pixi.Renderer;

public class GameEditorGameView extends TeaVMGameView {

    public GameEditorGameView(GameRuntime aGameRuntime,
            CameraBehavior aCameraBehavior,
            GestureDetector aGestureDetector,
            Renderer aRenderer) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector, aRenderer);
    }

    @Override
    protected boolean includeInRendering(GameObjectInstance aInstance) {
        return true;
    }
}
