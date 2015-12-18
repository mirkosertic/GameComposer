package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.generic.CSSCache;
import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.type.*;

import org.teavm.jso.canvas.CanvasImageSource;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.HashMap;
import java.util.Map;

class TeaVMGameView extends GenericAbstractGameView<TeaVMGameResource> {

    private final Map<String, HTMLElement> instanceCache;
    private final HTMLCanvasElement html5Canvas;
    private final CSSCache cssCache;
    private final TeaVMPrerenderedTextCache prerenderedTextCache;

    public TeaVMGameView(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector, HTMLCanvasElement aHTML5CanvasElement, TeaVMPrerenderedTextCache aPrerenderedTextCache) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
        html5Canvas = aHTML5CanvasElement;
        instanceCache = new HashMap<>();
        cssCache = new CSSCache();
        prerenderedTextCache = aPrerenderedTextCache;
    }

    @Override
    public void prepareNewScene(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector) {
        super.prepareNewScene(aGameRuntime, aCameraBehavior,aGestureDetector);
        for (Map.Entry<String, HTMLElement> theEntry : instanceCache.entrySet()) {
            theEntry.getValue().getParentNode().removeChild(theEntry.getValue());
        }
        instanceCache.clear();
    }

    private CanvasRenderingContext2D renderingContext2D;

    @Override
    protected boolean beginFrame(GameScene aScene) {
        renderingContext2D = (CanvasRenderingContext2D) html5Canvas.getContext("2d");
        Size theCurrentScreenSize = getCurrentScreenSize();
        Color theBGColor = aScene.backgroundColorProperty().get();
        renderingContext2D.setFillStyle(CSSUtils.toColor(theBGColor));
        renderingContext2D.clearRect(0, 0, theCurrentScreenSize.width, theCurrentScreenSize.height);
        return true;
    }

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new TeaVMEffectCanvas(renderingContext2D);
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Angle aRotation) {
        renderingContext2D.save();

        renderingContext2D.translate(aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        renderingContext2D.rotate(aRotation.toRadians());
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, TeaVMGameResource aResource) {
        renderingContext2D.drawImage(aResource.getElement(), -aCenterOffset.x, -aCenterOffset.y);
    }

    @Override
    protected void drawText(GameObjectInstance aInstance, Position aPosition, Position aCenterOffset, Font aFont, Color aColor, String aText, Size aSize) {
        CanvasImageSource theImageSource = prerenderedTextCache.getImageSourceFor(cssCache, aSize, aFont, aColor, aText);
        renderingContext2D.drawImage(theImageSource, -aCenterOffset.x, -aCenterOffset.y);
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Color aColor, Size aSize) {
        String theColor = cssCache.getCSS(aColor);
        renderingContext2D.setFillStyle(theColor);
        renderingContext2D.setStrokeStyle(theColor);
        renderingContext2D.setLineWidth(1);
        renderingContext2D.strokeRect(-aCenterOffset.x, -aCenterOffset.y, aSize.width, aSize.height);
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
        renderingContext2D.restore();
    }

    @Override
    protected void logError(String aMessage) {
        TeaVMLogger.error(aMessage);
    }

    public void setSize(Size aSize) {
        super.setCurrentScreenSize(aSize);
        html5Canvas.setWidth(aSize.width);
        html5Canvas.setHeight(aSize.height);
    }
}