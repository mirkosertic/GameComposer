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

import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;

public abstract class Container extends DisplayObject {

    @JSBody(params = {}, script = "return new PIXI.Container();")
    public static native Container createContainer();

    @JSProperty
    public abstract DisplayObject[] getChildren();

    public abstract void addChild(DisplayObject aChild);

    public abstract void removeChild(DisplayObject aChild);

    @JSBody(params = {"aChild1", "aChild2"}, script = "this.swapChildren(aChild1, aChild2);")
    public abstract void swapChildren(DisplayObject aChild1, DisplayObject aChild2);
}
