package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

import org.w3c.dom.Element;

public class DragomeGameResource implements GameResource {

    private final GameResourceType type;
    private final String name;
    private final Element element;

    public DragomeGameResource(String aName, GameResourceType aType, Element aBrowserElement) {
        name = aName;
        type = aType;
        element = aBrowserElement;
    }

    @Override
    public GameResourceType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Element getElement() {
        return element;
    }
}
