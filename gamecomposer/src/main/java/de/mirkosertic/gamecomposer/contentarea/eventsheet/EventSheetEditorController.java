package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import de.mirkosertic.gamecomposer.FlushResourceCacheEvent;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gamecomposer.ShutdownEvent;
import de.mirkosertic.gamecomposer.contentarea.ContentChildController;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.event.PropertyChanged;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class EventSheetEditorController implements ContentChildController<EventSheet> {

    @FXML
    VBox rulesList;

    @Inject
    RuleEditorControllerFactory ruleEditorControllerFactory;

    private EventSheet editingObject;
    private Node viewNode;
    private List<RuleEditorController> controllerList;

    EventSheetEditorController initialize(Node aViewNode, EventSheet aEventSheet) {
        viewNode = aViewNode;
        editingObject = aEventSheet;
        controllerList = new ArrayList<>();

        initializeRuleList();

        return this;
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

    void initializeRuleList() {
        rulesList.getChildren().clear();
        for (RuleEditorController theController : controllerList) {
            theController.removed();
        }
        controllerList.clear();
        for (GameRule theRule : editingObject.getRules()) {
            RuleEditorController theRuleController = ruleEditorControllerFactory.createFor(this, editingObject, theRule);
            rulesList.getChildren().add(theRuleController.getView());

            controllerList.add(theRuleController);
        }
    }

    @FXML
    public void onAddNewRule() {
        GameRule theNewRule = new GameRule();
        editingObject.addRule(theNewRule);
        initializeRuleList();
    }
}