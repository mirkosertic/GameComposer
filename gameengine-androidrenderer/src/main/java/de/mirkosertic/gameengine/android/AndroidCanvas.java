package de.mirkosertic.gameengine.android;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import de.mirkosertic.gameengine.core.GameLoop;
import de.mirkosertic.gameengine.core.PlaySceneStrategy;

public class AndroidCanvas extends View {

    private PlaySceneStrategy playSceneStrategy;

    private transient Canvas currentCanvas;

    public AndroidCanvas(Context aContext) {
        super(aContext);
    }

    public AndroidCanvas(Context aContext, AttributeSet aAttrs) {
        super(aContext, aAttrs);
    }

    public AndroidCanvas(Context aContext, AttributeSet aAttrs, int aDefStyle) {
        super(aContext, aAttrs, aDefStyle);
    }

    public void setPlaySceneStrategy(PlaySceneStrategy playSceneStrategy) {
        this.playSceneStrategy = playSceneStrategy;
    }

    @Override
    public void draw(Canvas canvas) {

        currentCanvas = canvas;

        if (playSceneStrategy != null) {
            GameLoop theLoop = playSceneStrategy.getRunningGameLoop();
            if (theLoop != null && !theLoop.isShutdown()) {
                theLoop.singleRun();
            }
        }

        currentCanvas = null;

        this.invalidate();
    }

    public Canvas getCurrentCanvas() {
        return currentCanvas;
    }
}