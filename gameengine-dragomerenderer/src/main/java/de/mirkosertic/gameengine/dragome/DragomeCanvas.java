package de.mirkosertic.gameengine.dragome;

import com.dragome.commons.javascript.ScriptHelper;
import org.w3c.dom.Element;

public class DragomeCanvas {

    private Element element;

    public DragomeCanvas(Element aElement) {
        element = aElement;
    }

    public void setWidth(int aWidth) {
        ScriptHelper.put("aWidth", aWidth, this);
        ScriptHelper.put("aWidth", aWidth, this);
        ScriptHelper.eval("this.$$$element.node.width = aWidth", this);
    }

    public void setHeight(int aHeight) {
        ScriptHelper.put("aHeight", aHeight, this);
        ScriptHelper.eval("this.$$$element.node.height = aHeight", this);
    }

    public DragomeCanvasRenderingContext2D getContext(String aContext) {
        ScriptHelper.put("aContext", aContext, this);
        Object theResult = ScriptHelper.eval("this.$$$element.node.getContext(aContext)", this);
        return new DragomeCanvasRenderingContext2D(theResult);
    }
}
