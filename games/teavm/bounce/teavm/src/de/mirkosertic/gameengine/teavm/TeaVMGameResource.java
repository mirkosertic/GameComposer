package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

import org.teavm.dom.canvas.CanvasImageSource;

public class TeaVMGameResource implements GameResource {

    private final GameResourceType type;
    private final String name;
    private final CanvasImageSource element;

    public TeaVMGameResource(String aName, GameResourceType aType, CanvasImageSource aElement) {
        name = aName;
        type = aType;
        element = aElement;
    }

    @Override
    public GameResourceType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public CanvasImageSource getElement() {
        return element;
    }
}
