package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

public class Editor {

    private static final Window window = Window.current();
    private static final HTML5Document document = (HTML5Document) window.getDocument();

    public void boot(EditorProject aProject) {
        EditorHTMLCanvasElement theCanvasElement = (EditorHTMLCanvasElement) document.getElementById("html5canvas");

        // Initialize object editor
        HTMLElement thePropertyEditorElement = (HTMLElement) document.getElementById("objectEditor");
        HTMLElement theTreeElement = (HTMLElement) document.getElementById("objecttree");
        HTMLTemplateEngine theTemplateEngine = new HTMLTemplateEngine(document);
        GameObjectEditor theObjectEditor = new GameObjectEditor(thePropertyEditorElement, theTemplateEngine);
        GameTreeView theTreeView = new GameTreeView(theTreeElement, theTemplateEngine, theObjectEditor);

        GameEditor theGameEditor = new GameEditor(theCanvasElement, window, aProject) {

            @Override
            protected void playScene(GameScene aGameScene) {
                theTreeView.onGameSceneLoaded(aGameScene);
                super.playScene(aGameScene);
            }

            @Override
            protected void setSelectedInstance(GameObjectInstance aInstance) {
                super.setSelectedInstance(aInstance);
                theObjectEditor.setEditingObject(aInstance);
                theTreeView.setEditingObject(aInstance);
            }
        };

        window.addEventListener("resize", evt -> {
            theGameEditor.handleResize();
        }, true);

        theObjectEditor.clear();
    }
}
