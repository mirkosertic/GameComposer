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

import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameRule;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.ArrayList;
import java.util.List;

public abstract class EventsheetEditorHTMLElement implements HTMLElement {

    @JSBody(params = {}, script = "return document.createElement('gameeditor-eventsheeteditor');")
    public static native EventsheetEditorHTMLElement create();

    public List<HTMLInputBinder> bindTo(EventSheet aEventsheet,  Editor aEditor) {
        List<HTMLInputBinder> theResult = new ArrayList<>();

        RuleEditorHTMLElement.EventListener theListener = (aElement, aRule) -> {
            aEventsheet.removeRule(aRule);
            Polymer.dom(EventsheetEditorHTMLElement.this).removeChild(aElement);

            Toast.info("Rule deleted.");
        };

        for (GameRule theRule : aEventsheet.getRules()) {
            RuleEditorHTMLElement theRuleEditor = RuleEditorHTMLElement.create();
            Polymer.dom(this).appendChild(theRuleEditor);

            theResult.addAll(theRuleEditor.bindTo(theRule, aEventsheet, aEditor, theListener));
        }
        return theResult;
    }
}