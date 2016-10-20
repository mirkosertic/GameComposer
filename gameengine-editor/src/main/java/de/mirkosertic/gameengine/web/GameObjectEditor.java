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
import de.mirkosertic.gameengine.camera.Camera;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.CameraBehaviorTemplate;
import de.mirkosertic.gameengine.camera.CameraType;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
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

public class GameObjectEditor extends ListingElement {

    public static class FloatStringConverter implements HTMLInputBinder.Converter<Float, String> {

        @Override
        public String convertFrom(Float aValue) {
            return Float.toString(aValue);
        }

        @Override
        public Float convertTo(String aValue) {
            return Float.parseFloat(aValue);
        }
    }

    public static class StringStringConverter implements HTMLInputBinder.Converter<String, String> {

        @Override
        public String convertFrom(String aValue) {
            return aValue;
        }

        @Override
        public String convertTo(String aValue) {
            return aValue;
        }
    }

    public static class IntegerStringConverter implements HTMLInputBinder.Converter<Integer, String> {

        @Override
        public String convertFrom(Integer aValue) {
            return Integer.toString(aValue);
        }

        @Override
        public Integer convertTo(String aValue) {
            return Integer.parseInt(aValue);
        }
    }

    private final TabbedPaneHTMLElement.Manager tabbedPaneHTMLElement;

    public GameObjectEditor(HTMLElement aHtmlElement, TabbedPaneHTMLElement.Manager aTabbedPane) {
        super(aHtmlElement);
        tabbedPaneHTMLElement = aTabbedPane;
    }

    public void setEditingObject(GameObject aObject) {
        clear();
        addTitleLevel1("Game Object");
        addTitleLevel2("Common properties");
        addTextInputfieldPropertyEditor("Name", aObject.nameProperty(), new StringStringConverter());
        addSizePropertyEditor(aObject.sizeProperty());
        addBooleanPropertyEditor("Visible", aObject.visibleProperty());

        addSubComponent(aObject.getBehaviorTemplate(CameraBehaviorTemplate.class));
        addSubComponent(aObject.getBehaviorTemplate(PlayerScoreBehaviorTemplate.class));
        addSubComponent(aObject.getBehaviorTemplate(StaticBehaviorTemplate.class));
        addSubComponent(aObject.getBehaviorTemplate(TextBehaviorTemplate.class));
        addSubComponent(aObject.getBehaviorTemplate(PlatformBehaviorTemplate.class));
        addSubComponent(aObject.getBehaviorTemplate(ConstantMovementBehaviorTemplate.class));
        addSubComponent(aObject.getBehaviorTemplate(PhysicsBehaviorTemplate.class));
        addSubComponent(aObject.getBehaviorTemplate(SpriteBehaviorTemplate.class), aObject.getGameScene());
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
        addTextInputfieldPropertyEditor("Default Scene", aObject.defaultSceneProperty(), new StringStringConverter());
    }

    public void setEditingObject(GameScene aObject) {
        clear();
        addTitleLevel1("Game Scene");
        addTitleLevel2("Common properties");
        addTextInputfieldPropertyEditor("Name", aObject.nameProperty(), new StringStringConverter());
        addColorPropertyEditor("Background color", aObject.backgroundColorProperty());
        addGameObjectSelectEditor("Camera", aObject.cameraObjectProperty(), aObject.getObjects());
        addGameObjectSelectEditor("Player", aObject.defaultPlayerProperty(), aObject.getObjects());
    }

    public void setEditingObject(EventSheet aObject) {
        clear();
        addTitleLevel1("Event Sheet");
        addTitleLevel2("Common properties");
        addTextInputfieldPropertyEditor("Name", aObject.nameProperty(), new StringStringConverter());

        EventsheetEditorHTMLElement theEventsheetEditor = EventsheetEditorHTMLElement.create();
        theEventsheetEditor.bindTo(aObject, tabbedPaneHTMLElement);
        tabbedPaneHTMLElement.addTab("Event sheet", new TabbedPaneHTMLElement.TabHandler() {
            @Override
            public HTMLElement getElement() {
                return theEventsheetEditor;
            }

            @Override
            public Object getOwner() {
                return aObject;
            }

            @Override
            public void handleClosed() {
            }
        });
    }

