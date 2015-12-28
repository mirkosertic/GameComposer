package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLImageElement;
import org.teavm.jso.dom.xml.Node;
import org.teavm.jso.dom.xml.NodeList;

public class TeaVMGameResourceLoader implements GameResourceLoader {

    private final String sceneId;
    private final HTMLElement resourceCacheElement;
    private final HTMLDocument document;

    public TeaVMGameResourceLoader(String aSceneID, HTMLDocument aDocument, HTMLElement aResourceCacheElement) {
        sceneId = aSceneID;
        document = aDocument;
        resourceCacheElement = aResourceCacheElement;
    }

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        String theResourceName = sceneId + aResourceName.name.replace('\\', '/');
        if (aResourceName.name.endsWith(".wav")) {
            return new TeaVMGameResource(theResourceName, GameResourceType.SOUND, null);
        }

        HTMLImageElement theImage = (HTMLImageElement) document.createElement("img");
        theImage.setAttribute("src", theResourceName);
        resourceCacheElement.appendChild(theImage);

        return new TeaVMGameResource(theResourceName, GameResourceType.BITMAP, theImage);
    }

    @Override
    public void flush() {
        NodeList theChildren = resourceCacheElement.getChildNodes();
        for (int i=0;i<theChildren.getLength();i++) {
            Node theChild = (Node) theChildren.get(i);
            theChild.getParentNode().removeChild(theChild);
        }
    }
}
