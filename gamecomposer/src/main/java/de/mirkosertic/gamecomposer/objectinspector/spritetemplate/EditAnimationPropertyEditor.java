package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.javafx.JavaFXBitmapResource;
import de.mirkosertic.gameengine.type.Animation;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class EditAnimationPropertyEditor implements PropertyEditor<Animation> {

    private final AnimationPropertySheetItem item;
    private final VBox editor;
    private final ImageView imageView;

    public EditAnimationPropertyEditor(PropertySheet.Item aItem) {
        item = (AnimationPropertySheetItem) aItem;
        imageView = new ImageView();

        Hyperlink theEditAnimation = new Hyperlink("Edit animation...");
        theEditAnimation.setOnAction(aEvent -> {
            Animation theNewAnimation = item.editAnimationSequence((Node) aEvent.getSource());
            if (theNewAnimation != null) {
                setValue(theNewAnimation);
            }
        });

        editor = new VBox();
        editor.getChildren().addAll(imageView, theEditAnimation);
    }

    @Override
    public Node getEditor() {
        return editor;
    }

    @Override
    public Animation getValue() {
        return item.getValue();
    }

    @Override
    public void setValue(Animation aValue) {
        item.setValue(aValue);
        GameScene theGameScene = item.getTemplate().getGameScene();
        GameResourceLoader theLoader = item.getPersistenceManager().createResourceLoaderFor(theGameScene);
        try {
            JavaFXBitmapResource theResource = (JavaFXBitmapResource) theLoader.load(aValue.computeCurrentFrame(0, 5));
            imageView.setImage(theResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}