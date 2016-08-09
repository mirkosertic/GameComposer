package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.teavm.pixi.Texture;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TeaVMGameResourceLoader implements GameResourceLoader {

    private final String sceneId;
    private final Map<ResourceName, TeaVMTextureResource> textureResources;

    public TeaVMGameResourceLoader(String aSceneID) {
        sceneId = aSceneID;
        textureResources = new HashMap<>();
    }

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        String theResourceName = sceneId + aResourceName.name.replace('\\', '/');
        if (aResourceName.name.endsWith(".wav")) {
            return new TeaVMSoundResource(theResourceName);
        }

        TeaVMTextureResource theResource = textureResources.get(aResourceName);
        if (theResource == null) {
            Texture theTexure = Texture.createTextureFromImage(theResourceName);
            theResource = new TeaVMTextureResource(theTexure);
            textureResources.put(aResourceName, theResource);
        }

        return theResource;
    }

    @Override
    public void flush() {

        for (Map.Entry<ResourceName, TeaVMTextureResource> aEntry : textureResources.entrySet()) {
            aEntry.getValue().getTexture().destroy();
        }
        textureResources.clear();
    }
}