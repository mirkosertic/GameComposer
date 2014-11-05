package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.type.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dragome.services.ServiceLocator;
import org.w3c.dom.NodeList;

class DragomeGameView implements GameView {

    private Size currentScreenSize;

    private final CameraBehavior cameraComponent;
    private final GestureDetector gestureDetector;
    private final GameRuntime gameRuntime;

    private final Map<String, Element> instanceCache;

    public DragomeGameView(GameRuntime aGameRuntime, CameraBehavior aCameraComponent, GestureDetector aGestureDetector) {
        cameraComponent = aCameraComponent;
        gestureDetector = aGestureDetector;
        gameRuntime = aGameRuntime;
        instanceCache = new HashMap<>();
    }

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene) {

        Document theDocument = ServiceLocator.getInstance().getDomHandler().getDocument();
        Element theCanvas = ServiceLocator.getInstance().getDomHandler().getElementBySelector("#canvas");

        //DragomeLogger.info("Rendering scene");

        Color theBGColor = aScene.backgroundColorProperty().get();
        theCanvas.setAttribute("style","width: "+currentScreenSize.width+"px; height: "+currentScreenSize.height+"px; background-color: rgb("+theBGColor.r+","+theBGColor.g+","+theBGColor.b+"); ");

        for (GameObjectInstance theInstance : cameraComponent.getObjectsToDrawInRightOrder(aScene)) {

            Position thePosition = cameraComponent.transformToScreenPosition(theInstance);

            Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();

            float theHalfWidth = theSize.width / 2;
            float theHalfHeight = theSize.height / 2;

            Angle theAngle = theInstance.rotationAngleProperty().get();
            int theAngleInDegrees = theAngle.angleInDegrees;

            String theInstanceName = theInstance.nameProperty().get();
            Element theInstanceElement = instanceCache.get(theInstanceName);
            if (theInstanceElement == null) {
                theInstanceElement = theDocument.createElement("div");
                theInstanceElement.setAttribute("name", theInstanceName);
                instanceCache.put(theInstanceName, theInstanceElement);
                theCanvas.appendChild(theInstanceElement);
            }

            // Cleanup the node
            NodeList theChildren = theInstanceElement.getChildNodes();
            for (int i=0;i<theChildren.getLength();i++) {
                theInstanceElement.removeChild(theChildren.item(i));
            }
            theInstanceElement.setNodeValue("");

            boolean theSomethingRendered = false;
            Sprite theSpriteComponent = theInstance.getComponent(SpriteBehavior.class);
            if (theSpriteComponent != null) {
                theInstanceElement.setAttribute("style",
                        "position: absolute; top: " + ((int) thePosition.y) + "px; left: " + ((int) thePosition.x) + "px; width: "
                                + theSize.width + "px; height: " + theSize.height + "px;");

                ResourceName theSpriteResource = theSpriteComponent.resourceNameProperty().get();
                try {
                    DragomeGameResource theGameResource = gameRuntime.getResourceCache()
                            .getResourceFor(theSpriteResource);

                    Element theImg = theDocument.createElement("img");
                    theImg.setAttribute("src", theGameResource.getName());
                    theImg.setAttribute("width", "" + theSize.width);
                    theImg.setAttribute("height", "" + theSize.height);
                    theInstanceElement.appendChild(theImg);

                    theSomethingRendered = true;
                } catch (IOException e) {
                    DragomeLogger.error("Fehler beim laden von "+theSpriteResource.name+" : " + e);
                }
            }
            Text theTextComponent = theInstance.getComponent(TextBehavior.class);
            if (theTextComponent != null) {
                Color theFontColor = theTextComponent.colorProperty().get();
                Font theFont = theTextComponent.fontProperty().get();
                ExpressionParser theExpressionParser = aScene.get(theTextComponent.textExpressionProperty().get());
                theInstanceElement.setAttribute("style",
                        "position: absolute; top: " + ((int) thePosition.y) + "px; left: " + ((int) thePosition.x) + "px; color: rgb("+theFontColor.r+","+theFontColor.g+","+theFontColor.b+"); font-size: "+theFont.size+"px;");

                theInstanceElement.appendChild(theDocument.createTextNode(theExpressionParser.evaluateToString()));

                theSomethingRendered = true;
            }

            if (!theSomethingRendered) {
                theInstanceElement.setAttribute("style",
                        "position: absolute; top: " + ((int) thePosition.y) + "px; left: " + ((int) thePosition.x) + "px; width: "
                                + theSize.width + "px; height: " + theSize.height + "px; border: 1px solid white;");
            }
        }

        //DragomeLogger.info("Rendering scene done");
    }

    @Override
    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    public void setSize(Size aSize) {
        currentScreenSize = aSize;
    }
}