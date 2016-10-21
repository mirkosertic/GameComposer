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

import de.mirkosertic.gameengine.action.SystemTickCondition;
import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.Condition;
import de.mirkosertic.gameengine.core.DeleteGameObjectInstanceAction;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToSceneCondition;
import de.mirkosertic.gameengine.core.GameObjectInstanceLeftLayoutCondition;
import de.mirkosertic.gameengine.core.GameObjectInstanceRemovedFromSceneCondition;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.core.SceneStartedCondition;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.input.KeyEventCondition;
import de.mirkosertic.gameengine.input.MouseEventCondition;
import de.mirkosertic.gameengine.physic.ObjectCollisionCondition;
import de.mirkosertic.gameengine.script.RunScriptAction;
import de.mirkosertic.gameengine.type.CollisionPosition;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Script;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RuleEditorHTMLElement implements HTMLElement {

    abstract class ConditionMetaData<T extends Condition> {

        public abstract boolean matches(Condition aCondition);

        public abstract T create();

        public void initEditorFor(T aCondition, HTMLElement aElement) {
        }
    }

    abstract class ActionMetaData<T extends Action> {

        public abstract boolean matches(Action aAction);

        public abstract T create();

        public void initEditorFor(T Action, HTMLElement aElement) {
        }
    }

    @JSBody(params = {}, script = "return document.createElement('gameeditor-ruleeditor');")
    public static native RuleEditorHTMLElement create();

    public abstract SelectPropertyEditorHTMLElement conditionselection();

    public abstract TextfieldPropertyEditorHTMLElement rulename();

    public abstract HTMLElement conditionproperties();

    public abstract HTMLElement actionelements();

    public abstract void clearconditionproperties();

    public abstract void clearactions();

    public List<HTMLInputBinder> bindTo(GameRule aRule, EventSheet aEventSheet,  TabbedPaneHTMLElement.Manager aEditorTab) {

        final Map<String, ConditionMetaData<? extends Condition>> theConditionMetaData = new HashMap<>();
        theConditionMetaData.put("InstanceRemovedFromScene", new ConditionMetaData<Condition>() {
            @Override
            public GameObjectInstanceRemovedFromSceneCondition create() {
                return new GameObjectInstanceRemovedFromSceneCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof GameObjectInstanceRemovedFromSceneCondition;
            }
        });
        theConditionMetaData.put("KeyEvent", new ConditionMetaData<KeyEventCondition>() {
            @Override
            public KeyEventCondition create() {
                return new KeyEventCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof KeyEventCondition;
            }

            @Override
            public void initEditorFor(KeyEventCondition aCondition, HTMLElement aElement) {

                KeyEventConditionHTMLElement theElement = KeyEventConditionHTMLElement.create();
                theElement.keycodeelement().bindTo(aCondition.keyCodeProperty(), GameKeyCode.values());
                theElement.eventtypeelement().bindTo(aCondition.eventTypeProperty(), KeyEventCondition.KeyEventType.values());

                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theConditionMetaData.put("InstanceAddedToScene", new ConditionMetaData<Condition>() {
            @Override
            public GameObjectInstanceAddedToSceneCondition create() {
                return new GameObjectInstanceAddedToSceneCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof GameObjectInstanceAddedToSceneCondition;
            }
        });
        theConditionMetaData.put("SystemTick", new ConditionMetaData<Condition>() {
            @Override
            public SystemTickCondition create() {
                return new SystemTickCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof SystemTickCondition;
            }
        });
        theConditionMetaData.put("SceneStarted", new ConditionMetaData<Condition>() {
            @Override
            public SceneStartedCondition create() {
                return new SceneStartedCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof SceneStartedCondition;
            }
        });
        theConditionMetaData.put("MouseEvent", new ConditionMetaData<Condition>() {
            @Override
            public MouseEventCondition create() {
                return new MouseEventCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof MouseEventCondition;
            }
        });
        theConditionMetaData.put("ObjectCollision", new ConditionMetaData<ObjectCollisionCondition>() {
            @Override
            public ObjectCollisionCondition create() {
                return new ObjectCollisionCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof ObjectCollisionCondition;
            }

            @Override
            public void initEditorFor(ObjectCollisionCondition aCondition, HTMLElement aElement) {

                ObjectCollisionConditionHTMLElement theElement = ObjectCollisionConditionHTMLElement.create();
                theElement.primaryelement().bindTo(aCondition.primaryObjectProperty(), aEventSheet.getGameScene().getObjects(),
                        aValue -> aValue.nameProperty().get());
                theElement.secondaryelement().bindTo(aCondition.secondaryObjectProperty(), aEventSheet.getGameScene().getObjects(),
                        aValue -> aValue.nameProperty().get());
                theElement.positionelement().bindTo(aCondition.positionProperty(), CollisionPosition.values());

                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theConditionMetaData.put("InstanceLeftLayout", new ConditionMetaData<Condition>() {
            @Override
            public GameObjectInstanceLeftLayoutCondition create() {
                return new GameObjectInstanceLeftLayoutCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof GameObjectInstanceLeftLayoutCondition;
            }
        });


        Map<String, ActionMetaData<? extends Action>> theActionMetaData = new HashMap<>();
        theActionMetaData.put("Delete Instance", new ActionMetaData<DeleteGameObjectInstanceAction>() {
            @Override
            public boolean matches(Action aAction) {
                return aAction instanceof DeleteGameObjectInstanceAction;
            }

            @Override
            public DeleteGameObjectInstanceAction create() {
                return new DeleteGameObjectInstanceAction();
            }

            @Override
            public void initEditorFor(DeleteGameObjectInstanceAction Action, HTMLElement aElement) {
                DeleteGameObjectInstanceActionHTMLElement theElement = DeleteGameObjectInstanceActionHTMLElement.create();

                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theActionMetaData.put("Run LUA Script", new ActionMetaData<RunScriptAction>() {
            @Override
            public boolean matches(Action aAction) {
                return aAction instanceof RunScriptAction;
            }

            @Override
            public RunScriptAction create() {
                return new RunScriptAction();
            }

            @Override
            public void initEditorFor(RunScriptAction aAction, HTMLElement aElement) {
                RunLuaScriptActionHTMLElement theElement = RunLuaScriptActionHTMLElement.create();
                theElement.addEventListener("luaedit", evt -> {
                    Script theScript = aAction.scriptProperty().get();

                    AceEditorHTMLElement theEditor = AceEditorHTMLElement.create();
                    theEditor.initWithScript(theScript);

                    aEditorTab.addTab("LUA Script", new TabbedPaneHTMLElement.TabHandler() {
                        @Override
                        public HTMLElement getElement() {
                            return theEditor;
                        }

                        @Override
                        public Object getOwner() {
                            return aAction;
                        }

                        @Override
                        public void handleClosed() {
                        }

                        @Override
                        public void handleResize() {
                        }
                    });
                    theEditor.addEventListener("change", evt1 -> aAction.scriptProperty().set(new Script(theEditor.getValue())));
                });

                Polymer.dom(aElement).appendChild(theElement);
            }
        });

        List<HTMLInputBinder> theBinder = new ArrayList<>();
        theBinder.add(rulename().bindTo(aRule.nameProperty(), new GameObjectEditor.StringStringConverter()));

        Condition theCurrentCondition = aRule.conditionProperty().get();

        final Property<String> theCondition = new Property<>(String.class, null, "Condition", "");
        for (Map.Entry<String, ConditionMetaData<?>> theEntry : theConditionMetaData.entrySet()) {
            if (theEntry.getValue().matches(theCurrentCondition)) {
                theCondition.set(theEntry.getKey());

                clearconditionproperties();

                ConditionMetaData<Condition> theMetaData = (ConditionMetaData<Condition>) theEntry.getValue();
                theMetaData.initEditorFor(theCurrentCondition, conditionproperties());

            }
        }

        theCondition.addChangeListener(aEvent -> {
            ConditionMetaData<Condition> theMetaData = (ConditionMetaData<Condition>) theConditionMetaData.get(theCondition.get());

            Condition theNewCondition = theMetaData.create();
            aRule.conditionProperty().set(theNewCondition);

            clearconditionproperties();
            theMetaData.initEditorFor(theNewCondition, conditionproperties());
        });

        theBinder.add(conditionselection().bindTo(theCondition, theConditionMetaData.keySet().toArray(new String[theConditionMetaData.keySet().size()])));

        clearactions();

        for (Action theAction : aRule.getActions()) {
            for (Map.Entry<String, ActionMetaData<?>> theEntry : theActionMetaData.entrySet()) {
                if (theEntry.getValue().matches(theAction)) {
                    ActionMetaData<Action> theMetaData = (ActionMetaData<Action>) theEntry.getValue();
                    theMetaData.initEditorFor(theAction, actionelements());
                }
            }
        }

        return theBinder;
    }
}