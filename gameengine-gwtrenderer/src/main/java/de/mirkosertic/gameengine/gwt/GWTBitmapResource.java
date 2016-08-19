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
package de.mirkosertic.gameengine.gwt;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;

public class GWTBitmapResource implements GameResource {

    private final Image image;
    private boolean loaded;
    private ImageElement imageElement;

    public GWTBitmapResource(Image aImage) {
        image = aImage;
        aImage.addLoadHandler(new LoadHandler() {
            @Override
            public void onLoad(LoadEvent loadEvent) {
                imageElement = ImageElement.as(image.getElement());
                loaded = true;
            }
        });
    }

    @Override
    public GameResourceType getType() {
        return GameResourceType.BITMAP;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public ImageElement getImage() {
        return imageElement;
    }
}
