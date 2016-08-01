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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class TeaVMGameView extends GenericAbstractGameView<GameResource> {

    private final Renderer renderer;
    private final Map<String, DisplayObject> instances;
    private final Set<String> touchedInstances;
    private Stage stage;

    public TeaVMGameView(GameRuntime aGameRuntime, CameraBehavior aCameraBehavior, GestureDetector aGestureDetector,
            Renderer aRenderer) {
        super(aGameRuntime, aCameraBehavior, aGestureDetector);
        touchedInstances = new HashSet<>();
        renderer = aRenderer;
        instances = new HashMap<>();
        stage = Stage.createStage(0);
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
    }

    @Override
    protected boolean beginFrame(GameScene aScene) {
        renderer.setBackgroundColor(CSSUtils.toInt(aScene.backgroundColorProperty().get()));
        touchedInstances.clear();
        return true;
    }

    @Override
    protected EffectCanvas createEffectCanvas() {
        return new TeaVMEffectCanvas(null);
    }

    @Override
    protected void beforeInstance(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset,
            Angle aRotation) {
    }

    @Override
    protected void drawImage(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset,
            GameResource aResource) {

        String theInstanceID = aInstance.uuidProperty().get();
        Sprite theCurrentObject = (Sprite) instances.get(theInstanceID);
        if (theCurrentObject == null) {
            TeaVMTextureResource theTexture = (TeaVMTextureResource) aResource;
            theCurrentObject = Sprite.createSprite(theTexture.getTexture());
            instances.put(theInstanceID, theCurrentObject);
            theCurrentObject.getScale().set(1, 1);
            theCurrentObject.getPivot().set(aCenterOffset.x, aCenterOffset.y);
            stage.addChild(theCurrentObject);
        }
        // Update the position and all the other stuff
        theCurrentObject.getPosition().set(aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        theCurrentObject.setRotation(aInstance.rotationAngleProperty().get().toRadians());

        touchedInstances.add(theInstanceID);
    }

    @Override
    protected void drawText(String aInstanceID, Position aPosition, Angle aAngle, Position aCenterOffset, Font aFont, Color aColor, String aText,
            Size aSize) {

        Text theCurrentObject = (Text) instances.get(aInstanceID);
        if (theCurrentObject == null) {
            theCurrentObject = Text.createText(aText);
            instances.put(aInstanceID, theCurrentObject);
            theCurrentObject.getScale().set(1, 1);
            theCurrentObject.getPivot().set(aCenterOffset.x, aCenterOffset.y);
            stage.addChild(theCurrentObject);
        }

        Style theStyle = theCurrentObject.getStyle();
        theStyle.setFont(CSSUtils.toFont(aFont));
        theStyle.setFill(CSSUtils.toColor(aColor));
        theStyle.setStroke(CSSUtils.toColor(aColor));
        theCurrentObject.setText(aText);

        // Update the position and all the other stuff
        theCurrentObject.getPosition().set(aPosition.x + aCenterOffset.x, aPosition.y + aCenterOffset.y);
        theCurrentObject.setRotation(aAngle.toRadians());

        touchedInstances.add(aInstanceID);
    }

    @Override
    protected void drawRect(GameObjectInstance aInstance, Position aPositionOnScreen, Position aCenterOffset, Color aColor,
            Size aSize) {

        String theInstanceID = aInstance.uuidProperty().get();
        Graphics theCurrentObject = (Graphics) instances.get(theInstanceID);
        if (theCurrentObject == null) {
            theCurrentObject = Graphics.createGraphics();
            instances.put(theInstanceID, theCurrentObject);
            theCurrentObject.getScale().set(1, 1);
            theCurrentObject.getPivot().set(aCenterOffset.x, aCenterOffset.y);
            theCurrentObject.setWidth(aSize.width);
            theCurrentObject.setHeight(aSize.height);

            theCurrentObject.lineStyle(1, CSSUtils.toInt(aColor), 1f);
            theCurrentObject.drawRect(0, 0, aSize.width, aSize.height);

            stage.addChild(theCurrentObject);
        }
        // Update the position and all the other stuff
        theCurrentObject.getPosition().set(aPositionOnScreen.x + aCenterOffset.x, aPositionOnScreen.y + aCenterOffset.y);
        theCurrentObject.setRotation(aInstance.rotationAngleProperty().get().toRadians());

        touchedInstances.add(theInstanceID);
    }

    @Override
    protected void afterInstance(GameObjectInstance aInstance, Position aPositionOnScreen) {
    }

    @Override
    protected void framefinished() {
        super.framefinished();

        // Remove no longer visible instances
        Set<String> theRemovedInstances = new HashSet<>();
        for (Map.Entry<String, DisplayObject> theEntry : instances.entrySet()) {
            if (!touchedInstances.contains(theEntry.getKey())) {
                theRemovedInstances.add(theEntry.getKey());
                stage.removeChild(theEntry.getValue());
            }
        }

        for (String theEntry : theRemovedInstances) {
            instances.remove(theEntry);
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