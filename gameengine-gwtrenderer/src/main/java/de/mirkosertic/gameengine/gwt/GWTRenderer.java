package de.mirkosertic.gameengine.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class GWTRenderer implements EntryPoint {

    static final String holderId = "canvasholder";
    static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";

    private Canvas canvas;
    private Context2d context;
    private int counter;

    @Override
    public void onModuleLoad() {
        canvas = Canvas.createIfSupported();
        if (canvas == null) {
            RootPanel.get(holderId).add(new Label(upgradeMessage));
            return;
        }

        canvas.setStyleName("mainCanvas");
        RootPanel.get(holderId).add(canvas);
        context = canvas.getContext2d();

        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                resizeCanvas(event.getWidth(), event.getHeight());
            }
        });

        resizeCanvas(Window.getClientWidth(), Window.getClientHeight());

        // setup timer
        final Timer timer = new Timer() {
            @Override
            public void run() {
                doUpdate();
            }
        };
        timer.scheduleRepeating(30);
    }

    void resizeCanvas(int aWidth, int aHeight) {
        canvas.setWidth(aWidth + "px");
        canvas.setHeight(aHeight + "px");
        canvas.setCoordinateSpaceWidth(aWidth);
        canvas.setCoordinateSpaceHeight(aHeight);
    }

    void doUpdate() {
        context.setFillStyle(CssColor.make(128, 128, 128));
        context.setStrokeStyle(CssColor.make(128, 128, 128));
        context.fillRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
        context.setFillStyle(CssColor.make(0, 0, 0));
        context.setStrokeStyle(CssColor.make(0, 0, 0));
        context.fillText("Hello world " + counter++, 100, 100);

    }
}