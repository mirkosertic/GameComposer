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
package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;

public abstract class DisplayObject implements JSObject {

    public abstract void destroy();

    @JSProperty
    public abstract Point getPosition();

    @JSProperty
    public abstract Point getScale();

    @JSProperty
    public abstract void setRotation(float aRotationInRadians);

    @JSProperty
    public abstract Point getPivot();

    @JSProperty
    public abstract void setWidth(int aWidth);

    @JSProperty
    public abstract void setHeight(int aHeight);

    @JSProperty
    public abstract void setZOrder(int aIndex);

    @JSProperty
    public abstract int getZOrder();

    @JSProperty
    public abstract void setAlpha(float alpha);

    @JSProperty
    public abstract void setFilters(JSArray<Filter> aFilters);
}