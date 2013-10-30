package de.mirkosertic.gamecomposer.objectinspector;

import com.dooapp.fxform.FXForm;
import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ObjectInspectorController implements ChildController {

    @FXML
    Accordion propertiesAccordion;

    private Node view;

    ObjectInspectorController initialize(Node aView) {
        propertiesAccordion.getPanes().clear();
        view = aView;
        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    public void onObjectSelected(@Observes ObjectSelectedEvent aEvent) {
        propertiesAccordion.getPanes().clear();
        Object theSelectedObject = aEvent.getSelectedObject();
        if (theSelectedObject != null) {
            FXForm thePropertiesForm = new FXForm(theSelectedObject);
            TitledPane theCorePropertiesPane = new TitledPane("Properties", thePropertiesForm);
            propertiesAccordion.getPanes().add(theCorePropertiesPane);

            if (theSelectedObject instanceof GameObject) {
                GameObject theGameObject = (GameObject) theSelectedObject;
                for (GameComponentTemplate theTemplate : theGameObject.getComponentTemplates()) {
                    FXForm theForm = new FXForm(theTemplate);
                    TitledPane theTitlePane = new TitledPane(theTemplate.getTypeKey(), theForm);
                    propertiesAccordion.getPanes().add(theTitlePane);
                }
            }

            if (theSelectedObject instanceof GameObjectInstance) {
                GameObjectInstance theGameObjectInstance = (GameObjectInstance) theSelectedObject;
                for (GameComponent theComponent : theGameObjectInstance.getComponents()) {
                    FXForm theForm = new FXForm(theComponent);
                    TitledPane theTitlePane = new TitledPane(theComponent.getTypeKey(), theForm);
                    propertiesAccordion.getPanes().add(theTitlePane);
                }
            }

            propertiesAccordion.setExpandedPane(theCorePropertiesPane);
        }
    }
}
