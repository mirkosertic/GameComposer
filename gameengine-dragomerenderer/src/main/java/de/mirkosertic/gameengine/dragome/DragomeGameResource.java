package de.mirkosertic.gameengine.dragome;

import com.dragome.html.dom.w3c.BrowserElement;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

public class DragomeGameResource implements GameResource {

    private final GameResourceType type;
    private final String name;
    private final BrowserElement element;

    public DragomeGameResource(String aName, GameResourceType aType, BrowserElement aBrowserElement) {
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

    public BrowserElement getElement() {
        return element;
    }
}
