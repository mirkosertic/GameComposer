package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.generic.CSSUtils;
import de.mirkosertic.gameengine.generic.GenericAbstractGameView;
import de.mirkosertic.gameengine.teavm.pixi.DisplayObject;
import de.mirkosertic.gameengine.teavm.pixi.Graphics;
import de.mirkosertic.gameengine.teavm.pixi.Renderer;
import de.mirkosertic.gameengine.teavm.pixi.Sprite;
import de.mirkosertic.gameengine.teavm.pixi.Stage;
import de.mirkosertic.gameengine.teavm.pixi.Style;
import de.mirkosertic.gameengine.teavm.pixi.Text;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TeaVMGameView extends GenericAbstractGameView<GameResource> {

    private final Renderer renderer;
    private final Map<String, DisplayObject> instances;
    private Stage stage;
    private InstanceCache instanceCache;

    public TeaVMGameView(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector,
            Renderer aRenderer) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
        renderer = aRenderer;
        instances = new HashMap<>();
        stage = Stage.createStage(0);
        instanceCache = new InstanceCache(stage);
    }

    @Override
    public void prepareNewScene(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector) {
        super.prepareNewScene(aGameRuntime, aCameraBehavior, aGestureDetector);

        for (Map.Entry<String, DisplayObject> theEntry : instances.entrySet()) {
            theEntry.getValue().destroy();
        }
        instances.clear();
        stage.destroy();
        stage = Stage.createStage(0);
        instanceCache = new InstanceCache(stage);
    }

    @Override
    protected boolean beginFrame(GameScene aScene) {
        renderer.setBackgroundColor(CSSUtils.toInt(aScene.backgroundColorProperty().get()));
        instanceCache.clearTouchedInstances();
        return true;
    }

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new TeaVMEffectCanvas(instanceCache, renderer);
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset,
            Angle aRotation) {
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, final Position aCenterOffset,
            final GameResource aResource) {

        String theInstanceID = aInstance.uuidProperty().get();
        Sprite theCurrentObject = instanceCache.getOrCreate(theInstanceID, new InstanceCache.Producer<Sprite>() {
            @Override
            public Sprite create() {
                TeaVMTextureResource theTexture = (TeaVMTextureResource) aResource;
                Sprite theSprite =  Sprite.createSprite(theTexture.getTexture());
                theSprite.getScale().set(1, 1);
                theSprite.getPivot().set(aCenterOffset.x, aCenterOffset.y);
                return theSprite;
            }
        }, 0);

        // Update the position and all the other stuff
        theCurrentObject.getPosition().set(aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        theCurrentObject.setRotation(aInstance.rotationAngleProperty().get().toRadians());
    }

    @Override
    protected void drawText(String aInstanceID, Position aPosition, Angle aAngle, final Position aCenterOffset, Font aFont, Color aColor, final String aText,
            Size aSize) {

        Text theCurrentObject = instanceCache.getOrCreate(aInstanceID, new InstanceCache.Producer<Text>() {
            @Override
            public Text create() {
                Text theText = Text.createText(aText);
                theText.getScale().set(1, 1);
                theText.getPivot().set(aCenterOffset.x, aCenterOffset.y);
                return theText;
            }
        }, 0);

        Style theStyle = theCurrentObject.getStyle();
        theStyle.setFont(CSSUtils.toFont(aFont));
        theStyle.setFill(CSSUtils.toColor(aColor));
        theStyle.setStroke(CSSUtils.toColor(aColor));
        theCurrentObject.setText(aText);

        // Update the position and all the other stuff
        theCurrentObject.getPosition().set(aPosition.x + aCenterOffset.x, aPosition.y + aCenterOffset.y);
        theCurrentObject.setRotation(aAngle.toRadians());
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, final Position aCenterOffset, final Color aColor,
            final Size aSize) {

        String theInstanceID = aInstance.uuidProperty().get();
        Graphics theCurrentObject = instanceCache.getOrCreate(theInstanceID, new InstanceCache.Producer<Graphics>() {
            @Override
            public Graphics create() {
                Graphics theCurrentObject = Graphics.createGraphics();
                theCurrentObject.getScale().set(1, 1);
                theCurrentObject.getPivot().set(aCenterOffset.x, aCenterOffset.y);
                theCurrentObject.setWidth(aSize.width);
                theCurrentObject.setHeight(aSize.height);
                theCurrentObject.lineStyle(1, CSSUtils.toInt(aColor), 1f);
                theCurrentObject.drawRect(0, 0, aSize.width, aSize.height);
                return theCurrentObject;
            }
        }, 0);

        // Update the position and all the other stuff
        theCurrentObject.getPosition().set(aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        theCurrentObject.setRotation(aInstance.rotationAngleProperty().get().toRadians());
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
    }

    @Override
    protected void framefinished() {
        super.framefinished();

        // Remove no longer visible instances
        instanceCache.keepOnlyTouched();

        // Now we sort by ZIndex
        List<DisplayObject> theChildren = new ArrayList<>();
        DisplayObject[] theObjects = stage.getChildren();
        for (DisplayObject theEntry : theObjects) {
            theChildren.add(theEntry);
            stage.removeChild(theEntry);
        }

        for (int i=0;i<theObjects.length;i++)  {
            for (int j=i+1;j<theObjects.length;j++) {
                if (theObjects[i].getZOrder() < theObjects[j].getZOrder()) {
                    DisplayObject theTemp = theObjects[i];
                    theObjects[i] = theObjects[j];
                    theObjects[j] = theTemp;
                }
            }
        }

        for (DisplayObject theEntry : theObjects) {
            stage.addChild(theEntry);
        }

        renderer.render(stage);
    }

    @Override
    protected void logError(String aMessage) {
        TeaVMLogger.error(aMessage);
    }

    public void setSize(Size aSize) {
        super.setCurrentScreenSize(aSize);

        renderer.resize(aSize.width, aSize.height);
    }
}