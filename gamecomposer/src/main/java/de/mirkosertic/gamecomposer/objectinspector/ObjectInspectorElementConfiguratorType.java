package de.mirkosertic.gamecomposer.objectinspector;

import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Qualifier
@Singleton
public @interface ObjectInspectorElementConfiguratorType {
    Class clazz();
}
