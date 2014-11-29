package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.type.*;
import org.teavm.dom.core.Attr;
import org.teavm.dom.html.HTMLDocument;
import org.teavm.dom.html.HTMLElement;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TeaVMGameView implements GameView {

    private Size currentScreenSize;

    private CameraBehavior cameraComponent;
    private GestureDetector gestureDetector;
    private GameRuntime gameRuntime;

    private long lastRenderingDuration;

    private final Map<String, HTMLElement> instanceCache;
    private final Map<String, Attr> styleCache;
    private final HTMLDocument document;
    private final HTMLElement canvas;

    public TeaVMGameView(GameRuntime aGameRuntime, CameraBehavior aCameraComponent, GestureDetector aGestureDetector, HTMLDocument aHTMLDocument, HTMLElement aCanvasElement) {
        cameraComponent = aCameraComponent;
        gestureDetector = aGestureDetector;
        gameRuntime = aGameRuntime;
        instanceCache = new HashMap<>();
        styleCache = new HashMap<>();
        document = aHTMLDocument;
        canvas = aCanvasElement;
    }

    public void prepareNewScene(GameRuntime aGameRuntime, CameraBehavior aCameraComponent, GestureDetector aGestureDetector) {
        for (Map.Entry<String, HTMLElement> theEntry : instanceCache.entrySet()) {
            theEntry.getValue().getParentNode().removeChild(theEntry.getValue());
        }
        cameraComponent = aCameraComponent;
        gestureDetector = aGestureDetector;
        gameRuntime = aGameRuntime;

        instanceCache.clear();
    }

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene) {

        long theStart = System.currentTimeMillis();

        Color theBGColor = aScene.backgroundColorProperty().get();
        canvas.setAttribute("style", "width: " + currentScreenSize.width + "px; height: " + currentScreenSize.height + "px; background-color: rgb(" + theBGColor.r + "," + theBGColor.g + "," + theBGColor.b + "); ");

        Set<String> theInvisibleInstances = new HashSet<>(instanceCache.keySet());

        for (GameObjectInstance theInstance : cameraComponent.getObjectsToDrawInRightOrder(aScene)) {

            theInvisibleInstances.remove(theInstance.nameProperty().get());

            Position thePosition = cameraComponent.transformToScreenPosition(theInstance);

            Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();

            Angle theAngle = theInstance.rotationAngleProperty().get();
            int theAngleInDegrees = theAngle.angleInDegrees;
            String theRotateStyle = "transform: rotate("+theAngleInDegrees+"deg); "
                    +"-webkit-transform: rotate("+theAngleInDegrees+"deg); "
                    +"-moz-transform: rotate("+theAngleInDegrees+"deg); ";

            String theInstanceName = theInstance.nameProperty().get();
            HTMLElement theInstanceElement = instanceCache.get(theInstanceName);
            Attr theStyleNode;
            if (theInstanceElement == null) {
                theInstanceElement = document.createElement("div");
                theInstanceElement.setAttribute("name", theInstanceName);
                instanceCache.put(theInstanceName, theInstanceElement);
                canvas.appendChild(theInstanceElement);

                theStyleNode = theInstanceElement.getAttributeNode("style");
                styleCache.put(theInstanceName, theStyleNode);
            } else {
                theStyleNode = styleCache.get(theInstanceName);
            }
            theInstanceElement.setNodeValue("");

            Sprite theSpriteComponent = theInstance.getComponent(SpriteBehavior.class);
            if (theSpriteComponent != null) {

                ResourceName theSpriteResource = theSpriteComponent.resourceNameProperty().get();

                try {
                    TeaVMGameResource theGameResource = gameRuntime.getResourceCache()
                            .getResourceFor(theSpriteResource);

                    theStyleNode.setValue("position: absolute; top: " + ((int) thePosition.y) + "px; left: " + ((int) thePosition.x) + "px; width: "
                            + theSize.width + "px; height: " + theSize.height + "px;" + theRotateStyle+" background: url('" + theGameResource.getName()+"'); background-size: 100% 100%;");

                } catch (IOException e) {
                }
            } else {
                Text theTextComponent = theInstance.getComponent(TextBehavior.class);
                if (theTextComponent != null) {
                    Color theFontColor = theTextComponent.colorProperty().get();
                    Font theFont = theTextComponent.fontProperty().get();
                    ExpressionParser theExpressionParser = aScene.get(theTextComponent.textExpressionProperty().get());
                    theStyleNode.setValue("position: absolute; top: " + ((int) thePosition.y) + "px; left: " + ((int) thePosition.x)
                            + "px; color: rgb(" + theFontColor.r + "," + theFontColor.g + "," + theFontColor.b
                            + "); font-size: " + theFont.size + "px;" + theRotateStyle);

                    theInstanceElement.setNodeValue(theExpressionParser.evaluateToString());
                } else {
                    theStyleNode.setValue("position: absolute; top: " + ((int) thePosition.y) + "px; left: " + ((int) thePosition.x)
                            + "px; width: "
                            + theSize.width + "px; height: " + theSize.height + "px; border: 1px solid white;"
                            + theRotateStyle);
                }
            }
        }

        // Remove no longer visible instances
        for (String theInvisibleKey : theInvisibleInstances) {
            HTMLElement theInvisibleElement = instanceCache.remove(theInvisibleKey);
            theInvisibleElement.getParentNode().removeChild(theInvisibleElement);
            styleCache.remove(theInvisibleKey);
        }

        lastRenderingDuration = (lastRenderingDuration + System.currentTimeMillis() - theStart) / 2;
    }

    @Override
    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    public void setSize(Size aSize) {
        currentScreenSize = aSize;
    }
}
