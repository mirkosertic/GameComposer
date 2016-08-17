package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.teavm.pixi.Texture;

public class TeaVMTextureResource implements GameResource {

    private final Texture texture;
    private final String url;

    public TeaVMTextureResource(Texture aTexture, String aURL) {
        texture = aTexture;
        url = aURL;
    }

    public String getUrl() {
        return url;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public GameResourceType getType() {
        return GameResourceType.BITMAP;
    }
}
