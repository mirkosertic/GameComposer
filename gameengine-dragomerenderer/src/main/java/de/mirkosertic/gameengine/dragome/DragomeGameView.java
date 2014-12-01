package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.ExpressionParser;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.RuntimeStatistics;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.type.Size;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.dragome.services.ServiceLocator;

class DragomeGameView implements GameView {

    private Size currentScreenSize;

    private CameraBehavior cameraComponent;
    private GestureDetector gestureDetector;
    private GameRuntime gameRuntime;

    private final Map<String, Element> instanceCache;
    private final Map<String, Attr> styleCache;

    public DragomeGameView(GameRuntime aGameRuntime, CameraBehavior aCameraComponent, GestureDetector aGestureDetector) {
        cameraComponent = aCameraComponent;
        gestureDetector = aGestureDetector;
        gameRuntime = aGameRuntime;
        instanceCache = new HashMap<>();
        styleCache = new HashMap<>();
    }

    public void prepareNewScene(GameRuntime aGameRuntime, CameraBehavior aCameraComponent, GestureDetector aGestureDetector) {
        for (Map.Entry<String, Element> theEntry : instanceCache.entrySet()) {
            theEntry.getValue().getParentNode().removeChild(theEntry.getValue());
        }
        cameraComponent = aCameraComponent;
        gestureDetector = aGestureDetector;
        gameRuntime = aGameRuntime;

        instanceCache.clear();
        styleCache.clear();
    }

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene, RuntimeStatistics aStatistics) {

        Document theDocument = ServiceLocator.getInstance().getDomHandler().getDocument();
        Element theCanvas = ServiceLocator.getInstance().getDomHandler().getElementBySelector("#canvas");

        Color theBGColor = aScene.backgroundColorProperty().get();
        theCanvas.setAttribute("style","width: "+currentScreenSize.width+"px; height: "+currentScreenSize.height+"px; background-color: rgb("+theBGColor.r+","+theBGColor.g+","+theBGColor.b+"); ");

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
            Element theInstanceElement = instanceCache.get(theInstanceName);
            Attr theStyleNode;
            if (theInstanceElement == null) {
                theInstanceElement = theDocument.createElement("div");
                theInstanceElement.setAttribute("name", theInstanceName);
                instanceCache.put(theInstanceName, theInstanceElement);
                theCanvas.appendChild(theInstanceElement);

                theStyleNode = theInstanceElement.getAttributeNode("style");
                styleCache.put(theInstanceName, theStyleNode);
            } else {
                theStyleNode = styleCache.get(theInstanceName);
            }
            theInstanceElement.setTextContent("");

            Sprite theSpriteComponent = theInstance.getComponent(SpriteBehavior.class);
            if (theSpriteComponent != null) {

                ResourceName theSpriteResource = theSpriteComponent.resourceNameProperty().get();

                try {
                    DragomeGameResource theGameResource = gameRuntime.getResourceCache()
                            .getResourceFor(theSpriteResource);

                    theStyleNode.setValue("position: absolute; top: " + ((int) thePosition.y) + "px; left: " + ((int) thePosition.x) + "px; width: "
                                    + theSize.width + "px; height: " + theSize.height + "px;" + theRotateStyle+" background: url('" + theGameResource.getName()+"'); background-size: 100% 100%;");

                } catch (IOException e) {
                    DragomeLogger.error("Error while loading game resource "+theSpriteResource.name+" : " + e);
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

                    theInstanceElement.setTextContent(theExpressionParser.evaluateToString()+" "+aStatistics.getAverageTimePerLoopCycle());
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
            Element theInvisibleElement = instanceCache.remove(theInvisibleKey);
            theInvisibleElement.getParentNode().removeChild(theInvisibleElement);
            styleCache.remove(theInvisibleKey);
        }
    }

    @Override
    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    public void setSize(Size aSize) {
        currentScreenSize = aSize;
    }
}