package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.type.*;

import org.teavm.dom.canvas.CanvasRenderingContext2D;
import org.teavm.dom.html.HTMLCanvasElement;
import org.teavm.dom.html.HTMLElement;

import java.util.HashMap;
import java.util.Map;

class TeaVMGameView extends GenericAbstractGameView<TeaVMGameResource> {

    private final Map<String, HTMLElement> instanceCache;
    private final HTMLCanvasElement html5Canvas;

    public TeaVMGameView(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector, HTMLCanvasElement aHTML5CanvasElement) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
        html5Canvas = aHTML5CanvasElement;
        instanceCache = new HashMap<>();
    }

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
    protected void beforeInstance(GameObjectInstance aInstance, float aOffsetX, float aOffsetY, Angle aRotation) {
        renderingContext2D.save();

        int theAngleInDegrees = aRotation.angleInDegrees;
        renderingContext2D.translate(aOffsetX, aOffsetY);
        if (theAngleInDegrees % 360 != 0) {
            renderingContext2D.rotate(aRotation.toRadians());
        }
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, TeaVMGameResource aResource, float aPositionX, float aPositionY) {
        renderingContext2D.drawImage(aResource.getElement(), aPositionX, aPositionY);
    }

    @Override
    protected void drawText(GameObjectInstance aInstance, Position aPosition, Font aFont, Color aColor, String aText, Size aSize) {
        String theTextColor = CSSUtils.toColor(aColor);
        renderingContext2D.setFillStyle(theTextColor);
        renderingContext2D.setStrokeStyle(theTextColor);
        renderingContext2D.setFont(CSSUtils.toFont(aFont));
        renderingContext2D.fillText(aText, aPosition.x, aPosition.y + aFont.size);
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Color aColor, float aX, float aY, float aWidth, float aHeight) {
        String theColor = CSSUtils.toColor(aColor);
        renderingContext2D.setFillStyle(theColor);
        renderingContext2D.setStrokeStyle(theColor);
        renderingContext2D.setLineWidth(1);
        renderingContext2D.strokeRect(aX, aY, aWidth, aHeight);
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