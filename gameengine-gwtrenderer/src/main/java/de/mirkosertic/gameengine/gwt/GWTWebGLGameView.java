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
package de.mirkosertic.gameengine.gwt;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;
import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.WebGLConstants;
import thothbot.parallax.core.client.gl2.WebGLProgram;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.WebGLShader;
import thothbot.parallax.core.client.gl2.WebGLTexture;
import thothbot.parallax.core.client.gl2.WebGLUniformLocation;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.client.gl2.enums.BeginMode;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorDest;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorSrc;
import thothbot.parallax.core.client.gl2.enums.BufferTarget;
import thothbot.parallax.core.client.gl2.enums.BufferUsage;
import thothbot.parallax.core.client.gl2.enums.DataType;
import thothbot.parallax.core.client.gl2.enums.DrawElementsType;
import thothbot.parallax.core.client.gl2.enums.EnableCap;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.PixelStoreParameter;
import thothbot.parallax.core.client.gl2.enums.PixelType;
import thothbot.parallax.core.client.gl2.enums.ProgramParameter;
import thothbot.parallax.core.client.gl2.enums.TextureParameterName;
import thothbot.parallax.core.client.gl2.enums.TextureTarget;
import thothbot.parallax.core.client.gl2.enums.TextureUnit;
import thothbot.parallax.core.shared.math.Matrix4;

