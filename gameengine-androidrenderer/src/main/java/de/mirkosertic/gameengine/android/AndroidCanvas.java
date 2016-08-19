/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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