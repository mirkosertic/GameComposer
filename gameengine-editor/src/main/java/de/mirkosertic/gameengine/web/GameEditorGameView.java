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
package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.teavm.TeaVMGameView;
import de.mirkosertic.gameengine.teavm.pixi.Container;
import de.mirkosertic.gameengine.teavm.pixi.Filter;
import de.mirkosertic.gameengine.teavm.pixi.Renderer;
import de.mirkosertic.gameengine.teavm.pixi.Uniforms;
import org.teavm.jso.JSBody;
import org.teavm.jso.core.JSArray;

public class GameEditorGameView extends TeaVMGameView {

    public static abstract class GridUniforms implements Uniforms {

        @JSBody(params = {}, script = "return {};")
        public static native GridUniforms create();
    }

    public GameEditorGameView(GameRuntime aGameRuntime,
            CameraBehavior aCameraBehavior,
            GestureDetector aGestureDetector,
            Renderer aRenderer) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector, aRenderer);

        // http://www.awwwards.com/a-gentle-introduction-to-shaders-with-pixi-js.html

        Container theStage = getStage();
        Filter theFilter = Filter.createFilter("", "precision mediump float;\n" +
                "\n" +
                "varying vec2 vTextureCoord;//The coordinates of the current pixel\n" +
                "uniform sampler2D uSampler;//The image data\n" +
                "\n" +
                "void main(void) {\n" +
                "   gl_FragColor = texture2D(uSampler, vTextureCoord);\n" +
                "   gl_FragColor.r = 0.0;\n" +
                "}", GridUniforms.create());
        //theStage.setFilters(JSArray.of(theFilter));
    }

    @Override
    protected boolean includeInRendering(GameObjectInstance aInstance) {
        return true;
    }
}
