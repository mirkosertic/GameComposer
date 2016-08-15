package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.camera.Camera;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.CameraBehaviorTemplate;
import de.mirkosertic.gameengine.camera.CameraType;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.physic.Static;
import de.mirkosertic.gameengine.physic.StaticBehavior;
import de.mirkosertic.gameengine.physic.StaticBehaviorTemplate;
import de.mirkosertic.gameengine.playerscore.PlayerScore;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehavior;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehaviorTemplate;
import de.mirkosertic.gameengine.type.*;
import org.teavm.jso.dom.html.*;

import java.util.HashMap;
import java.util.Map;

public class GameObjectEditor extends ListingElement {

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

        CameraBehaviorTemplate theCamera = aObject.getBehaviorTemplate(CameraBehaviorTemplate.class);
        if (theCamera != null) {
            addSubComponent(theCamera);
        }
        PlayerScoreBehaviorTemplate thePlayerscore = aObject.getBehaviorTemplate(PlayerScoreBehaviorTemplate.class);
        if (thePlayerscore != null) {
            addSubComponent(thePlayerscore);
        }
        StaticBehaviorTemplate theStatic = aObject.getBehaviorTemplate(StaticBehaviorTemplate.class);
        if (theStatic != null) {
            addSubComponent(theStatic);
        }
    }

    public void setEditingObject(GameScene aObject) {
        clear();
        addTitleLevel1("Game Scene");
        addTitleLevel2("Common properties");
        addStringPropertyEditor("Name", aObject.nameProperty());
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
        addStringPropertyEditor("Rot.", aObject.rotationAngleProperty(), new HTMLInputBinder.Converter<Angle>() {

            @Override
            public String asString(Angle aValue) {
                return Integer.toString(aValue.angleInDegrees);
            }

            @Override
            public Angle asValue(String aValue) {
                return new Angle(Integer.valueOf(aValue));
            }
        });
        addSelectionEditor("Anchor", aObject.positionAnchorProperty(), PositionAnchor.values());

        CameraBehavior theCamera = aObject.getBehavior(CameraBehavior.class);
        if (theCamera != null) {
            addSubComponent(theCamera);
        }
        PlayerScoreBehavior thePlayerscore = aObject.getBehavior(PlayerScoreBehavior.class);
        if (thePlayerscore != null) {
            addSubComponent(thePlayerscore);
        }
        StaticBehavior theStatic = aObject.getBehavior(StaticBehavior.class);
        if (theStatic != null) {
            addSubComponent(theStatic);
        }
    }

    private void addSubComponent(Camera aComponent) {
        addTitleLevel2("Camera");
        addSelectionEditor("Type", aComponent.typeProperty(), CameraType.values());
    }

    private void addSubComponent(Static aComponent) {
        addTitleLevel2("Static");
    }

    private void addSubComponent(PlayerScore aComponent) {
        addTitleLevel2("Player score");
        addStringPropertyEditor("Score", aComponent.scoreValueProperty(), new HTMLInputBinder.Converter<ScoreValue>() {
            @Override
            public String asString(ScoreValue aValue) {
                return Long.toString(aValue.score);
            }

            @Override
            public ScoreValue asValue(String aValue) {
                return new ScoreValue(Long.parseLong(aValue));
            }
        });
    }

    private void addStringPropertyEditor(String aLabel, Property<String> aProperty) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("label", aLabel);
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertyTextEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLInputElement theTextElement = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".text");
        binder.add(HTMLInputBinder.forStringProperty(theTextElement, aProperty));
    }

    private <T> void addStringPropertyEditor(String aLabel, Property<T> aProperty, HTMLInputBinder.Converter<T> aConverter) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("label", aLabel);
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertyTextEditor", theParams);
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

    private void addSizePropertyEditor(Property<Size> aProperty) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertySizeEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLInputElement theWidth = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".width");
        binder.add(HTMLInputBinder.forAnyProperty(theWidth, aProperty, new HTMLInputBinder.Converter<Size>() {
            @Override
            public String asString(Size aValue) {
                return Integer.toString(aValue.width);
            }

            @Override
            public Size asValue(String aValue) {
                return aProperty.get().changeWidth(Integer.parseInt(aValue));
            }
        }));

        HTMLInputElement theHeight = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".height");
        binder.add(HTMLInputBinder.forAnyProperty(theHeight, aProperty, new HTMLInputBinder.Converter<Size>() {
            @Override
            public String asString(Size aValue) {
                return Integer.toString(aValue.height);
            }

            @Override
            public Size asValue(String aValue) {
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
        binder.add(HTMLInputBinder.forAnyProperty(theWidth, aProperty, new HTMLInputBinder.Converter<Position>() {
            @Override
            public String asString(Position aValue) {
                return Integer.toString((int) aValue.x);
            }

            @Override
            public Position asValue(String aValue) {
                return aProperty.get().changeX((float) Integer.parseInt(aValue));
            }
        }));

        HTMLInputElement theHeight = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".y");
        binder.add(HTMLInputBinder.forAnyProperty(theHeight, aProperty, new HTMLInputBinder.Converter<Position>() {
            @Override
            public String asString(Position aValue) {
                return Integer.toString((int) (aValue.y));
            }

            @Override
            public Position asValue(String aValue) {
                return aProperty.get().changeY((float) Integer.parseInt(aValue));
            }
        }));
    }
}
