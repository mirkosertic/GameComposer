package de.mirkosertic.gameengine.gwt;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;

import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

abstract class AbstractWebGameView implements GameView {

    protected static class SavedState {
        public FillStrokeStyle fillStyle;
        public FillStrokeStyle strokeStyle;
        public String font;
        public double lineWidth;
    }

    private Size size;

    Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    protected SavedState saveState(Context2d aContext) {
        SavedState theResult = new SavedState();
        theResult.fillStyle = aContext.getFillStyle();
        theResult.strokeStyle = aContext.getStrokeStyle();
        theResult.font = aContext.getFont();
        theResult.lineWidth = aContext.getLineWidth();
        return theResult;
    }

    protected void restoreState(Context2d aContext, SavedState aState) {
        aContext.setFillStyle(aState.fillStyle);
        aContext.setStrokeStyle(aState.strokeStyle);
        aContext.setFont(aState.font);
        aContext.setLineWidth(aState.lineWidth);
        aContext.setTransform(1, 0, 0, 1, 0, 0);
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

    void drawText(Context2d aContext, Position aPosition, de.mirkosertic.gameengine.type.Font aFont, de.mirkosertic.gameengine.type.Color aColor, String aText, Size aSize) {
        SavedState theSavedState = saveState(aContext);
        CssColor theTextColor = CssColor.make(aColor.r, aColor.g, aColor.b);
        aContext.setFillStyle(theTextColor);
        aContext.setStrokeStyle(theTextColor);
        aContext.setFont(toFont(aFont));
        aContext.fillText(aText, aPosition.x, aPosition.y + aFont.size);
        restoreState(aContext, theSavedState);
    }
}
