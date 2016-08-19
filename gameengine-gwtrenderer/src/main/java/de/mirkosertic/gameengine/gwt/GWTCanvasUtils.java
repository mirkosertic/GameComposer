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

import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;

public class GWTCanvasUtils {

    public static class SavedState {
        public FillStrokeStyle fillStyle;
        public FillStrokeStyle strokeStyle;
        public String font;
        public double lineWidth;
    }

    public static SavedState saveState(Context2d aContext) {
        SavedState theResult = new SavedState();
        theResult.fillStyle = aContext.getFillStyle();
        theResult.strokeStyle = aContext.getStrokeStyle();
        theResult.font = aContext.getFont();
        theResult.lineWidth = aContext.getLineWidth();
        return theResult;
    }

    public static void drawText(Context2d aContext, Position aPosition, Font aFont, Color aColor, String aText, Size aSize) {
        GWTCanvasUtils.SavedState theSavedState = GWTCanvasUtils.saveState(aContext);
        CssColor theTextColor = CssColor.make(aColor.r, aColor.g, aColor.b);
        aContext.setFillStyle(theTextColor);
        aContext.setStrokeStyle(theTextColor);
        aContext.setFont(CSSUtils.toFont(aFont));
        aContext.fillText(aText, -aSize.width / 2, aFont.size - aSize.height / 2);
        GWTCanvasUtils.restoreState(aContext, theSavedState);
    }

    public static void restoreState(Context2d aContext, SavedState aState) {
        aContext.setFillStyle(aState.fillStyle);
        aContext.setStrokeStyle(aState.strokeStyle);
        aContext.setFont(aState.font);
        aContext.setLineWidth(aState.lineWidth);
        aContext.setTransform(1, 0, 0, 1, 0, 0);
    }
}
