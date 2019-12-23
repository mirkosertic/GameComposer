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
package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.arcade.ConstantMovement;
import de.mirkosertic.gameengine.arcade.ConstantMovementBehavior;
import de.mirkosertic.gameengine.arcade.ConstantMovementBehaviorTemplate;
import de.mirkosertic.gameengine.arcaderacer.ArcadeRacerGameSceneEffect;
import de.mirkosertic.gameengine.camera.Camera;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.CameraBehaviorTemplate;
import de.mirkosertic.gameengine.camera.CameraType;
import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSceneEffect;
import de.mirkosertic.gameengine.core.Spritesheet;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.physic.Physics;
import de.mirkosertic.gameengine.physic.PhysicsBehavior;
import de.mirkosertic.gameengine.physic.PhysicsBehaviorTemplate;
import de.mirkosertic.gameengine.physic.Platform;
import de.mirkosertic.gameengine.physic.PlatformBehavior;
import de.mirkosertic.gameengine.physic.PlatformBehaviorTemplate;
import de.mirkosertic.gameengine.physic.Static;
import de.mirkosertic.gameengine.physic.StaticBehavior;
import de.mirkosertic.gameengine.physic.StaticBehaviorTemplate;
import de.mirkosertic.gameengine.playerscore.PlayerScore;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehavior;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehaviorTemplate;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import de.mirkosertic.gameengine.starfield.StarfieldGameSceneEffect;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.text.TextBehaviorTemplate;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.PositionAnchor;
import de.mirkosertic.gameengine.type.ScoreValue;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.Speed;
import de.mirkosertic.gameengine.type.TextExpression;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.HashMap;
import java.util.Map;

public class GameObjectEditor extends ListingElement {

    public static class FloatStringConverter implements ObjectConverter<Float> {

        @Override
        public String convertFrom(Float aValue) {
            return Float.toString(aValue);
        }

        @Override
        public Float convertTo(String aValue) {
            return Float.parseFloat(aValue);
        }
    }

    public static class DoubleStringConverter implements ObjectConverter<Double> {

        @Override
        public String convertFrom(Double aValue) {
            return Double.toString(aValue);
        }

        @Override
        public Double convertTo(String aValue) {
            return Double.parseDouble(aValue);
        }
    }

    public static class StringStringConverter implements ObjectConverter<String> {

        @Override
        public String convertFrom(String aValue) {
            return aValue;
        }

        @Override
        public String convertTo(String aValue) {
            return aValue;
        }
    }

    public static class IntegerStringConverter implements ObjectConverter<Integer> {

        @Override
        public String convertFrom(Integer aValue) {
            return Integer.toString(aValue);
        }

        @Override
        public Integer convertTo(String aValue) {
            return Integer.parseInt(aValue);
        }
    }

    interface ObjectEditorHelper<T> {

        boolean contains(T aObject);

        void addOptionEditor(T aObject);

        void create(T aObject);
    }


    private final Map<String, ObjectEditorHelper<GameObject>> availableTemplates;
    private final Map<String, ObjectEditorHelper<GameScene>> availableEffects;

