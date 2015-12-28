package de.mirkosertic.gamecomposer.objectinspector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import javax.inject.Singleton;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Qualifier
@Singleton
public @interface ObjectInspectorElementConfiguratorType {
    Class clazz();
}
