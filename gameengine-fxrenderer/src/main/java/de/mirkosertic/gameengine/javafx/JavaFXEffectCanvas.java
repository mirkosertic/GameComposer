package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;
import javafx.scene.canvas.GraphicsContext;

public class JavaFXEffectCanvas implements EffectCanvas {

    private final GraphicsContext context;

    public JavaFXEffectCanvas(GraphicsContext context) {
        this.context = context;
    }

    @Override
    public void setPaint(Color aColor) {
        javafx.scene.paint.Color theFXColor = javafx.scene.paint.Color.rgb(aColor.r, aColor.g, aColor.b);
        context.setFill(theFXColor);
        context.setStroke(theFXColor);
    }

    @Override
    public void drawSingleDot(Position aPosition) {
        context.fillRect(aPosition.x, aPosition.y, 1, 1);
    }

    @Override
    public void fillRect(double aX, double aY, double aWidth, double aHeight) {
        context.fillRect(aX, aY, aWidth, aHeight);
    }

    @Override
    public void fillPolygon(double[] aXPositions, double[] aYPositions, int aNumberOfPositions) {
        double[] theX = new double[aNumberOfPositions];
        double[] theY = new double[aNumberOfPositions];
        for (int i=0;i<aNumberOfPositions;i++) {
            theX[i] = aXPositions[i];
            theY[i] = aYPositions[i];
        }
        context.fillPolygon(theX, theY, aNumberOfPositions);
    }

    @Override
    public void drawScaled(GameResource aResource, double aX, double aY, double aWidth, double aHeight) {
        JavaFXBitmapResource theBitmap = (JavaFXBitmapResource) aResource;
        context.drawImage(theBitmap, aX, aY, aWidth, aHeight);
    }
}