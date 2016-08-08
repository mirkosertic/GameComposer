package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.UUID;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ObjectEditor  {

    private final HTMLElement htmlElement;
    private final TemplateEngine templateEngine;
    private final Set<Binder> binder;

    public ObjectEditor(HTMLElement aHtmlElement, TemplateEngine aTemplateEngine) {
        htmlElement = aHtmlElement;
        templateEngine = aTemplateEngine;
        binder = new HashSet<>();
    }

    private void addTitleLevel1(String aTitle) {
        Map<String, Object> theParams = new HashMap<>();
        theParams.put("title", aTitle);
        HTMLElement theElement = templateEngine.renderToElement("propertyMajorSeparator", theParams);
        htmlElement.appendChild(theElement);
    }

    private void addTitleLevel2(String aTitle) {
        Map<String, Object> theParams = new HashMap<>();
        theParams.put("title", aTitle);
        HTMLElement theElement = templateEngine.renderToElement("propertyMinorSeparator", theParams);
        htmlElement.appendChild(theElement);
    }

    private void clear() {
        for (Binder theBinder : binder) {
            theBinder.unbind();
        }
        htmlElement.clear();
    }

    public void setEditingObject(GameObject aObject) {
        clear();
        addTitleLevel1("Game Object");
        addTitleLevel2("Common properties");
        addStringPropertyEditor("Name", aObject.nameProperty());
        addSizePropertyEditor(aObject.sizeProperty());
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
        binder.add(Binder.forStringProperty(theTextElement, aProperty));
    }

    private void addBooleanPropertyEditor(String aLabel, Property<Boolean> aProperty) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("label", aLabel);
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertyBooleanEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLInputElement theTextElement = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".checkbox");
        binder.add(Binder.forBooleanProperty(theTextElement, aProperty));
    }

    private void addSizePropertyEditor(Property<Size> aProperty) {

        String theNewID = UUID.randomUID();

        Map<String, Object> theParams = new HashMap<>();
        theParams.put("id", theNewID);

        HTMLElement theElement = templateEngine.renderToElement("propertySizeEditor", theParams);
        htmlElement.appendChild(theElement);

        HTMLInputElement theWidth = (HTMLInputElement) htmlElement.getOwnerDocument().getElementById(theNewID+".width");
        binder.add(Binder.forAnyProperty(theWidth, aProperty, new Binder.Converter<Size>() {
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
        binder.add(Binder.forAnyProperty(theHeight, aProperty, new Binder.Converter<Size>() {
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

}
