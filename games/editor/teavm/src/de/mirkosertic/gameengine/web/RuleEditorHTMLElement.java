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
import de.mirkosertic.gameengine.core.RunSceneAction;
import de.mirkosertic.gameengine.core.SceneStartedCondition;
import de.mirkosertic.gameengine.core.SpawnGameObjectInstanceAction;
import de.mirkosertic.gameengine.input.KeyEventCondition;
import de.mirkosertic.gameengine.input.MouseEventCondition;
import de.mirkosertic.gameengine.physic.ObjectCollisionCondition;
import de.mirkosertic.gameengine.process.KillProcessesForInstanceAction;
import de.mirkosertic.gameengine.script.RunScriptAction;
import de.mirkosertic.gameengine.sound.PlaySoundAction;
import de.mirkosertic.gameengine.teavm.TeaVMMouseEvent;
import de.mirkosertic.gameengine.type.CollisionPosition;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.ResourceName;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RuleEditorHTMLElement implements HTMLElement {

    interface EventListener {
        void deleteRule(RuleEditorHTMLElement aElement, GameRule aRule);
    }

    abstract class ConditionMetaData<T extends Condition> {

        public abstract boolean matches(Condition aCondition);

        public abstract T create();

        public abstract void initEditorFor(T aCondition, HTMLElement aElement);
    }

    abstract class ActionMetaData<T extends Action> {

        public abstract boolean matches(Action aAction);

        public abstract T create();

        public abstract void initEditorFor(T aAction, HTMLElement aElement);
    }

    @JSBody(params = {}, script = "return document.createElement('gameeditor-ruleeditor');")
    public static native RuleEditorHTMLElement create();

    public abstract TextfieldPropertyEditorHTMLElement rulename();

    public abstract HTMLElement conditionproperties();

    public abstract HTMLElement actionelements();

    public abstract void clearconditionproperties();

    public abstract void clearactions();

    public abstract HTMLElement addactionmenu();

    public abstract HTMLElement changeconditionmenu();

    public List<HTMLInputBinder> bindTo(GameRule aRule, EventSheet aEventSheet,  Editor aEditor, EventListener aListener) {

        final Map<String, ConditionMetaData<? extends Condition>> theConditionMetaData = new HashMap<>();
        theConditionMetaData.put("Instance removed from scene", new ConditionMetaData<GameObjectInstanceRemovedFromSceneCondition>() {
            @Override
            public GameObjectInstanceRemovedFromSceneCondition create() {
                return new GameObjectInstanceRemovedFromSceneCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof GameObjectInstanceRemovedFromSceneCondition;
            }

            @Override
            public void initEditorFor(GameObjectInstanceRemovedFromSceneCondition aCondition, HTMLElement aElement) {

                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();

                theElement.addText("If object of type ");
                theElement.addSelection(aCondition.gameObjectProperty(), aEventSheet.getGameScene().getObjects(), t -> t.nameProperty().get());
                theElement.addText(" was removed from scene");

                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theConditionMetaData.put("Key event", new ConditionMetaData<KeyEventCondition>() {
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

                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();

                theElement.addText("If key ");
                theElement.addSelection(aCondition.keyCodeProperty(), GameKeyCode.values(),
                        aValue -> aValue.name());
                theElement.addText(" is ");;
                theElement.addSelection(aCondition.eventTypeProperty(), KeyEventCondition.KeyEventType.values(),
                        aValue -> aValue.name());

                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theConditionMetaData.put("Instance added to scene", new ConditionMetaData<GameObjectInstanceAddedToSceneCondition>() {
            @Override
            public GameObjectInstanceAddedToSceneCondition create() {
                return new GameObjectInstanceAddedToSceneCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof GameObjectInstanceAddedToSceneCondition;
            }

            @Override
            public void initEditorFor(GameObjectInstanceAddedToSceneCondition aCondition, HTMLElement aElement) {
                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();

                theElement.addText("If object of type ");
                theElement.addSelection(aCondition.gameObjectProperty(), aEventSheet.getGameScene().getObjects(), t -> t.nameProperty().get());
                theElement.addText(" was added to the scene");

                Polymer.dom(aElement).appendChild(theElement);

            }
        });
        theConditionMetaData.put("System tick", new ConditionMetaData<SystemTickCondition>() {
            @Override
            public SystemTickCondition create() {
                return new SystemTickCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof SystemTickCondition;
            }

            @Override
            public void initEditorFor(SystemTickCondition aCondition, HTMLElement aElement) {
                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();

                theElement.addText("Every ");
                theElement.addInput(aCondition.everyTicksProperty(), t -> Long.toString(t));
                theElement.addText(" system ticks");

                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theConditionMetaData.put("Scene started", new ConditionMetaData<SceneStartedCondition>() {
            @Override
            public SceneStartedCondition create() {
                return new SceneStartedCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof SceneStartedCondition;
            }

            @Override
            public void initEditorFor(SceneStartedCondition aCondition, HTMLElement aElement) {
                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();
                theElement.addText("If the scene was started");

                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theConditionMetaData.put("Mouse event", new ConditionMetaData<MouseEventCondition>() {
            @Override
            public MouseEventCondition create() {
                return new MouseEventCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof MouseEventCondition;
            }

            @Override
            public void initEditorFor(MouseEventCondition aCondition, HTMLElement aElement) {
                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();
                theElement.addText("If a mouse event of type ");
                theElement.addSelection(aCondition.eventTypeProperty(), MouseEventCondition.MouseEventType.values(), t -> t.name());
                theElement.addText(" filtered by ");
                theElement.addSelection(aCondition.filterTypeProperty(), MouseEventCondition.FilterType.values(), t -> t.name());
                theElement.addText(" occours on object of type ");
                theElement.addSelection(aCondition.objectTypeProperty(), aEventSheet.getGameScene().getObjects(), t -> t.nameProperty().get());

                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theConditionMetaData.put("Object collision", new ConditionMetaData<ObjectCollisionCondition>() {
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

                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();

                theElement.addText("If object of type ");
                theElement.addSelection(aCondition.primaryObjectProperty(), aEventSheet.getGameScene().getObjects(), t -> t.nameProperty().get());
                theElement.addText(" collides with object of type ");
                theElement.addSelection(aCondition.secondaryObjectProperty(), aEventSheet.getGameScene().getObjects(), t -> t.nameProperty().get());
                theElement.addText(" at ");
                theElement.addSelection(aCondition.positionProperty(), CollisionPosition.values(), t -> t.name());

                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theConditionMetaData.put("Instance left layout", new ConditionMetaData<GameObjectInstanceLeftLayoutCondition>() {
            @Override
            public GameObjectInstanceLeftLayoutCondition create() {
                return new GameObjectInstanceLeftLayoutCondition();
            }

            @Override
            public boolean matches(Condition aCondition) {
                return aCondition instanceof GameObjectInstanceLeftLayoutCondition;
            }

            @Override
            public void initEditorFor(GameObjectInstanceLeftLayoutCondition aCondition, HTMLElement aElement) {
                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();
                theElement.addText("If game object of type ");
                theElement.addSelection(aCondition.gameObjectProperty(), aEventSheet.getGameScene().getObjects(), t -> t.nameProperty().get());
                theElement.addText(" left the scene layout");

                Polymer.dom(aElement).appendChild(theElement);
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
            public void initEditorFor(DeleteGameObjectInstanceAction aAction, HTMLElement aElement) {
                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();
                theElement.addDeleteImage().addEventListener("click", evt -> {
                    aRule.removeAction(aAction);
                    Polymer.dom(aElement).removeChild(theElement);

                    Toast.info("Action deleted.");
                });
                theElement.addText("then delete the affected instances");

                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theActionMetaData.put("Run scene", new ActionMetaData<RunSceneAction>() {
            @Override
            public boolean matches(Action aAction) {
                return aAction instanceof RunSceneAction;
            }

            @Override
            public RunSceneAction create() {
                return new RunSceneAction();
            }

            @Override
            public void initEditorFor(RunSceneAction aAction, HTMLElement aElement) {
                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();
                theElement.addDeleteImage().addEventListener("click", evt -> {
                    aRule.removeAction(aAction);
                    Polymer.dom(aElement).removeChild(theElement);

                    Toast.info("Action deleted.");
                });
                theElement.addText("then run scene ");
                theElement.addSelection(aAction.gameSceneProperty(), aEventSheet.getGameScene().getGame().getKnownScenes(), aValue -> aValue);
                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theActionMetaData.put("Play sound", new ActionMetaData<PlaySoundAction>() {
            @Override
            public boolean matches(Action aAction) {
                return aAction instanceof PlaySoundAction;
            }

            @Override
            public PlaySoundAction create() {
                return new PlaySoundAction();
            }

            @Override
            public void initEditorFor(PlaySoundAction aAction, HTMLElement aElement) {
                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();
                theElement.addDeleteImage().addEventListener("click", evt -> {
                    aRule.removeAction(aAction);
                    Polymer.dom(aElement).removeChild(theElement);

                    Toast.info("Action deleted.");
                });

                theElement.addText("then play sound ");
                theElement.addInput(aAction.resourceNameProperty(), new ObjectConverter<ResourceName>() {
                    @Override
                    public ResourceName convertTo(String aValue) {
                        return new ResourceName(aValue);
                    }

                    @Override
                    public String convertFrom(ResourceName aValue) {
                        return aValue.get();
                    }
                });
                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theActionMetaData.put("Spawn game object instance", new ActionMetaData<SpawnGameObjectInstanceAction>() {
            @Override
            public boolean matches(Action aAction) {
                return aAction instanceof SpawnGameObjectInstanceAction;
            }

            @Override
            public SpawnGameObjectInstanceAction create() {
                return new SpawnGameObjectInstanceAction();
            }

            @Override
            public void initEditorFor(SpawnGameObjectInstanceAction aAction, HTMLElement aElement) {
                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();
                theElement.addDeleteImage().addEventListener("click", evt -> {
                    aRule.removeAction(aAction);
                    Polymer.dom(aElement).removeChild(theElement);

                    Toast.info("Action deleted.");
                });

                theElement.addText("then spawn an object of type ");
                theElement.addSelection(aAction.gameObjectProperty(), aEventSheet.getGameScene().getObjects(), aValue -> aValue.nameProperty().get());
                theElement.addText("next to reference object ");
                theElement.addSelection(aAction.referenceObjectProperty(), aEventSheet.getGameScene().getObjects(), aValue -> aValue.nameProperty().get());
                theElement.addText(" with x offset ");
                theElement.addInput(aAction.offsetProperty(), new ObjectConverter<Position>() {
                    @Override
                    public Position convertTo(String aValue) {
                        return aAction.offsetProperty().get().changeX(Float.parseFloat(aValue));
                    }

                    @Override
                    public String convertFrom(Position aValue) {
                        return Integer.toString((int) aValue.x);
                    }
                });
                theElement.addText(" and y offset ");
                theElement.addInput(aAction.offsetProperty(), new ObjectConverter<Position>() {
                    @Override
                    public Position convertTo(String aValue) {
                        return aAction.offsetProperty().get().changeY(Float.parseFloat(aValue));
                    }

                    @Override
                    public String convertFrom(Position aValue) {
                        return Integer.toString((int) aValue.y);
                    }
                });

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

                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();
                theElement.addDeleteImage().addEventListener("click", evt -> {
                    aRule.removeAction(aAction);
                    Polymer.dom(aElement).removeChild(theElement);

                    Toast.info("Action deleted.");
                });

                theElement.addText("then execute the following ");
                theElement.addAction("LUA Script", evt -> aEditor.editLUAScriptProperty(aAction.scriptProperty()));

                Polymer.dom(aElement).appendChild(theElement);
            }
        });
        theActionMetaData.put("Kill all running processes for instance", new ActionMetaData<KillProcessesForInstanceAction>() {
            @Override
            public boolean matches(Action aAction) {
                return aAction instanceof KillProcessesForInstanceAction;
            }

            @Override
            public KillProcessesForInstanceAction create() {
                return new KillProcessesForInstanceAction();
            }

            @Override
            public void initEditorFor(KillProcessesForInstanceAction aAction, HTMLElement aElement) {
                GenericNaturalLanguageEditorElement theElement = GenericNaturalLanguageEditorElement.create();
                theElement.addDeleteImage().addEventListener("click", evt -> {
                    aRule.removeAction(aAction);
                    Polymer.dom(aElement).removeChild(theElement);

                    Toast.info("Action deleted.");
                });

                theElement.addText("then kill all running processes for the affected instances");

                Polymer.dom(aElement).appendChild(theElement);
            }
        });

        List<HTMLInputBinder> theBinder = new ArrayList<>();
        rulename().addDeleteListener(evt -> {
            aListener.deleteRule(this, aRule);
        });

        changeconditionmenu().addEventListener("click", aClickEvent -> {
            ContextMenuHTMLElement theContextMenu = ContextMenuHTMLElement.create();
            theContextMenu.setTitle("Change condition to");

            for (Map.Entry<String, ConditionMetaData<? extends Condition>> theEntry : theConditionMetaData.entrySet()) {
                ConditionMetaData theMetaData = theEntry.getValue();
                if (!theMetaData.matches(aRule.conditionProperty().get())) {
                    ContextMenuItemHTMLElement theItem = ContextMenuItemHTMLElement.create();
                    theItem.setText(theEntry.getKey());
                    theItem.addEventListener("click", (aEvent) -> {

                        theContextMenu.hide();

                        Condition theNewCondition = theMetaData.create();
                        aRule.conditionProperty().set(theNewCondition);

                        clearconditionproperties();

                        theMetaData.initEditorFor(theNewCondition, conditionproperties());
                    });

                    theContextMenu.add(theItem);
                }
            }

            theContextMenu.showAt((TeaVMMouseEvent) aClickEvent);
        });

        addactionmenu().addEventListener("click", aClickEvent -> {
            ContextMenuHTMLElement theContextMenu = ContextMenuHTMLElement.create();
            theContextMenu.setTitle("Add action of type");

            for (Map.Entry<String, ActionMetaData<? extends Action>> theEntry : theActionMetaData.entrySet()) {
                ContextMenuItemHTMLElement theItem = ContextMenuItemHTMLElement.create();
                theItem.setText(theEntry.getKey());
                theItem.addEventListener("click", (aEvent) -> {

                    theContextMenu.hide();

                    ActionMetaData theMeta = theEntry.getValue();
                    Action theAction = theMeta.create();
                    aRule.addAction(theAction);

                    theMeta.initEditorFor(theAction, actionelements());
                });

                theContextMenu.add(theItem);
            }

            theContextMenu.showAt((TeaVMMouseEvent) aClickEvent);
        });

        theBinder.add(rulename().bindTo(aRule.nameProperty(), new GameObjectEditor.StringStringConverter()));

        Condition theCurrentCondition = aRule.conditionProperty().get();

        for (Map.Entry<String, ConditionMetaData<?>> theEntry : theConditionMetaData.entrySet()) {
            if (theEntry.getValue().matches(theCurrentCondition)) {
                clearconditionproperties();

                ConditionMetaData<Condition> theMetaData = (ConditionMetaData<Condition>) theEntry.getValue();
                theMetaData.initEditorFor(theCurrentCondition, conditionproperties());

            }
        }

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