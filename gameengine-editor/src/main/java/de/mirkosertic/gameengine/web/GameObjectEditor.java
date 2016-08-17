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
import de.mirkosertic.gameengine.generic.CSSUtils;
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
import de.mirkosertic.gameengine.teavm.TeaVMTextureResource;
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
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.type.ScoreValue;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.Speed;
import de.mirkosertic.gameengine.type.TextExpression;
import de.mirkosertic.gameengine.type.UUID;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLImageElement;
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

    public static class ColorStringConverter implements HTMLInputBinder.Converter<Color, String> {

        @Override
        public String convertFrom(Color aValue) {
            return CSSUtils.toColorHex(aValue);
        }

        @Override
        public Color convertTo(String aValue) {
            return CSSUtils.fromColorHex(aValue);
        }
    }

    public GameObjectEditor(HTMLElement aHtmlElement, HTMLTemplateEngine aTemplateEngine) {
        super(aHtmlElement, aTemplateEngine);
    }

    public void setEditingObject(GameObject aObject) {
        clear();
        addTitleLevel1("Game Object");
        addTitleLevel2("Common properties");
        addStringPropertyEditor("Name", aObject.nameProperty());
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
        addStringPropertyEditor("Name", aObject.nameProperty());
        addColorPropertyEditor("Background color", aObject.backgroundColorProperty());
    }

    public void setEditingObject(EventSheet aObject) {
        clear();
        addTitleLevel1("Event Sheet");
        addTitleLevel2("Common properties");
        addStringPropertyEditor("Name", aObject.nameProperty());
    }

    public void setEditingObject(GameObjectInstance aObject) {
        clear();
        addTitleLevel1("Game Object Instance");
        addStringPropertyEditor("Name", aObject.nameProperty());
        addPositionPropertyEditor(aObject.positionProperty());
        addBooleanPropertyEditor("Visible", aObject.visibleProperty());
        addStringPropertyEditor("Rot.", aObject.rotationAngleProperty(), new HTMLInputBinder.Converter<Angle, String>() {

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
        addStringPropertyEditor("Score", aComponent.scoreValueProperty(), new HTMLInputBinder.Converter<ScoreValue, String>() {
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
        addStringPropertyEditor("Movement speed", aComponent.speedProperty(), new HTMLInputBinder.Converter<Speed, String>() {
            @Override
            public String convertFrom(Speed aValue) {
                return Long.toString(aValue.speed);
            }

            @Override
            public Speed convertTo(String aValue) {
                return new Speed(Long.parseLong(aValue));
            }
        });
        addStringPropertyEditor("Rotation speed", aComponent.rotationSpeedProperty(), new HTMLInputBinder.Converter<Speed, String>() {
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
        addStringPropertyEditor("Jump impulse.", aComponent.jumpImpulseProperty(), new FloatStringConverter());
        addStringPropertyEditor("Left/rigt impulse.", aComponent.leftRightImpulseProperty(), new FloatStringConverter());
    }

    private void addSubComponent(Physics aComponent) {
        if (aComponent == null) {
            return;
        }

        addTitleLevel2("Physics");
        addBooleanPropertyEditor("Active", aComponent.activeProperty());
        addBooleanPropertyEditor("Fixed rotation", aComponent.fixedRotationProperty());
        addStringPropertyEditor("Density", aComponent.densityProperty(), new FloatStringConverter());
        addStringPropertyEditor("Friction", aComponent.frictionProperty(), new FloatStringConverter());
        addStringPropertyEditor("Restitution", aComponent.restitutionProperty(), new FloatStringConverter());
        addStringPropertyEditor("Gravity", aComponent.gravityScaleProperty(), new FloatStringConverter());
    }

    private void addSubComponent(Sprite aComponent, GameScene aGameScene) {
        if (aComponent == null) {
            return;
        }

        addTitleLevel2("Sprite");
        addStringPropertyEditor("Speed", aComponent.speedProperty(), new IntegerStringConverter());
        addAnimationEditor("Animation", aComponent.currentAnimationProperty(), aGameScene);
    }

    private void addStringPropertyEditor(String aLabel, Property<String> aProperty) {

        HTMLElement theDIV = templateEngine.createNewComponent("div");
        theDIV.setAttribute("class", "propertyEditor");

        EditorHTMLElement theElement = templateEngine.createNewComponent("string-propertyeditor");
        theElement.setAttribute("label", aLabel);
        theElement.setAttribute("value", aProperty.get());
        theElement.setAttribute("class", "field");
        theElement.addEventListener("value-changed", aEvent -> {
            aProperty.set(theElement.get("value"));
        });
        theDIV.appendChild(theElement);

        htmlElement.appendChild(theDIV);

//        HTMLInputElement theTextElement = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".text");
//        binder.add(HTMLInputBinder.forStringProperty(theTextElement, aProperty));
    }

    private void addColorPropertyEditor(String aLabel, Property<Color> aProperty) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("label", aLabel);
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertyColorEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLInputElement theTextElement = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".color");
        binder.add(HTMLInputBinder.forAnyProperty(theTextElement, aProperty, new ColorStringConverter()));
    }

    private <T> void addStringPropertyEditor(String aLabel, Property<T> aProperty, HTMLInputBinder.Converter<T, String> aConverter) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("label", aLabel);
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertyTextEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLInputElement theTextElement = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".text");
        binder.add(HTMLInputBinder.forAnyProperty(theTextElement, aProperty, aConverter));
    }

    private <T> void addLongStringPropertyEditor(String aLabel, Property<T> aProperty, HTMLInputBinder.Converter<T, String> aConverter) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("label", aLabel);
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertyTextAreaEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLInputElement theTextElement = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".text");
        binder.add(HTMLInputBinder.forAnyProperty(theTextElement, aProperty, aConverter));
    }

    private void addBooleanPropertyEditor(String aLabel, Property<Boolean> aProperty) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("label", aLabel);
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertyBooleanEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLInputElement theTextElement = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".checkbox");
        binder.add(HTMLInputBinder.forBooleanProperty(theTextElement, aProperty));
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

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("label", aLabel);
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertyAnimationEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLImageElement theImageElement = (HTMLImageElement) htmlElement.getOwnerDocument().getElementById(theNewID+".image");
        Animation theCurrentAnimation = aProperty.get();
        if (theCurrentAnimation != null && theCurrentAnimation.getSequenceSize() > 0) {
            ResourceName theResourceName = theCurrentAnimation.getResourceByIndex(0);
            try {
                TeaVMTextureResource aTexture = aScene.getRuntime().getResourceCache().getResourceFor(theResourceName);
                theImageElement.setSrc(aTexture.getUrl());
            } catch (Exception e) {
                aScene.getRuntime().getLogger().error("Error loading " + theResourceName.name);
            }
        }
    }

    private void addSizePropertyEditor(Property<Size> aProperty) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertySizeEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLInputElement theWidth = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".width");
        binder.add(HTMLInputBinder.forAnyProperty(theWidth, aProperty, new HTMLInputBinder.Converter<Size, String>() {
            @Override
            public String convertFrom(Size aValue) {
                return Integer.toString(aValue.width);
            }

            @Override
            public Size convertTo(String aValue) {
                return aProperty.get().changeWidth(Integer.parseInt(aValue));
            }
        }));

        HTMLInputElement theHeight = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".height");
        binder.add(HTMLInputBinder.forAnyProperty(theHeight, aProperty, new HTMLInputBinder.Converter<Size, String>() {
            @Override
            public String convertFrom(Size aValue) {
                return Integer.toString(aValue.height);
            }

            @Override
            public Size convertTo(String aValue) {
                return aProperty.get().changeHeight(Integer.parseInt(aValue));
            }
        }));
    }

    private void addPositionPropertyEditor(Property<Position> aProperty) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertyPositionEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLInputElement theWidth = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".x");
        binder.add(HTMLInputBinder.forAnyProperty(theWidth, aProperty, new HTMLInputBinder.Converter<Position, String>() {
            @Override
            public String convertFrom(Position aValue) {
                return Integer.toString((int) aValue.x);
            }

            @Override
            public Position convertTo(String aValue) {
                return aProperty.get().changeX((float) Integer.parseInt(aValue));
            }
        }));

        HTMLInputElement theHeight = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".y");
        binder.add(HTMLInputBinder.forAnyProperty(theHeight, aProperty, new HTMLInputBinder.Converter<Position, String>() {
            @Override
            public String convertFrom(Position aValue) {
                return Integer.toString((int) (aValue.y));
            }

            @Override
            public Position convertTo(String aValue) {
                return aProperty.get().changeY((float) Integer.parseInt(aValue));
            }
        }));
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