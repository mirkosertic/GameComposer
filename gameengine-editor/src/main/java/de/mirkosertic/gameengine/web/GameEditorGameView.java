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
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.teavm.TeaVMGameView;
import de.mirkosertic.gameengine.teavm.pixi.Filter;
import de.mirkosertic.gameengine.teavm.pixi.Renderer;
import de.mirkosertic.gameengine.teavm.pixi.Uniforms;
import de.mirkosertic.gameengine.type.Position;
import org.teavm.jso.JSBody;
import org.teavm.jso.core.JSArray;

public class GameEditorGameView extends TeaVMGameView {

    public static abstract class GridUniforms implements Uniforms {

        @JSBody(params = {"aXOffset", "aYOffset", "aWidth", "aHeight"}, script = "return {width: {type: 'f', value: aWidth}, height: {type: 'f', value: aHeight}, xoffset: {type: 'f', value: aYOffset}, yoffset: {type: 'f', value: aXOffset}, screenHeight: {type: 'f', value: 0}};")
        public static native GridUniforms create(float aXOffset, float aYOffset, float aWidth, float aHright);

        @JSBody(params = {"aFilter", "aValue"}, script = "aFilter.uniforms.width = aValue;")
        public static native void setWidth(Filter aFilter, float aValue);

        @JSBody(params = {"aFilter", "aValue"}, script = "aFilter.uniforms.height = aValue;")
        public static native void setHeight(Filter aFilter, float aValue);

        @JSBody(params = {"aFilter", "aValue"}, script = "aFilter.uniforms.xoffset = aValue;")
        public static native void setXOffset(Filter aFilter, float aValue);

        @JSBody(params = {"aFilter", "aValue"}, script = "aFilter.uniforms.yoffset = aValue;")
        public static native void setYOffset(Filter aFilter, float aValue);

        @JSBody(params = {"aFilter", "aValue"}, script = "aFilter.uniforms.screenHeight = aValue;")
        public static native void setScrenHeight(Filter aFilter, float aValue);

    }

    private final Filter gridFilter;

    private final Property<Integer> gridWidth;
    private final Property<Integer> gridHeight;

    public GameEditorGameView(GameRuntime aGameRuntime,
            CameraBehavior aCameraBehavior,
            GestureDetector aGestureDetector,
            Renderer aRenderer) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector, aRenderer);

        gridWidth = new Property<Integer>(Integer.class, this, "gridWidth", 64);
        gridHeight = new Property<Integer>(Integer.class, this, "gridHeight", 64);

        // http://www.awwwards.com/a-gentle-introduction-to-shaders-with-pixi-js.html
        //http://stackoverflow.com/questions/17339149/webgl-how-do-i-get-the-position-of-every-pixel-in-fragment-shader

        gridFilter = Filter.createFilter("", "precision mediump float;\n" +
                "\n" +
                "varying vec2 vTextureCoord;//The coordinates of the current pixel\n" +
                "uniform sampler2D uSampler;//The image data\n" +
                "uniform float xoffset;\n" +
                "uniform float yoffset;\n" +
                "uniform float width;\n" +
                "uniform float height;\n" +
                "uniform float screenHeight;\n" +
                "\n" +
                "int func_mod(int x, int y) {\n" +
                "    return int(float(x)-float(y)*floor(float(x)/float(y)));\n" +
                 "}"+
                "\n" +
                "void main(void) {\n" +
                "   if ((int(mod(xoffset + gl_FragCoord.x + 0.5, width)) == 0) || (int(mod(yoffset + (screenHeight - gl_FragCoord.y) - 0.5, height)) == 0)) {\n" +
                "       gl_FragColor.r = 1.0;\n" +
                "       gl_FragColor.g = 1.0;\n" +
                "       gl_FragColor.b = 1.0;\n" +
                "   } else {\n" +
                "       gl_FragColor = texture2D(uSampler, vTextureCoord);\n" +
                "  }\n" +
                "}", GridUniforms.create(0, 0, 10f, 10f));
        getStage().setFilters(JSArray.of(gridFilter));
    }

    public Property<Integer> gridWidth() {
        return gridWidth;
    }

    public Property<Integer> gridHeight() {
        return gridHeight;
    }

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene, RuntimeStatistics aStatistics) {
        Position theCamera = getCameraBehavior().getInstance().positionProperty().get();
        GridUniforms.setXOffset(gridFilter, theCamera.x);
        GridUniforms.setYOffset(gridFilter, theCamera.y);
        GridUniforms.setWidth(gridFilter, (float) gridWidth.get());
        GridUniforms.setHeight(gridFilter, (float) gridHeight.get());
        GridUniforms.setScrenHeight(gridFilter, (float) getCurrentScreenSize().height);
        super.renderGame(aGameTime, aElapsedTimeSinceLastLoop, aScene, aStatistics);
    }

    @Override
    protected boolean includeInRendering(GameObjectInstance aInstance) {
        return true;
    }
}
