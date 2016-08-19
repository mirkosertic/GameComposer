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
package de.mirkosertic.gamecomposer.objectinspector.gameobjectinstance;

import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.AbsolutePositionAnchorPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.AnglePropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.PositionPropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.SizePropertyEditor;
import de.mirkosertic.gamecomposer.objectinspector.utils.StringPropertyEditor;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.controlsfx.control.PropertySheet;

@ObjectInspectorElementConfiguratorType(clazz = GameObjectInstance.class)
public class GameObjectInstanceElementConfigurator implements ObjectInspectorElementConfigurator<GameObjectInstance> {

    private static final String CATEGORY_NAME_GAMEOBJECT = "03 GameObject";
    private static final String CATEGORY_NAME = "05 Instance";

    @Inject
    MessageBox messageBox;

    @Override
    public List<PropertySheet.Item> getItemsFor(final GameObjectInstance aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();

        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME_GAMEOBJECT, aObject.getOwnerGameObject().nameProperty(), "Name", "The name of the object", Optional.of(StringPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME_GAMEOBJECT, aObject.getOwnerGameObject().sizeProperty(), "Size", "The size of the object", Optional.of(SizePropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME_GAMEOBJECT, aObject.getOwnerGameObject().visibleProperty(), "Visible", "The visibility of the object"));

        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.nameProperty(), "Name", "The name of the scene", Optional.of(StringPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.positionProperty(), "Position", "The position of the instannce", Optional.of(PositionPropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.visibleProperty(), "Visible", "Is the instance visible or not", Optional.empty()));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.rotationAngleProperty(), "Rotation angle", "The rotation angle of the instance", Optional.of(AnglePropertyEditor.class)));
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.positionAnchorProperty(), "Position anchor", "The position anchor of the instance", Optional.of(AbsolutePositionAnchorPropertyEditor.class)));

        ActionPropertyEditorItem theActions = new ActionPropertyEditorItem(CATEGORY_NAME, "", "Available actions");
        theActions.addAction(new ActionPropertyEditorItem.Action("Delete object...", aEvent -> {
            if (messageBox.showMessageBox((Node) aEvent.getSource(), "Delete object", "Do you really want to delete this item?", MessageBox.ButtonType.YES, MessageBox.ButtonType.NO) == MessageBox.ButtonType.YES) {
                aObject.getOwnerGameObject().getGameScene().removeGameObjectInstance(aObject);
            }
        }));
        theResult.add(theActions);

        return theResult;
    }
}