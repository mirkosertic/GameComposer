/*
 * Copyright 2019 Mirko Sertic
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
package de.mirkosertic.gameengine.bytecoder.pixi;

import de.mirkosertic.bytecoder.api.OpaqueProperty;
import de.mirkosertic.bytecoder.api.OpaqueReferenceType;

public interface DisplayObject extends OpaqueReferenceType {

    public abstract void destroy();

    @OpaqueProperty
    public abstract Point position();

    @OpaqueProperty
    public abstract Point getScale();

    @OpaqueProperty
    public abstract void rotation(float aRotationInRadians);

    @OpaqueProperty
    public abstract Point pivot();

    @OpaqueProperty
    public abstract void width(int aWidth);

    @OpaqueProperty
    public abstract void height(int aHeight);

    @OpaqueProperty
    public abstract void zOrder(int aIndex);

    @OpaqueProperty
    public abstract int zOrder();

    @OpaqueProperty
    public abstract void alpha(float alpha);
}