import java.util.HashMap;
import java.util.Map;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public class GWTWebGLGameView extends GenericAbstractGameView<GWTBitmapResource> {

    interface Resource extends ClientBundle {

        Resource INSTANCE = GWT.create(Resource.class);

        @Source("source/sprite.vs")
        TextResource getVertexShader();

        @Source("source/sprite.fs")
        TextResource getFragmentShader();
    }

    static class GLSprite {

        final Float32Array vertices;
        final Uint16Array faces;

        final WebGLBuffer vertexBuffer;
        final WebGLBuffer elementBuffer;
        final WebGLProgram program;

        WebGLUniformLocation modelViewTransform;
        WebGLUniformLocation screenPosition;
        WebGLUniformLocation scale;
        WebGLUniformLocation rotation;
        WebGLUniformLocation textureMap;
        WebGLUniformLocation drawBorder;

        int vertexPositionVariable;
        int uvVariable;

        private final WebGLRenderingContext renderingContext;
        Map<String, WebGLTexture> loadedTextures;

        GLSprite(WebGLRenderingContext aRenderingContext) {

            renderingContext = aRenderingContext;

            vertices = Float32Array.create(8 + 8);
            faces = Uint16Array.create(6);

            int i = 0;

            vertices.set(i++, -0.5f);
            vertices.set(i++, -0.5f);    // vertex 0
            vertices.set(i++, 0);
            vertices.set(i++, 1f);    // uv 0

            vertices.set(i++, 0.5f);
            vertices.set(i++, -0.5f);    // vertex 1
            vertices.set(i++, 1f);
            vertices.set(i++, 1f);    // uv 1

            vertices.set(i++, 0.5f);
            vertices.set(i++, 0.5f);    // vertex 2
            vertices.set(i++, 1f);
            vertices.set(i++, 0);    // uv 2

            vertices.set(i++, -0.5f);
            vertices.set(i++, 0.5f);    // vertex 3
            vertices.set(i++, 0);
            vertices.set(i++, 0);    // uv 3

            i = 0;

            faces.set(i++, 0);
            faces.set(i++, 1);
            faces.set(i++, 2);
            faces.set(i++, 0);
            faces.set(i++, 2);
            faces.set(i++, 3);

            vertexBuffer = aRenderingContext.createBuffer();
            elementBuffer = aRenderingContext.createBuffer();

            aRenderingContext.bindBuffer(BufferTarget.ARRAY_BUFFER, vertexBuffer);
            aRenderingContext.bufferData(BufferTarget.ARRAY_BUFFER, vertices, BufferUsage.STATIC_DRAW);

            aRenderingContext.bindBuffer(BufferTarget.ELEMENT_ARRAY_BUFFER, elementBuffer);
            aRenderingContext.bufferData(BufferTarget.ELEMENT_ARRAY_BUFFER, faces, BufferUsage.STATIC_DRAW);

            // Create the program
            program = aRenderingContext.createProgram();

            WebGLShader theVertexShader = aRenderingContext.createShader(WebGLConstants.VERTEX_SHADER);
            aRenderingContext.shaderSource(theVertexShader, Resource.INSTANCE.getVertexShader().getText());
            aRenderingContext.compileShader(theVertexShader);
            if (!aRenderingContext.getShaderParameterb(theVertexShader, WebGLConstants.COMPILE_STATUS)) {
                throw new RuntimeException(aRenderingContext.getShaderInfoLog(theVertexShader));
            }

            WebGLShader theFragmentShader = aRenderingContext.createShader(WebGLConstants.FRAGMENT_SHADER);
            aRenderingContext.shaderSource(theFragmentShader, Resource.INSTANCE.getFragmentShader().getText());
            aRenderingContext.compileShader(theFragmentShader);
            if (!aRenderingContext.getShaderParameterb(theFragmentShader, WebGLConstants.COMPILE_STATUS)) {
                throw new RuntimeException(aRenderingContext.getShaderInfoLog(theFragmentShader));
            }

            aRenderingContext.attachShader(program, theVertexShader);
            aRenderingContext.attachShader(program, theFragmentShader);
            aRenderingContext.linkProgram(program);

            if (!aRenderingContext.getProgramParameterb(program, ProgramParameter.LINK_STATUS)) {
                throw new RuntimeException(aRenderingContext.getProgramInfoLog(program));
            }

            aRenderingContext.deleteShader(theVertexShader);
            aRenderingContext.deleteShader(theFragmentShader);

            // And finally we need to bind the variables and uniforms
            vertexPositionVariable = aRenderingContext.getAttribLocation(program, "aVertexPosition");
            aRenderingContext.enableVertexAttribArray(vertexPositionVariable);

            uvVariable = aRenderingContext.getAttribLocation(program, "aUv");
            aRenderingContext.enableVertexAttribArray(uvVariable);

            modelViewTransform = aRenderingContext.getUniformLocation(program, "uPMatrix");
            screenPosition = aRenderingContext.getUniformLocation(program, "uScreenPosition");
            drawBorder = aRenderingContext.getUniformLocation(program, "uDrawBorder");

            scale = aRenderingContext.getUniformLocation(program, "uScale");
            rotation = aRenderingContext.getUniformLocation(program, "uRotation");
            textureMap = aRenderingContext.getUniformLocation(program, "uMap");

            // The texture cache with resource name and other studd
            loadedTextures = new HashMap<>();
        }

        public void render(Position aPosition, Size aSize, Angle aRotation, GWTBitmapResource aResource) {

            WebGLTexture theTexture = null;
            if (aResource != null && aResource.isLoaded()) {
                theTexture = loadedTextures.get(aResource.getImage().getSrc());
                if (theTexture == null) {
                    // Create and load a new texture
                    // This is an expensive operation, so do it only once
                    theTexture = renderingContext.createTexture();

                    renderingContext.activeTexture(TextureUnit.TEXTURE0);
                    renderingContext.bindTexture(TextureTarget.TEXTURE_2D, theTexture);
                    renderingContext.texParameteri(TextureTarget.TEXTURE_2D, TextureParameterName.TEXTURE_MIN_FILTER, WebGLConstants.LINEAR);
                    renderingContext.texParameteri(TextureTarget.TEXTURE_2D, TextureParameterName.TEXTURE_WRAP_S, WebGLConstants.CLAMP_TO_EDGE);
                    renderingContext.texParameteri(TextureTarget.TEXTURE_2D, TextureParameterName.TEXTURE_WRAP_T, WebGLConstants.CLAMP_TO_EDGE);
                    renderingContext.pixelStorei(PixelStoreParameter.UNPACK_PREMULTIPLY_ALPHA_WEBGL, 0);
                    renderingContext.texImage2D(TextureTarget.TEXTURE_2D, 0, PixelFormat.RGBA, PixelType.UNSIGNED_BYTE, aResource.getImage());
                    renderingContext.bindTexture(TextureTarget.TEXTURE_2D, null);

                    loadedTextures.put(aResource.getImage().getSrc(), theTexture);
                }
            }

            // Position, Scale and Rotation
            renderingContext.uniform2f(screenPosition, aPosition.x + aSize.width / 2, aPosition.y + aSize.height / 2);
            renderingContext.uniform2f(scale, aSize.width, aSize.height);
            renderingContext.uniform1f(rotation, aRotation.toRadians());

            // 3 elements per vertex
            renderingContext.bindBuffer(BufferTarget.ARRAY_BUFFER, vertexBuffer);
            renderingContext.vertexAttribPointer(vertexPositionVariable, 2, DataType.FLOAT, false, 2 * 8, 0);
            renderingContext.vertexAttribPointer(uvVariable, 2, DataType.FLOAT, false, 2 * 8, 8);
            renderingContext.bindBuffer(BufferTarget.ELEMENT_ARRAY_BUFFER, elementBuffer);

            if (theTexture != null) {
                renderingContext.enable(EnableCap.BLEND);
                renderingContext.activeTexture(TextureUnit.TEXTURE0, 0);
                renderingContext.bindTexture(TextureTarget.TEXTURE_2D, theTexture);
                renderingContext.uniform1i(textureMap, 0);
                renderingContext.uniform1f(drawBorder, 0);

                renderingContext.drawElements(BeginMode.TRIANGLES, 6, DrawElementsType.UNSIGNED_SHORT, 0);
            } else {
                renderingContext.disable(EnableCap.BLEND);
                renderingContext.uniform1i(textureMap, 0);
                renderingContext.uniform1f(drawBorder, 0);

                renderingContext.drawArrays(BeginMode.LINE_LOOP, 0, 4);
            }
        }
    }

    private final WebGLRenderingContext webGLRenderingContext;
    private final Canvas canvas;
    private final GLSprite sprite;

    private Matrix4 transform;

    public GWTWebGLGameView(GameRuntime aRuntime, WebGLRenderingContext aWebGLRenderingContext, Canvas aCanvas, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector) {
        super(aRuntime, aCameraBehavior, aGestureDetector);
        webGLRenderingContext = aWebGLRenderingContext;
        canvas = aCanvas;

        sprite = new GLSprite(aWebGLRenderingContext);
    }

    @Override
    public void setCurrentScreenSize(Size aSize) {
        super.setCurrentScreenSize(aSize);

        webGLRenderingContext.viewport(0, 0, aSize.width, aSize.height);

        Matrix4 theMat4 = new Matrix4();
        theMat4.makeOrthographic(0, aSize.width, 0, aSize.height, -10, 10);

        transform = theMat4;
    }

    private Context2d context2d;
    private GWTCanvasUtils.SavedState savedState;

    @Override
    protected boolean beginFrame(GameScene aScene) {

        context2d = canvas.getContext2d();
        savedState = GWTCanvasUtils.saveState(context2d);

        Color theBGColor = aScene.backgroundColorProperty().get();
        Size theSize = getCurrentScreenSize();

        context2d.clearRect(0,0, theSize.width, theSize.height);

        webGLRenderingContext.clearColor(theBGColor.r / 255, theBGColor.g / 255, theBGColor.b / 255, 1);
        webGLRenderingContext.clear(WebGLConstants.COLOR_BUFFER_BIT | WebGLConstants.DEPTH_BUFFER_BIT);
        webGLRenderingContext.disable(EnableCap.DEPTH_TEST);

        webGLRenderingContext.blendFunc(BlendingFactorSrc.SRC_ALPHA, BlendingFactorDest.ONE_MINUS_SRC_ALPHA);
        webGLRenderingContext.enable(EnableCap.BLEND);
        webGLRenderingContext.depthMask(true);

        webGLRenderingContext.useProgram(sprite.program);

        // Setup model world transform
        webGLRenderingContext.uniformMatrix4fv(sprite.modelViewTransform, false, transform.getArray());

        return true;
    }

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new GWTEffectCanvas(context2d);
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Angle aRotation) {
        context2d.translate(aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        context2d.rotate(aRotation.toRadians());
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, GWTBitmapResource aResource) {
        sprite.render(aPositionOnScreen, aInstance.getOwnerGameObject().sizeProperty().get(), aInstance.rotationAngleProperty().get(), aResource);
    }

    @Override
    protected void drawText(String aInstanceID, Position aPosition, Angle aAngle, Position aCenterOffset, Font aFont, Color aColor, String aText,
            Size aSize, boolean aVisible) {
        GWTCanvasUtils.drawText(context2d, aPosition, aFont, aColor, aText, aSize);
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Color aColor, Size aSize) {
        sprite.render(aPositionOnScreen, aInstance.getOwnerGameObject().sizeProperty().get(), aInstance.rotationAngleProperty().get(), null);
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
        GWTCanvasUtils.restoreState(context2d, savedState);
    }
}