    public void setEditingObject(GameObjectInstance aObject) {
        clear();
        addTitleLevel1("Game Object Instance");
        addTitleLevel2("Common properties");
        addTextInputfieldPropertyEditor("Name", aObject.nameProperty(), new StringStringConverter());
        addPositionPropertyEditor(aObject.positionProperty());
        addBooleanPropertyEditor("Visible", aObject.visibleProperty());
        addTextInputfieldPropertyEditor("Rotation", aObject.rotationAngleProperty(), new HTMLInputBinder.Converter<Angle, String>() {

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

        addSubComponent(aObject.getBehavior(CameraBehavior.class));
        addSubComponent(aObject.getBehavior(PlayerScoreBehavior.class));
        addSubComponent(aObject.getBehavior(StaticBehavior.class));
        addSubComponent(aObject.getBehavior(TextBehavior.class));
        addSubComponent(aObject.getBehavior(PlatformBehavior.class));
        addSubComponent(aObject.getBehavior(ConstantMovementBehavior.class));
        addSubComponent(aObject.getBehavior(PhysicsBehavior.class));
        addSubComponent(aObject.getBehavior(SpriteBehavior.class), aObject.getOwnerGameObject().getGameScene());
    }

    private void addSubComponent(Camera aComponent) {
        if (aComponent == null) {
            return;
        }
        addTitleLevel2("Camera");
        addSelectionEditor("Type", aComponent.typeProperty(), CameraType.values());
    }

    private void addSubComponent(Static aComponent) {
        if (aComponent == null) {
            return;
        }
        addTitleLevel2("Static");
    }

    private void addSubComponent(Text aComponent) {
        if (aComponent == null) {
            return;
        }
        addTitleLevel2("Text");
        addFontPropertyEditor(aComponent.fontProperty());
        addBooleanPropertyEditor("Is LUA Script", aComponent.isScriptProperty());
        addLongStringPropertyEditor("Text Expression", aComponent.textExpressionProperty(),
            new HTMLInputBinder.Converter<TextExpression, String>() {
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
    }

    private void addSubComponent(PlayerScore aComponent) {
        if (aComponent == null) {
            return;
        }
        addTitleLevel2("Player score");
        addTextInputfieldPropertyEditor("Score", aComponent.scoreValueProperty(), new HTMLInputBinder.Converter<ScoreValue, String>() {
            @Override
            public String convertFrom(ScoreValue aValue) {
                return Long.toString(aValue.score);
            }

            @Override
            public ScoreValue convertTo(String aValue) {
                return new ScoreValue(Long.parseLong(aValue));
            }
        });
    }

    private void addSubComponent(ConstantMovement aComponent) {
        if (aComponent == null) {
            return;
        }
        addTitleLevel2("Constant movement");
        addTextInputfieldPropertyEditor("Movement speed", aComponent.speedProperty(), new HTMLInputBinder.Converter<Speed, String>() {
            @Override
            public String convertFrom(Speed aValue) {
                return Long.toString(aValue.speed);
            }

            @Override
            public Speed convertTo(String aValue) {
                return new Speed(Long.parseLong(aValue));
            }
        });
        addTextInputfieldPropertyEditor("Rotation speed", aComponent.rotationSpeedProperty(), new HTMLInputBinder.Converter<Speed, String>() {
            @Override
            public String convertFrom(Speed aValue) {
                return Long.toString(aValue.speed);
            }

            @Override
            public Speed convertTo(String aValue) {
                return new Speed(Long.parseLong(aValue));
            }
        });
    }

    private void addSubComponent(Platform aComponent) {
        if (aComponent == null) {
            return;
        }

        addTitleLevel2("Platformer");
        addSelectionEditor("Move left", aComponent.moveLeftKeyProperty(), GameKeyCode.values());
        addSelectionEditor("Move right", aComponent.moveRightKeyProperty(), GameKeyCode.values());
        addSelectionEditor("Jump", aComponent.jumpKeyProperty(), GameKeyCode.values());
        addTextInputfieldPropertyEditor("Jump impulse.", aComponent.jumpImpulseProperty(), new FloatStringConverter());
        addTextInputfieldPropertyEditor("Left/rigt impulse.", aComponent.leftRightImpulseProperty(), new FloatStringConverter());
    }

    private void addSubComponent(Physics aComponent) {
        if (aComponent == null) {
            return;
        }

        addTitleLevel2("Physics");
        addBooleanPropertyEditor("Active", aComponent.activeProperty());
        addBooleanPropertyEditor("Fixed rotation", aComponent.fixedRotationProperty());
        addTextInputfieldPropertyEditor("Density", aComponent.densityProperty(), new FloatStringConverter());
        addTextInputfieldPropertyEditor("Friction", aComponent.frictionProperty(), new FloatStringConverter());
        addTextInputfieldPropertyEditor("Restitution", aComponent.restitutionProperty(), new FloatStringConverter());
        addTextInputfieldPropertyEditor("Gravity", aComponent.gravityScaleProperty(), new FloatStringConverter());
    }

    private void addSubComponent(Sprite aComponent, GameScene aGameScene) {
        if (aComponent == null) {
            return;
        }

        addTitleLevel2("Sprite");
        addTextInputfieldPropertyEditor("Speed", aComponent.speedProperty(), new IntegerStringConverter());
        addAnimationEditor("Animation", aComponent.currentAnimationProperty(), aGameScene);
    }

    private <T> void addTextInputfieldPropertyEditor(String aLabel, Property<T> aProperty, HTMLInputBinder.Converter<T, String> aConverter) {

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

    private <T> void addLongStringPropertyEditor(String aLabel, Property<T> aProperty, HTMLInputBinder.Converter<T, String> aConverter) {

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
        binder.add(theElement.bindField1To(aProperty, new HTMLInputBinder.Converter<Size, String>() {
            @Override
            public String convertFrom(Size aValue) {
                return Integer.toString(aValue.width);
            }

            @Override
            public Size convertTo(String aValue) {
                return aProperty.get().changeWidth(Integer.parseInt(aValue));
            }
        }));
        binder.add(theElement.bindField2To(aProperty, new HTMLInputBinder.Converter<Size, String>() {
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
        binder.add(theElement.bindField1To(aProperty, new HTMLInputBinder.Converter<Position, String>() {
            @Override
            public String convertFrom(Position aValue) {
                return Integer.toString((int) aValue.x);
            }

            @Override
            public Position convertTo(String aValue) {
                return aProperty.get().changeX((float) Integer.parseInt(aValue));
            }
        }));
        binder.add(theElement.bindField2To(aProperty, new HTMLInputBinder.Converter<Position, String>() {
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