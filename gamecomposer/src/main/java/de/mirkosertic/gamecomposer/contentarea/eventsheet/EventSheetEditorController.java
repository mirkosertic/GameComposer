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
package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import de.mirkosertic.gamecomposer.FlushResourceCacheEvent;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gamecomposer.ShutdownEvent;
import de.mirkosertic.gamecomposer.contentarea.ContentController;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.event.PropertyChanged;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class EventSheetEditorController implements ContentController<EventSheet> {

    @FXML
    ListView<GameRule> rules;

    @Inject
    RuleEditorControllerFactory ruleEditorControllerFactory;

    private EventSheet editingObject;
    private Node viewNode;
    private Map<GameRule, RuleEditorController> controllerMap;

    EventSheetEditorController initialize(Node aViewNode, EventSheet aEventSheet) {
        viewNode = aViewNode;
        editingObject = aEventSheet;
        controllerMap = new HashMap<>();

        rules.setCellFactory(new Callback<ListView<GameRule>, ListCell<GameRule>>() {
            @Override
            public ListCell<GameRule> call(ListView<GameRule> aListView) {
                return new GameRuleCell();
            }
        });

        initializeRuleList();

        return this;
    }

    private class GameRuleCell extends ListCell<GameRule> {

        private GameRuleCell() {
        }

        @Override
        protected void updateItem(GameRule aRule, boolean empty) {
            if (empty) {
                setGraphic(null);
            } else {
                RuleEditorController theRuleController = controllerMap.get(aRule);
                if (theRuleController != null) {
                    setGraphic(theRuleController.getView());
                } else {
                    setGraphic(null);
                }
            }
        }
    }

    @Override
    public EventSheet getEditingObject() {
        return editingObject;
    }

    @Override
    public void processKeyPressedEvent(KeyEvent aKeyEvent) {
    }

    @Override
    public void processKeyReleasedEvent(KeyEvent aKeyEvent) {
    }

    @Override
    public void addedAsTab() {
    }

    @Override
    public void removed() {
        cleanupControllers();
    }

    @Override
    public void onObjectSelected(ObjectSelectedEvent aEvent) {
    }

    @Override
    public void onShutdown(ShutdownEvent aEvent) {
    }

    @Override
    public void onObjectUpdated(Tab aTab, PropertyChanged aEvent) {
        if (aEvent.getOwner() == editingObject) {
            aTab.setText(editingObject.nameProperty().get());
        }
    }

    @Override
    public void onFlushResourceCache(FlushResourceCacheEvent aEvent) {
    }

    @Override
    public Node getView() {
        return viewNode;
    }

    private void cleanupControllers() {
        for (RuleEditorController theController : controllerMap.values()) {
            theController.removed();
        }
        controllerMap.clear();
    }

    void initializeRuleList() {

        rules.getItems().clear();

        cleanupControllers();
         for (GameRule theRule : editingObject.getRules()) {

            RuleEditorController theRuleController = ruleEditorControllerFactory.createFor(EventSheetEditorController.this, editingObject, theRule);
            controllerMap.put(theRule, theRuleController);

            rules.getItems().add(theRule);
        }
    }

    @FXML
    public void onAddNewRule() {
        GameRule theNewRule = new GameRule();
        editingObject.addRule(theNewRule);
        initializeRuleList();
    }
}