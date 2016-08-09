package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.UUID;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;

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
        setEditingObject(aObject.getOwnerGameObject());
        addTitleLevel2("Game Object Instance");
        addStringPropertyEditor("Name", aObject.nameProperty());
        addPositionPropertyEditor(aObject.positionProperty());
        addBooleanPropertyEditor("Visible", aObject.visibleProperty());
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
