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

import de.mirkosertic.gameengine.type.Position;
import org.teavm.jso.JSBody;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;

public abstract class SceneEditorHTMLElement implements HTMLElement {

    @JSBody(params = {}, script = "return document.createElement('gameeditor-sceneeditor');")
    public static native SceneEditorHTMLElement create();

    public abstract EditorHTMLCanvasElement currentCanvas();

    public abstract HTMLInputElement gridWidth();

    public abstract HTMLInputElement gridHeight();

    public abstract HTMLInputElement snapToGrid();

    public Position eventuallySnapToGrid(Position aPosition) {
        if (snapToGrid().isChecked()) {
            int theGridWidth = Integer.valueOf(gridWidth().getValue());
            int theGridHeight = Integer.valueOf(gridHeight().getValue());

            int theXMod = (int) aPosition.x % theGridWidth;
            int theYMod = (int) aPosition.y % theGridHeight;

            float theNewX;
            if (theXMod < theGridWidth / 2) {
                theNewX = aPosition.x - theXMod;
            } else {
                theNewX = (((int) (aPosition.x / theGridWidth)) + 1 ) * theGridWidth;
            }
            float theNewY;
            if (theYMod < theGridHeight / 2) {
                theNewY = aPosition.y - theYMod;
            } else {
                theNewY = (((int) (aPosition.y / theGridHeight)) + 1 ) * theGridHeight;
            }

            return new Position(theNewX, theNewY);
        }
        return aPosition;
    }
}