package de.mirkosertic.gamecomposer;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FXMLLoaderFactory {

    @Inject
    Instance<Object> instance;

    public FXMLLoader createLoader() {
        FXMLLoader theLoader = new FXMLLoader();
        theLoader.setBuilderFactory(new JavaFXBuilderFactory());
        theLoader.setControllerFactory(param -> instance.select(param).get());
        return theLoader;
    }
}