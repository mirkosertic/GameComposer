package de.mirkosertic.gamecomposer;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class GameComposerApplication extends Application {

    private Weld weld;
    private WeldContainer weldContainer;

    @Override
    public void init() throws Exception {
        super.init();
        weld = new Weld();
        weldContainer = weld.initialize();
    }

    @Override
    public void start(Stage aStage) throws Exception {
        weldContainer.instance().select(GameComposerFactory.class).get().createAndStart(aStage, getParameters());
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        weld.shutdown();
    }

    public static void main(String[] args) {

        Weld theWeld = new Weld();
        theWeld.initialize();


        Application.launch(args);
    }
}
