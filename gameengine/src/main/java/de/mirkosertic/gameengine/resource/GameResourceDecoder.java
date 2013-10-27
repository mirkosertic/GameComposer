package de.mirkosertic.gameengine.resource;

import de.mirkosertic.gameengine.core.ResourceName;

import java.io.InputStream;

public interface GameResourceDecoder<T extends GameResource> {

    boolean canDecode(ResourceName aResourceName);

    T decodeToResource(InputStream aInputStream);
}
