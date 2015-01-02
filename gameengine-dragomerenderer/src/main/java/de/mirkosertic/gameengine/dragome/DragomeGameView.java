package de.mirkosertic.gameengine.dragome;

import com.dragome.enhancers.jsdelegate.JsDelegateFactory;
import com.dragome.html.dom.html5canvas.CanvasImageSource;
import com.dragome.html.dom.html5canvas.CanvasRenderingContext2D;
import com.dragome.html.dom.html5canvas.HTMLCanvasElement;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

class DragomeGameView extends GenericAbstractGameView<DragomeGameResource> {

    private final HTMLCanvasElement canvas;

    public DragomeGameView(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector, HTMLCanvasElement aCanvas) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
        canvas = aCanvas;
    }

    private CanvasRenderingContext2D renderingContext2D;

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new DragomeEffectCanvas(renderingContext2D);
    }

    @Override
    protected boolean beginFrame(GameScene aScene) {
        renderingContext2D = canvas.getContext("2d");
        Size theCurrentScreenSize = getCurrentScreenSize();
        renderingContext2D.setFillStyle(CSSUtils.toColor(aScene.backgroundColorProperty().get()));
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
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, DragomeGameResource aResource, float aPositionX, float aPositionY) {
		CanvasImageSource imageSource= JsDelegateFactory.createFromNode(aResource.getElement(), CanvasImageSource.class);
        renderingContext2D.drawImage(imageSource, aPositionX, aPositionY);
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
        renderingContext2D.strokeRect(aX, aY, (int) aWidth, (int) aHeight);
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
        renderingContext2D.restore();
    }

    @Override
    protected void logError(String aMessage) {
        DragomeLogger.error(aMessage);
    }

    @Override
    public void setCurrentScreenSize(Size aCurrentScreenSize) {
        super.setCurrentScreenSize(aCurrentScreenSize);

        canvas.setWidth(aCurrentScreenSize.width);
        canvas.setHeight(aCurrentScreenSize.height);
    }
}