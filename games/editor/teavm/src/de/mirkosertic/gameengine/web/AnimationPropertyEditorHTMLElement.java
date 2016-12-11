/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.teavm.TeaVMTextureResource;
import de.mirkosertic.gameengine.teavm.pixi.Texture;
import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.ResourceName;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

public abstract class AnimationPropertyEditorHTMLElement implements HTMLElement {

    @JSBody(params = {}, script = "return document.createElement('gameeditor-animationpropertyeditor');")
    public static native AnimationPropertyEditorHTMLElement create();

    @JSProperty
    public abstract void setLabel(String aLabel);

    @JSProperty
    public abstract String getLabel();

    public abstract void setTexture(Texture aTexture);

    public void bindTo(Property<Animation> aProperty, GameScene aScene) {

        Animation theCurrentAnimation = aProperty.get();
        if (theCurrentAnimation != null && theCurrentAnimation.getSequenceSize() > 0) {
            ResourceName theResourceName = theCurrentAnimation.getResourceByIndex(0);
            aScene.getRuntime().getResourceCache().getResourceFor(theResourceName).thenContinue(aResult -> {
                setTexture(((TeaVMTextureResource) aResult).getTexture());
            }).catchError((aResult, aOptionalRejectedException) -> aScene.getRuntime().getLogger().error("Error loading " + theResourceName.name));
        }
    }
}