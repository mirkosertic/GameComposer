package de.mirkosertic.gameengine.dragome;

import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.html.dom.w3c.BrowserElement;

public class DragomeCanvasRenderingContext2D {

    private final Object context;

    public DragomeCanvasRenderingContext2D(Object aJSContext) {
        context = aJSContext;
    }

    public void setFillStyle(String aStyle) {
        ScriptHelper.put("style", aStyle, context);
        ScriptHelper.put("instance", context, context);
        ScriptHelper.eval("instance.fillStyle = style", context);
    }

    public void clearRect(int aX, int aY, int aWidth, int aHeight) {
        ScriptHelper.put("x", aX, context);
        ScriptHelper.put("y", aY, context);
        ScriptHelper.put("width", aWidth, context);
        ScriptHelper.put("height", aHeight, context);
        ScriptHelper.put("instance", context, context);
        ScriptHelper.eval("instance.fillRect(x,y,width,height)", context);
    }

    public void save() {
        ScriptHelper.put("instance", context, context);
        ScriptHelper.eval("instance.save()", context);
    }

    public void restore() {
        ScriptHelper.put("instance", context, context);
        ScriptHelper.eval("instance.restore()", context);
    }

    public void translate(float aOffsetX, float aOffsetY) {
        ScriptHelper.put("offsetX", aOffsetX, context);
        ScriptHelper.put("offsetY", aOffsetY, context);
        ScriptHelper.put("instance", context, context);
        ScriptHelper.eval("instance.translate(offsetX,offsetY)", context);
    }

    public void rotate(float aRotationInRadians) {
        ScriptHelper.put("angle", aRotationInRadians, context);
        ScriptHelper.put("instance", context, context);
        ScriptHelper.eval("instance.rotate(angle)", context);
    }

    public void drawImage(BrowserElement aElement, float aOffsetX, float aOffsetY) {
        ScriptHelper.put("offsetX", aOffsetX, context);
        ScriptHelper.put("offsetY", aOffsetY, context);
        ScriptHelper.put("element", aElement, context);
        ScriptHelper.put("instance", context, context);
        ScriptHelper.eval("instance.drawImage(element.node,offsetX,offsetY)", context);
    }

    public void setStrokeStyle(String aStyle) {
        ScriptHelper.put("style", aStyle, context);
        ScriptHelper.put("instance", context, context);
        ScriptHelper.eval("instance.strokeStyle = style", context);
    }

    public void fillText(String aText, float aOffsetX, float aOffsetY) {
        ScriptHelper.put("text", aText, context);
        ScriptHelper.put("x", aOffsetX, context);
        ScriptHelper.put("y", aOffsetY, context);
        ScriptHelper.put("instance", context, context);
        ScriptHelper.eval("instance.fillText(text,x,y)", context);
    }

    public void setFont(String aFont) {
        ScriptHelper.put("font", aFont, context);
        ScriptHelper.put("instance", context, context);
        ScriptHelper.eval("instance.font = font", context);
    }

    public void setLineWidth(int aLineWidth) {
        ScriptHelper.put("width", aLineWidth, context);
        ScriptHelper.put("instance", context, context);
        ScriptHelper.eval("instance.lineWidth = width", context);
    }

    public void strokeRect(float aX, float aY, int aWidth, int aHeight) {
        ScriptHelper.put("x", aX, context);
        ScriptHelper.put("y", aY, context);
        ScriptHelper.put("width", aWidth, context);
        ScriptHelper.put("height", aHeight, context);
        ScriptHelper.put("instance", context, context);
        ScriptHelper.eval("instance.strokeRect(x,y,width,height)", context);
    }
}