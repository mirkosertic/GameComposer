package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.arcade.ConstantMovement;
import de.mirkosertic.gameengine.arcade.ConstantMovementBehavior;
import de.mirkosertic.gameengine.arcade.ConstantMovementBehaviorTemplate;
import de.mirkosertic.gameengine.camera.Camera;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.CameraBehaviorTemplate;
import de.mirkosertic.gameengine.camera.CameraType;
import de.mirkosertic.gameengine.core.EventSheet;
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
import de.mirkosertic.gameengine.type.UUID;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.html.HTMLOptionElement;
import org.teavm.jso.dom.html.HTMLOptionsCollection;
import org.teavm.jso.dom.html.HTMLSelectElement;

import java.util.HashMap;
import java.util.Map;

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

    public GameObjectEditor(HTMLElement aHtmlElement, HTMLTemplateEngine aTemplateEngine) {
        super(aHtmlElement, aTemplateEngine);
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

    public void setEditingObject(GameScene aObject) {
        clear();
        addTitleLevel1("Game Scene");
        addTitleLevel2("Common properties");
        addTextInputfieldPropertyEditor("Name", aObject.nameProperty(), new StringStringConverter());
        addColorPropertyEditor("Background color", aObject.backgroundColorProperty());
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
        addFontPropertyEditor("Font", aComponent.fontProperty());
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

        TextfieldPropertyEditorHTMLElement theElement = templateEngine.createNewComponent("textfield-propertyeditor");
        theElement.setLabel(aLabel);
        binder.add(theElement.bindTo(aProperty, aConverter));

        htmlElement.appendChild(theElement);
    }

    private void addColorPropertyEditor(String aLabel, Property<Color> aProperty) {

        ColorPropertyEditorHTMLElement theElement = templateEngine.createNewComponent("color-propertyeditor");
        theElement.setLabel(aLabel);
        binder.add(theElement.bindTo(aProperty));

        htmlElement.appendChild(theElement);
    }

    private <T> void addLongStringPropertyEditor(String aLabel, Property<T> aProperty, HTMLInputBinder.Converter<T, String> aConverter) {

        TextareaPropertyEditorHTMLElement theElement = templateEngine.createNewComponent("textarea-propertyeditor");
        theElement.setLabel(aLabel);
        binder.add(theElement.bindTo(aProperty, aConverter));

        htmlElement.appendChild(theElement);
    }

    private void addBooleanPropertyEditor(String aLabel, Property<Boolean> aProperty) {

        CheckboxPropertyEditorHTMLElement theElement = templateEngine.createNewComponent("checkbox-propertyeditor");
        theElement.setLabel(aLabel);
        binder.add(theElement.bindTo(aProperty));

        htmlElement.appendChild(theElement);
    }

    private <T> void refreshCollections(HTMLSelectElement aElement, T[] aValues) {
        HTMLOptionsCollection theOptions = aElement.getOptions();
        while(theOptions.getLength() > 0) {
            theOptions.remove(0);
        }
        for (T theValue : aValues) {
            HTMLOptionElement theOption = (HTMLOptionElement)aElement.getOwnerDocument().createElement("option");
            theOption.setAttribute("name", theValue.toString());
            theOption.setInnerHTML(theValue.toString());
            theOptions.add(theOption);
        }
    }

    private <T> void addSelectionEditor(String aLabel, Property<T> aProperty, T[] aValues) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("label", aLabel);
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertySelectionEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLSelectElement theSelectElement = (HTMLSelectElement) htmlElement.getOwnerDocument().getElementById(theNewID+".selection");
        refreshCollections(theSelectElement, aValues);

        binder.add(HTMLInputBinder.forAnyProperty(theSelectElement, aProperty, aValues));
    }

    private void addAnimationEditor(String aLabel, Property<Animation> aProperty, GameScene aScene) {

        AnimationPropertyEditorHTMLElement theElement = templateEngine.createNewComponent("animation-propertyeditor");
        theElement.setLabel(aLabel);
        theElement.bindTo(aProperty, aScene);

        htmlElement.appendChild(theElement);
    }

    private void addSizePropertyEditor(Property<Size> aProperty) {

        TwofieldPropertyEditorHTMLElement theElement = templateEngine.createNewComponent("twofield-propertyeditor");
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

        TwofieldPropertyEditorHTMLElement theElement = templateEngine.createNewComponent("twofield-propertyeditor");
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

    private void addFontPropertyEditor(String aLabel, Property<Font> aProperty) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("id", theNewID);
        theParams.put("label", aLabel);

        HTMLElement theElement = templateEngine.renderToElement("propertyFontEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLSelectElement theFont = (HTMLSelectElement) htmlElement.getOwnerDocument().getElementById(theNewID+".font");
        refreshCollections(theFont, Font.FontName.values());

        binder.add(HTMLInputBinder.forAnyProperty(theFont, aProperty, Font.FontName.values(),
                new HTMLInputBinder.Converter<Font, Font.FontName>() {
                    @Override
                    public Font.FontName convertFrom(Font aValue) {
                        return aValue.name;
                    }

                    @Override
                    public Font convertTo(Font.FontName aValue) {
                        return aProperty.get().changeName(aValue);
                    }
                }));

        HTMLInputElement theSize = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".size");
        binder.add(HTMLInputBinder.forAnyProperty(theSize, aProperty, new HTMLInputBinder.Converter<Font, String>() {
            @Override
            public String convertFrom(Font aValue) {
                return Integer.toString((int) aValue.size);
            }

            @Override
            public Font convertTo(String aValue) {
                return aProperty.get().changeSize(Integer.parseInt(aValue));
            }
        }));
    }
}