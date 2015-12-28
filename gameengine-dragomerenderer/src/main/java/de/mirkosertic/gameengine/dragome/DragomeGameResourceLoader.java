package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.dragome.web.html.dom.DomHandler;
import com.dragome.web.html.dom.w3c.BrowserDomHandler;

public class DragomeGameResourceLoader implements GameResourceLoader {

    private final String sceneId;
    private final Element cacheElement;
    private final Document document;

    public DragomeGameResourceLoader(String aSceneId) {
        sceneId = aSceneId;

        DomHandler theHandler = new BrowserDomHandler();

        cacheElement = theHandler.getElementBySelector("#resourcecache");
        document = theHandler.getDocument();
    }

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        String theResourceName = sceneId + aResourceName.name;
        if (aResourceName.name.endsWith(".wav")) {
            return new DragomeGameResource(theResourceName, GameResourceType.SOUND, null);
        }

        Element theImage = document.createElement("img");
        theImage.setAttribute("src", theResourceName);
        cacheElement.appendChild(theImage);

        return new DragomeGameResource(theResourceName, GameResourceType.BITMAP, theImage);
    }

    @Override
    public void flush() {
        NodeList theChilds = cacheElement.getChildNodes();
        for (int i=0;i<theChilds.getLength();i++) {
            Node theChild = theChilds.item(i);
            theChild.getParentNode().removeChild(theChild);
        }
    }
}