    public GameObjectEditor(HTMLElement aHtmlElement) {
        super(aHtmlElement);

        availableTemplates = new HashMap<>();
        availableTemplates.put("Camera", new ObjectEditorHelper<GameObject>() {
            @Override
            public boolean contains(GameObject aObject) {
                return aObject.getBehaviorTemplate(CameraBehaviorTemplate.TYPE) != null;
            }

            @Override
            public void addOptionEditor(GameObject aObject) {
                addSubComponent((CameraBehaviorTemplate) aObject.getBehaviorTemplate(CameraBehaviorTemplate.TYPE), true);
            }

            @Override
            public void create(GameObject aObject) {
                aObject.getGameScene().addBehaviorToObject(aObject, new CameraBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(),
                        aObject));
                addOptionEditor(aObject);
            }
        });
        availableTemplates.put("Player", new ObjectEditorHelper<GameObject>() {
            @Override
            public boolean contains(GameObject aObject) {
                return aObject.getBehaviorTemplate(PlayerScoreBehaviorTemplate.TYPE) != null;
            }

            @Override
            public void addOptionEditor(GameObject aObject) {
                addSubComponent((PlayerScoreBehaviorTemplate) aObject.getBehaviorTemplate(PlayerScoreBehaviorTemplate.TYPE), true);
            }

            @Override
            public void create(GameObject aObject) {
                aObject.getGameScene().addBehaviorToObject(aObject, new PlayerScoreBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(),
                        aObject));
                addOptionEditor(aObject);
            }
        });
        availableTemplates.put("Static", new ObjectEditorHelper<GameObject>() {
            @Override
            public boolean contains(GameObject aObject) {
                return aObject.getBehaviorTemplate(StaticBehaviorTemplate.TYPE) != null;
            }

            @Override
            public void addOptionEditor(GameObject aObject) {
                addSubComponent((StaticBehaviorTemplate) aObject.getBehaviorTemplate(StaticBehaviorTemplate.TYPE), true);
            }

            @Override
            public void create(GameObject aObject) {
                aObject.getGameScene().addBehaviorToObject(aObject, new StaticBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(),
                        aObject));
                addOptionEditor(aObject);
            }
        });
        availableTemplates.put("Text", new ObjectEditorHelper<GameObject>() {
            @Override
            public boolean contains(GameObject aObject) {
                return aObject.getBehaviorTemplate(TextBehaviorTemplate.TYPE) != null;
            }

            @Override
            public void addOptionEditor(GameObject aObject) {
                addSubComponent((TextBehaviorTemplate) aObject.getBehaviorTemplate(TextBehaviorTemplate.TYPE), true);
            }

            @Override
            public void create(GameObject aObject) {
                aObject.getGameScene().addBehaviorToObject(aObject, new TextBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(),
                        aObject));
                addOptionEditor(aObject);
            }
        });
        availableTemplates.put("Platformer", new ObjectEditorHelper<GameObject>() {
            @Override
            public boolean contains(GameObject aObject) {
                return aObject.getBehaviorTemplate(PlatformBehaviorTemplate.TYPE) != null;
            }

            @Override
            public void addOptionEditor(GameObject aObject) {
                addSubComponent((PlatformBehaviorTemplate) aObject.getBehaviorTemplate(PlatformBehaviorTemplate.TYPE), true);
            }

            @Override
            public void create(GameObject aObject) {
                aObject.getGameScene().addBehaviorToObject(aObject, new PlatformBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(),
                        aObject));
                addOptionEditor(aObject);
            }
        });
        availableTemplates.put("Constant movement", new ObjectEditorHelper<GameObject>() {
            @Override
            public boolean contains(GameObject aObject) {
                return aObject.getBehaviorTemplate(ConstantMovementBehaviorTemplate.TYPE) != null;
            }

            @Override
            public void addOptionEditor(GameObject aObject) {
                addSubComponent((ConstantMovementBehaviorTemplate) aObject.getBehaviorTemplate(ConstantMovementBehaviorTemplate.TYPE), true);
            }

            @Override
            public void create(GameObject aObject) {
                aObject.getGameScene().addBehaviorToObject(aObject, new ConstantMovementBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(),
                        aObject));
                addOptionEditor(aObject);
            }
        });
        availableTemplates.put("Physics", new ObjectEditorHelper<GameObject>() {
            @Override
            public boolean contains(GameObject aObject) {
                return aObject.getBehaviorTemplate(PhysicsBehaviorTemplate.TYPE) != null;
            }

            @Override
            public void addOptionEditor(GameObject aObject) {
                addSubComponent((PhysicsBehaviorTemplate) aObject.getBehaviorTemplate(PhysicsBehaviorTemplate.TYPE), true);
            }

            @Override
            public void create(GameObject aObject) {
                aObject.getGameScene().addBehaviorToObject(aObject, new PhysicsBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(),
                        aObject));
                addOptionEditor(aObject);
            }
        });
        availableTemplates.put("Sprite", new ObjectEditorHelper<GameObject>() {
            @Override
            public boolean contains(GameObject aObject) {
                return aObject.getBehaviorTemplate(SpriteBehaviorTemplate.TYPE) != null;
            }

            @Override
            public void addOptionEditor(GameObject aObject) {
                addSubComponent((SpriteBehaviorTemplate) aObject.getBehaviorTemplate(SpriteBehaviorTemplate.TYPE),
                        aObject.getGameScene(), true);
            }

            @Override
            public void create(GameObject aObject) {
                aObject.getGameScene().addBehaviorToObject(aObject, new SpriteBehaviorTemplate(aObject.getGameScene().getRuntime().getEventManager(),
                        aObject));
                addOptionEditor(aObject);
            }
        });

        availableEffects = new HashMap<>();
        availableEffects.put("Starfield", new ObjectEditorHelper<GameScene>() {
            @Override
            public boolean contains(GameScene aObject) {
                for (GameSceneEffect theEffect : aObject.getPreprocessorEffects()) {
                    if (theEffect instanceof StarfieldGameSceneEffect) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void addOptionEditor(GameScene aObject) {
                for (GameSceneEffect theEffect : aObject.getPreprocessorEffects()) {
                    if (theEffect instanceof StarfieldGameSceneEffect) {
                        addComponent((StarfieldGameSceneEffect) theEffect);
                    }
                }
            }

            @Override
            public void create(GameScene aObject) {
                StarfieldGameSceneEffect theEffect = new StarfieldGameSceneEffect(aObject, aObject.getRuntime().getEventManager());
                aObject.addEffect(theEffect);
                addOptionEditor(aObject);
            }
        });
        availableEffects.put("Arcade racer", new ObjectEditorHelper<GameScene>() {
            @Override
            public boolean contains(GameScene aObject) {
                for (GameSceneEffect theEffect : aObject.getPreprocessorEffects()) {
                    if (theEffect instanceof ArcadeRacerGameSceneEffect) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void addOptionEditor(GameScene aObject) {
                for (GameSceneEffect theEffect : aObject.getPreprocessorEffects()) {
                    if (theEffect instanceof ArcadeRacerGameSceneEffect) {
                        addComponent((ArcadeRacerGameSceneEffect) theEffect);
                    }
                }
            }

            @Override
            public void create(GameScene aObject) {
                ArcadeRacerGameSceneEffect theEffect = new ArcadeRacerGameSceneEffect(aObject, aObject.getRuntime().getEventManager());
                aObject.addEffect(theEffect);
                addOptionEditor(aObject);
            }
        });
    }

    private void selectParentOf(Object aDeletedObject) {
        if (aDeletedObject instanceof Behavior) {
            Behavior theBehavior = (Behavior) aDeletedObject;
            setEditingObject(theBehavior.getInstance());
        }
        if (aDeletedObject instanceof BehaviorTemplate) {
            BehaviorTemplate theTemplate = (BehaviorTemplate) aDeletedObject;
            setEditingObject(theTemplate.getOwner());
        }
    }

    public void setEditingObject(Game aObject) {
        clear();
        addTitleLevel1("Game");
        addTitleLevel2("Common properties");
        addTextInputfieldPropertyEditor("Name", aObject.nameProperty(), new StringStringConverter());
        addBooleanPropertyEditor("Enable Debug", aObject.enableDebugProperty());
        addBooleanPropertyEditor("Enable WebGL", aObject.enableWebGLProperty());
        addBooleanPropertyEditor("Enable Networking", aObject.enableNetworkingProperty());
        addTextInputfieldPropertyEditor("Firebase URL", aObject.fireBaseURLProperty(), new StringStringConverter());
        addSelectionEditor("Default Scene", aObject.defaultSceneProperty(), aObject.getKnownScenes());
    }

    public void setEditingObject(GameScene aObject) {
        clear();
        GlobalSeparatorHTMLElement theEditor = addTitleLevel1("Game Scene");
        addTitleLevel2("Common properties");
        addTextInputfieldPropertyEditor("Name", aObject.nameProperty(), new StringStringConverter());
        addColorPropertyEditor("Background color", aObject.backgroundColorProperty());
        addGameObjectSelectEditor("Camera", aObject.cameraObjectProperty(), aObject.getObjects());
        addGameObjectSelectEditor("Player", aObject.defaultPlayerProperty(), aObject.getObjects());

        initializeAndBuildContextMenu(theEditor, aObject, availableEffects);
    }

    public void setEditingObject(GameObject aObject) {
        clear();
        GlobalSeparatorHTMLElement theEditor = addTitleLevel1("Game Object");
        addTitleLevel2("Common properties");
        addTextInputfieldPropertyEditor("Name", aObject.nameProperty(), new StringStringConverter());
        addSizePropertyEditor(aObject.sizeProperty());
        addBooleanPropertyEditor("Visible", aObject.visibleProperty());

        initializeAndBuildContextMenu(theEditor, aObject, availableTemplates);
    }

    public void setEditingObject(Spritesheet aSheet) {
        clear();
        addTitleLevel1("Sprite Sheet");
        addTitleLevel2("Common properties");
        addTextInputfieldPropertyEditor("Name", aSheet.nameProperty(), new StringStringConverter());
    }

    public void setEditingObject(EventSheet aObject) {
        clear();
        addTitleLevel1("Event Sheet");
        addTitleLevel2("Common properties");
        addTextInputfieldPropertyEditor("Name", aObject.nameProperty(), new StringStringConverter());
    }

    public void setEditingObject(GameObjectInstance aObject) {
        clear();
        addTitleLevel1("Game Object Instance");
        addTitleLevel2("Common properties");
        addTextInputfieldPropertyEditor("Name", aObject.nameProperty(), new StringStringConverter());
        addPositionPropertyEditor(aObject.positionProperty());
        addBooleanPropertyEditor("Visible", aObject.visibleProperty());
        addTextInputfieldPropertyEditor("Rotation", aObject.rotationAngleProperty(), new ObjectConverter<Angle>() {

            @Override
            public String convertFrom(Angle aValue) {
                return Integer.toString(aValue.angleInDegrees);
            }

            @Override
            public Angle convertTo(String aValue) {
                return new Angle(Integer.valueOf(aValue));
            }
        });
        addSelectionEditor("Anchor", aObject.positionAnchorProperty(), PositionAnchor.values());

        addSubComponent((CameraBehavior) aObject.getBehavior(CameraBehavior.TYPE), false);
        addSubComponent((PlayerScoreBehavior) aObject.getBehavior(PlayerScoreBehavior.TYPE), false);
        addSubComponent((StaticBehavior) aObject.getBehavior(StaticBehavior.TYPE), false);
        addSubComponent((TextBehavior) aObject.getBehavior(TextBehavior.TYPE), false);
        addSubComponent((PlatformBehavior) aObject.getBehavior(PlatformBehavior.TYPE), false);
        addSubComponent((ConstantMovementBehavior) aObject.getBehavior(ConstantMovementBehavior.TYPE), false);
        addSubComponent((PhysicsBehavior) aObject.getBehavior(PhysicsBehavior.TYPE), false);
        addSubComponent(aObject.getBehavior(SpriteBehavior.TYPE), aObject.getOwnerGameObject().getGameScene(), false);
    }

    public <T> void initializeAndBuildContextMenu(GlobalSeparatorHTMLElement aEditor, T aObject, Map<String, ObjectEditorHelper<T>> aOptions) {

        for (Map.Entry<String, ObjectEditorHelper<T>> theEntry : aOptions.entrySet()) {
            theEntry.getValue().addOptionEditor(aObject);
        }

        aEditor.addContextMenuListener(aClickEvent -> {
            ContextMenuHTMLElement theContextMenu = ContextMenuHTMLElement.create();
            theContextMenu.setTitle("Add new");

            for (Map.Entry<String, ObjectEditorHelper<T>> theEntry : aOptions.entrySet()) {
                if (!theEntry.getValue().contains(aObject)) {
                    ContextMenuItemHTMLElement theItem = ContextMenuItemHTMLElement.create();
                    theItem.setText(theEntry.getKey());
                    theItem.addEventListener("click", aEvent -> {
                        theContextMenu.hide();
                        theEntry.getValue().create(aObject);
                    }, false);
                    theContextMenu.add(theItem);
                }
            }

            theContextMenu.showAt(aClickEvent);
        });
    }

    public void addComponent(StarfieldGameSceneEffect aEffect) {
        GlobalSeparatorHTMLElement theElement = addTitleLevel2("Starfield effect");
        addTextInputfieldPropertyEditor("Number of stars", aEffect.numberofStars(), new IntegerStringConverter());
        addColorPropertyEditor("Star color", aEffect.color());
        addTextInputfieldPropertyEditor("Star movement speed", aEffect.starSpeed(), new FloatStringConverter());
        theElement.addDeleteListener((aEvent) -> {
            aEffect.delete();
            setEditingObject(aEffect.getScene());

            Toast.info("Behavior deleted.");
        });
    }

    public void addComponent(ArcadeRacerGameSceneEffect aEffect) {
        GlobalSeparatorHTMLElement theElement = addTitleLevel2("Arcade racer");
        addTextInputfieldPropertyEditor("Speed", aEffect.speed(), new DoubleStringConverter());
        addTextInputfieldPropertyEditor("Field of view", aEffect.fieldOfView(), new IntegerStringConverter());
        addTextInputfieldPropertyEditor("View depth", aEffect.viewDepth(), new IntegerStringConverter());
        addTextInputfieldPropertyEditor("Distance to camera", aEffect.distanceCamera(), new IntegerStringConverter());
        addTextInputfieldPropertyEditor("Camera height", aEffect.cameraHeight(), new DoubleStringConverter());
        theElement.addDeleteListener((aEvent) -> {
            aEffect.delete();
            setEditingObject(aEffect.getScene());

            Toast.info("Behavior deleted.");
        });
    }

    private void addSubComponent(Camera aComponent, boolean aDeletable) {
        if (aComponent == null) {
            return;
        }
        GlobalSeparatorHTMLElement theElement = addTitleLevel2("Camera");
        addSelectionEditor("Type", aComponent.typeProperty(), CameraType.values());
        if (aDeletable) {
            theElement.addDeleteListener((aEvent) -> {
                aComponent.delete();
                selectParentOf(aComponent);

                Toast.info("Behavior deleted.");
            });
        }
    }

    private void addSubComponent(Static aComponent, boolean aDeletable) {
        if (aComponent == null) {
            return;
        }
        GlobalSeparatorHTMLElement theElement = addTitleLevel2("Static");
        if (aDeletable) {
            theElement.addDeleteListener((aEvent) -> {
                aComponent.delete();
                selectParentOf(aComponent);

                Toast.info("Behavior deleted.");
            });
        }
    }

    private void addSubComponent(Text aComponent, boolean aDeletable) {
        if (aComponent == null) {
            return;
        }
        GlobalSeparatorHTMLElement theElement = addTitleLevel2("Text");
        addFontPropertyEditor(aComponent.fontProperty());
        addBooleanPropertyEditor("Is LUA Script", aComponent.isScriptProperty());
        addLongStringPropertyEditor("Text Expression", aComponent.textExpressionProperty(),
            new ObjectConverter<TextExpression>() {
                @Override
                public String convertFrom(TextExpression aValue) {
                    return aValue.expression;
                }

                @Override
                public TextExpression convertTo(String aValue) {
                    return new TextExpression(aValue);
                }
            });
        addColorPropertyEditor("Text color", aComponent.colorProperty());
        if (aDeletable) {
            theElement.addDeleteListener((aEvent) -> {
                aComponent.delete();
                selectParentOf(aComponent);

                Toast.info("Behavior deleted.");
            });
        }
    }

    private void addSubComponent(PlayerScore aComponent, boolean aDeletable) {
        if (aComponent == null) {
            return;
        }
        GlobalSeparatorHTMLElement theElement = addTitleLevel2("Player score");
        addTextInputfieldPropertyEditor("Score", aComponent.scoreValueProperty(), new ObjectConverter<ScoreValue>() {
            @Override
            public String convertFrom(ScoreValue aValue) {
                return Long.toString(aValue.score);
            }

            @Override
            public ScoreValue convertTo(String aValue) {
                return new ScoreValue(Long.parseLong(aValue));
            }
        });
        if (aDeletable) {
            theElement.addDeleteListener((aEvent) -> {
                aComponent.delete();
                selectParentOf(aComponent);

                Toast.info("Behavior deleted.");
            });
        }
    }

    private void addSubComponent(ConstantMovement aComponent, boolean aDeletable) {
        if (aComponent == null) {
            return;
        }
        GlobalSeparatorHTMLElement theElement = addTitleLevel2("Constant movement");
        addTextInputfieldPropertyEditor("Movement speed", aComponent.speedProperty(), new ObjectConverter<Speed>() {
            @Override
            public String convertFrom(Speed aValue) {
                return Long.toString(aValue.speed);
            }

            @Override
            public Speed convertTo(String aValue) {
                return new Speed(Long.parseLong(aValue));
            }
        });
        addTextInputfieldPropertyEditor("Rotation speed", aComponent.rotationSpeedProperty(), new ObjectConverter<Speed>() {
            @Override
            public String convertFrom(Speed aValue) {
                return Long.toString(aValue.speed);
            }

            @Override
            public Speed convertTo(String aValue) {
                return new Speed(Long.parseLong(aValue));
            }
        });
        if (aDeletable) {
            theElement.addDeleteListener((aEvent) -> {
                aComponent.delete();
                selectParentOf(aComponent);

                Toast.info("Behavior deleted.");
            });
        }
    }

    private void addSubComponent(Platform aComponent, boolean aDeletable) {
        if (aComponent == null) {
            return;
        }

        GlobalSeparatorHTMLElement theElement = addTitleLevel2("Platformer");
        addSelectionEditor("Move left", aComponent.moveLeftKeyProperty(), GameKeyCode.values());
        addSelectionEditor("Move right", aComponent.moveRightKeyProperty(), GameKeyCode.values());
        addSelectionEditor("Jump", aComponent.jumpKeyProperty(), GameKeyCode.values());
        addTextInputfieldPropertyEditor("Jump impulse.", aComponent.jumpImpulseProperty(), new FloatStringConverter());
        addTextInputfieldPropertyEditor("Left/rigt impulse.", aComponent.leftRightImpulseProperty(), new FloatStringConverter());
        if (aDeletable) {
            theElement.addDeleteListener((aEvent) -> {
                aComponent.delete();
                selectParentOf(aComponent);

                Toast.info("Behavior deleted.");
            });
        }
    }

    private void addSubComponent(Physics aComponent, boolean aDeletable) {
        if (aComponent == null) {
            return;
        }

        GlobalSeparatorHTMLElement theElement = addTitleLevel2("Physics");
        addBooleanPropertyEditor("Active", aComponent.activeProperty());
        addBooleanPropertyEditor("Fixed rotation", aComponent.fixedRotationProperty());
        addTextInputfieldPropertyEditor("Density", aComponent.densityProperty(), new FloatStringConverter());
        addTextInputfieldPropertyEditor("Friction", aComponent.frictionProperty(), new FloatStringConverter());
        addTextInputfieldPropertyEditor("Restitution", aComponent.restitutionProperty(), new FloatStringConverter());
        addTextInputfieldPropertyEditor("Gravity", aComponent.gravityScaleProperty(), new FloatStringConverter());
        if (aDeletable) {
            theElement.addDeleteListener((aEvent) -> {
                aComponent.delete();
                selectParentOf(aComponent);

                Toast.info("Behavior deleted.");
            });
        }
    }

    private void addSubComponent(Sprite aComponent, GameScene aGameScene, boolean aDeletable) {
        if (aComponent == null) {
            return;
        }

        GlobalSeparatorHTMLElement theElement = addTitleLevel2("Sprite");
        addTextInputfieldPropertyEditor("Speed", aComponent.speedProperty(), new IntegerStringConverter());
        addAnimationEditor("Animation", aComponent.currentAnimationProperty(), aGameScene);
        if (aDeletable) {
            theElement.addDeleteListener((aEvent) -> {
                aComponent.delete();
                selectParentOf(aComponent);

                Toast.info("Behavior deleted.");
            });
        }
    }

    private <T> void addTextInputfieldPropertyEditor(String aLabel, Property<T> aProperty, ObjectConverter<T> aConverter) {

        TextfieldPropertyEditorHTMLElement theElement = TextfieldPropertyEditorHTMLElement.create();
        theElement.setLabel(aLabel);
        binder.add(theElement.bindTo(aProperty, aConverter));

        htmlElement.appendChild(theElement);
    }

    private void addColorPropertyEditor(String aLabel, Property<Color> aProperty) {

        ColorPropertyEditorHTMLElement theElement = ColorPropertyEditorHTMLElement.create();
        theElement.setLabel(aLabel);
        binder.add(theElement.bindTo(aProperty));

        htmlElement.appendChild(theElement);
    }

    private <T> void addLongStringPropertyEditor(String aLabel, Property<T> aProperty, ObjectConverter<T> aConverter) {

        TextareaPropertyEditorHTMLElement theElement = TextareaPropertyEditorHTMLElement.create();
        theElement.setLabel(aLabel);
        binder.add(theElement.bindTo(aProperty, aConverter));

        htmlElement.appendChild(theElement);
    }

    private void addBooleanPropertyEditor(String aLabel, Property<Boolean> aProperty) {

        CheckboxPropertyEditorHTMLElement theElement = CheckboxPropertyEditorHTMLElement.create();
        theElement.setLabel(aLabel);
        binder.add(theElement.bindTo(aProperty));

        htmlElement.appendChild(theElement);
    }

    private <T> void addSelectionEditor(String aLabel, Property<T> aProperty, T[] aValues) {

        SelectPropertyEditorHTMLElement theElement = SelectPropertyEditorHTMLElement.create();
        theElement.setLabel(aLabel);
        binder.add(theElement.bindTo(aProperty, aValues));
        htmlElement.appendChild(theElement);
    }

    private <T> void addGameObjectSelectEditor(String aLabel, Property<GameObject> aProperty, GameObject[] aObjects) {

        SelectPropertyEditorHTMLElement theElement = SelectPropertyEditorHTMLElement.create();
        theElement.setLabel(aLabel);
        binder.add(theElement.bindTo(aProperty, aObjects, aValue -> aValue.nameProperty().get()));
        htmlElement.appendChild(theElement);
    }

    private void addAnimationEditor(String aLabel, Property<Animation> aProperty, GameScene aScene) {

        AnimationPropertyEditorHTMLElement theElement = AnimationPropertyEditorHTMLElement.create();
        theElement.setLabel(aLabel);
        theElement.bindTo(aProperty, aScene);

        htmlElement.appendChild(theElement);
    }

    private void addSizePropertyEditor(Property<Size> aProperty) {

        TwofieldPropertyEditorHTMLElement theElement = TwofieldPropertyEditorHTMLElement.create();
        theElement.setLabel1("Width");
        theElement.setLabel2("Height");
        binder.add(theElement.bindField1To(aProperty, new ObjectConverter<Size>() {
            @Override
            public String convertFrom(Size aValue) {
                return Integer.toString(aValue.width);
            }

            @Override
            public Size convertTo(String aValue) {
                return aProperty.get().changeWidth(Integer.parseInt(aValue));
            }
        }));
        binder.add(theElement.bindField2To(aProperty, new ObjectConverter<Size>() {
            @Override
            public String convertFrom(Size aValue) {
                return Integer.toString(aValue.height);
            }

            @Override
            public Size convertTo(String aValue) {
                return aProperty.get().changeHeight(Integer.parseInt(aValue));
            }
        }));

        htmlElement.appendChild(theElement);
    }

    private void addPositionPropertyEditor(Property<Position> aProperty) {

        TwofieldPropertyEditorHTMLElement theElement = TwofieldPropertyEditorHTMLElement.create();
        theElement.setLabel1("X");
        theElement.setLabel2("Y");
        binder.add(theElement.bindField1To(aProperty, new ObjectConverter<Position>() {
            @Override
            public String convertFrom(Position aValue) {
                return Integer.toString((int) aValue.x);
            }

            @Override
            public Position convertTo(String aValue) {
                return aProperty.get().changeX((float) Integer.parseInt(aValue));
            }
        }));
        binder.add(theElement.bindField2To(aProperty, new ObjectConverter<Position>() {
            @Override
            public String convertFrom(Position aValue) {
                return Integer.toString((int) aValue.y);
            }

            @Override
            public Position convertTo(String aValue) {
                return aProperty.get().changeY((float) Integer.parseInt(aValue));
            }
        }));

        htmlElement.appendChild(theElement);
    }

    private void addFontPropertyEditor(Property<Font> aProperty) {

        FontPropertyEditorHTMLElement theElement = FontPropertyEditorHTMLElement.create();
        theElement.setLabel1("Font");
        theElement.setLabel2("Size");
        binder.add(theElement.bindTo(aProperty, Font.FontName.values()));

        htmlElement.appendChild(theElement);
    }
}