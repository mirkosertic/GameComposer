package de.mirkosertic.gameengine.gwt;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.type.*;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class GWTCanvasGameView extends GenericAbstractGameView<GWTBitmapResource> {

    private final Canvas canvas;

    public GWTCanvasGameView(GameRuntime aGameRuntime, Canvas aCanvas, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
        canvas = aCanvas;
    }

    private Context2d context2d;
    private GWTCanvasUtils.SavedState savedState;

    @Override
    protected boolean beginFrame(GameScene aScene) {
        context2d = canvas.getContext2d();
        savedState = GWTCanvasUtils.saveState(context2d);

        Color theBGColor = aScene.backgroundColorProperty().get();
        Size theCurrentSize = getCurrentScreenSize();

        CssColor theCssBackground = CssColor.make(theBGColor.r, theBGColor.g, theBGColor.b);
        context2d.setFillStyle(theCssBackground);
        context2d.setStrokeStyle(theCssBackground);
        context2d.fillRect(0, 0, theCurrentSize.width, theCurrentSize.height);

        return true;
    }

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new GWTEffectCanvas(context2d);
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, float aOffsetX, float aOffsetY, Angle aRotation) {
        int theAngleInDegrees = aRotation.angleInDegrees;
        context2d.translate(aOffsetX, aOffsetY);
        if (theAngleInDegrees % 360 != 0) {
            context2d.rotate(aRotation.toRadians());
        }
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, GWTBitmapResource aResource, float aPositionX,
            float aPositionY) {
        if (aResource.isLoaded()) {
            context2d.drawImage(aResource.getImage(), aPositionX, aPositionY);
        } else {
            Size theSize = aInstance.getOwnerGameObject().sizeProperty().get();
            context2d.setFillStyle(CssColor.make(255, 255, 255));
            context2d.setStrokeStyle(CssColor.make(255, 255, 255));
            context2d.setLineWidth(1);
            context2d.strokeRect(aPositionX, aPositionY, theSize.width, theSize.height);
        }

    }

    @Override
    protected void drawText(GameObjectInstance aInstance, Position aPosition, Font aFont, Color aColor, String aText,
            Size aSize) {
        GWTCanvasUtils.drawText(context2d, aPosition, aFont, aColor, aText, aSize);
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScren, Color aColor, float aX, float aY, float aWidth,
            float aHeight) {
        context2d.setFillStyle(CssColor.make(255, 255, 255));
        context2d.setStrokeStyle(CssColor.make(255, 255, 255));
        context2d.setLineWidth(1);
        context2d.strokeRect(aX, aY, aWidth, aHeight);
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
        GWTCanvasUtils.restoreState(context2d, savedState);
    }

    @Override
    protected void logError(String aMessage) {
        throw new RuntimeException(aMessage);
    }
}