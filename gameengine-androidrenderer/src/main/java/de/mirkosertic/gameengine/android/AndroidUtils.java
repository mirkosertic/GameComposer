package de.mirkosertic.gameengine.android;

import android.graphics.Paint;

public class AndroidUtils {

    public static Paint toPaint(de.mirkosertic.gameengine.type.Color aColor) {
        Paint thePaint = new Paint();
        thePaint.setARGB(255, aColor.r, aColor.g, aColor.b);
        thePaint.setStyle(Paint.Style.FILL);
        return thePaint;
    }
}
