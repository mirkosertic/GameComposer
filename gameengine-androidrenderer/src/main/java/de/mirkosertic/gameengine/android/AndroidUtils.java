package de.mirkosertic.gameengine.android;

import android.graphics.Paint;

public class AndroidUtils {

    public static Paint toFilledPaint(de.mirkosertic.gameengine.type.Color aColor) {
        Paint thePaint = new Paint();
        thePaint.setARGB(255, aColor.r, aColor.g, aColor.b);
        thePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        return thePaint;
    }

    public static Paint toStrokePaint(de.mirkosertic.gameengine.type.Color aColor) {
        Paint thePaint = new Paint();
        thePaint.setARGB(255, aColor.r, aColor.g, aColor.b);
        thePaint.setStyle(Paint.Style.STROKE);
        return thePaint;
    }
}
