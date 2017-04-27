/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.starfield;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSceneEffect;
import de.mirkosertic.gameengine.core.GameSceneEffectType;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.process.GameProcess;
import de.mirkosertic.gameengine.process.StartProcess;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

import java.util.HashMap;
import java.util.Map;

public class StarfieldGameSceneEffect implements GameSceneEffect {

    public static class Star {

        private Position position;
        private String id;

        public Star(Position aPosition, String aId) {
            position = aPosition;
            id = aId;
        }

        public void evolvePosition(float theMovement, int aWidth) {
            position = position.changeX((position.x + theMovement) % aWidth);
        }
    }

    private static final StarfieldGameSceneEffectClassInformation CIINSTANCE = new StarfieldGameSceneEffectClassInformation();

    public static final String TYPE_VALUE = "StarfieldGameSceneEffect";

    public static final String NUMBER_OF_STARS_PROPERTY = "numberOfStars";
    public static final String STAR_SPEED_PROPERTY = "starspeed";
    public static final String COLOR_PROPERTY = "numberOfStars";

    private final GameScene scene;

    private final Property<Integer> numberOfStars;
    private final Property<Color> color;
    private final Property<Float> starSpeed;

    private Star[] stars;

    private Size currentSize;

    public StarfieldGameSceneEffect(GameScene aParent, GameEventManager aEventManager) {

        scene = aParent;

        stars = new Star[0];

        numberOfStars = new Property<>(Integer.class, this, NUMBER_OF_STARS_PROPERTY, 30, aEventManager);
        starSpeed = new Property<>(Float.class, this, STAR_SPEED_PROPERTY, 7f, aEventManager);
        color = new Property<>(Color.class, this, COLOR_PROPERTY, Color.WHITE, aEventManager);

        aEventManager.register(this, SetScreenResolution.TYPE, new GameEventListener<SetScreenResolution>() {
            @Override
            public void handleGameEvent(SetScreenResolution aEvent) {
                setScreenSize(aEvent.screenSize);
            }
        });
        aEventManager.fire(new StartProcess(new StarfieldGameProcess(this)));
    }

    @Override
    public GameScene getScene() {
        return scene;
    }

    @Override
    public GameSceneEffectType getEffectType() {
        return GameSceneEffectType.PREPROCESSOR;
    }

    @ReflectiveField
    public Property<Integer> numberofStars() {
        return numberOfStars;
    }

    @ReflectiveField
    public Property<Float> starSpeed() {
        return starSpeed;
    }

    @ReflectiveField
    public Property<Color> color() {
        return color;
    }

    @Override
    public StarfieldGameSceneEffectClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public void render(EffectCanvas aEffectCanvas, CameraBehavior aCameraBehavior) {
        Color theStarColor = color.get();
        for (int i=0;i<stars.length;i++) {
            Position thePosition = stars[i].position;
            aEffectCanvas.drawSingleDot(stars[i].id, thePosition, theStarColor, 0);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put("numberOfStars", Integer.toString(numberOfStars.get()));
        theResult.put("starSpeed", Float.toString(starSpeed.get()));
        theResult.put("color", color.get().serialize());
        return theResult;
    }

    public GameProcess.ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
        float theMovement = starSpeed.get() / 1000 * aElapsedTimeSinceLastLoop;

        for (int i=0;i<stars.length;i++) {
            stars[i].evolvePosition(theMovement, currentSize.width);
        }

        return GameProcess.ProceedResult.CONTINUE_RUNNING;
    }

    private void setScreenSize(Size aSize) {
        Star[] theStars = new Star[numberOfStars.get()];
        long theMaximum = aSize.width * aSize.height;
        for (int i=0;i<numberOfStars.get();i++) {
            long theRandonPos = (long)(Math.random() * theMaximum);
            long theY = (int)(theRandonPos / aSize.width);
            long theX = theRandonPos % aSize.width;
            theStars[i] = new Star(new Position(theX, theY), "s" + i);
        }
        stars = theStars;
        currentSize = aSize;
    }

    public static StarfieldGameSceneEffect unmarshall(GameRuntime aGameRuntime, GameScene aScene, Map<String, Object> aObjectData) {
        StarfieldGameSceneEffect theResult = new StarfieldGameSceneEffect(aScene, aGameRuntime.getEventManager());
        theResult.numberOfStars.setQuietly(Integer.valueOf((String) (aObjectData.get("numberOfStars"))));
        theResult.starSpeed.setQuietly(Float.valueOf((String) (aObjectData.get("starSpeed"))));

        Map<String,Object> theColor = (Map<String, Object>) aObjectData.get("color");
        theResult.color.setQuietly(Color.deserialize(theColor));

        return theResult;
    }

    @Override
    public void delete() {
        scene.removeEffect(this);
    }
}