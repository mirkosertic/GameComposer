package de.mirkosertic.gameengine.gwt;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.TextExpression;

abstract class AbstractWebGameView implements GameView {

    private Size size;

    Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    String toFont(de.mirkosertic.gameengine.type.Font aFont) {
        switch (aFont.name) {
            case ARIAL:
                return Integer.toString(aFont.size)+"px arial";
            case VERDANA:
                return Integer.toString(aFont.size)+"px verdana";
        }
        throw new IllegalArgumentException("Wrong font name : "+aFont.name);
    }

    void drawText(Context2d aContext, GameObjectInstance aInstance, Position aPosition, de.mirkosertic.gameengine.type.Font aFont, de.mirkosertic.gameengine.type.Color aColor, TextExpression aExpression, Size aSize) {
        aContext.save();
        CssColor theTextColor = CssColor.make(aColor.r, aColor.g, aColor.b);
        aContext.setFillStyle(theTextColor);
        aContext.setStrokeStyle(theTextColor);
        aContext.setFont(toFont(aFont));
        aContext.fillText(aExpression.expression, aPosition.x, aPosition.y + aFont.size);
        aContext.restore();
    }
}
