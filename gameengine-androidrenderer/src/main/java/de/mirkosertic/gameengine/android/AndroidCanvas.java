package de.mirkosertic.gameengine.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class AndroidCanvas extends SurfaceView {

    public AndroidCanvas(Context aContext) {
        super(aContext);
    }

    public AndroidCanvas(Context aContext, AttributeSet aAttrs) {
        super(aContext, aAttrs);
    }

    public AndroidCanvas(Context aContext, AttributeSet aAttrs, int aDefStyle) {
        super(aContext, aAttrs, aDefStyle);
    }
}