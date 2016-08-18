package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.teavm.TeaVMTextureResource;
import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.ResourceName;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class AnimationPropertyEditorHTMLElement implements HTMLElement {

    @JSProperty
    public abstract void setLabel(String aLabel);

    @JSProperty
    public abstract String getLabel();

    @JSProperty
    public abstract void setUrl(String aURL);

    @JSProperty
    public abstract String getUrl();

    public void bindTo(Property<Animation> aProperty, GameScene aScene) {

        Animation theCurrentAnimation = aProperty.get();
        if (theCurrentAnimation != null && theCurrentAnimation.getSequenceSize() > 0) {
            ResourceName theResourceName = theCurrentAnimation.getResourceByIndex(0);
            try {
                TeaVMTextureResource aTexture = aScene.getRuntime().getResourceCache().getResourceFor(theResourceName);
                setUrl(aTexture.getUrl());
            } catch (Exception e) {
                aScene.getRuntime().getLogger().error("Error loading " + theResourceName.name);
            }
        }
    }
}
