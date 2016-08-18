package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.CustomProperties;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.HashMap;
import java.util.Map;

public class Game implements Reflectable {

    private static final GameClassInformation CIINSTANCE = new GameClassInformation();

    public static final String NAME_PROPERTY = "name";
    public static final String DEFAULT_SCENE_PROPERTY = "defaultScene";
    public static final String ENABLE_WEB_GL_PROPERTY = "enableWebGL";
    public static final String CUSTOM_PROPERTIES_PROPERTY = "customProperties";
    public static final String ENABLE_DEBUG_PROPERTY = "enableDebug";
    public static final String ENABLE_NETWORKING_PROPERTY = "enableNetworking";
    public static final String FIREBASE_URL = "firebaseURL";

    private final Property<String> name;
    private final Property<String> defaultScene;
    private final Property<Boolean> enableWebGL;
    private final Property<CustomProperties> customProperties;
    private final Property<Boolean> enableDebug;

    private final Property<Boolean> enableNetworking;
    private final Property<String> fireBaseURL;

    public Game() {
        name = new Property<>(String.class, this, NAME_PROPERTY, (String) null);
        defaultScene = new Property<>(String.class, this, DEFAULT_SCENE_PROPERTY, (String) null);
        enableWebGL = new Property<>(Boolean.class, this, ENABLE_WEB_GL_PROPERTY, Boolean.TRUE);
        enableDebug = new Property<>(Boolean.class, this, ENABLE_DEBUG_PROPERTY, Boolean.FALSE);
        customProperties = new Property<>(CustomProperties.class, this, CUSTOM_PROPERTIES_PROPERTY, new CustomProperties());

        enableNetworking = new Property<>(Boolean.class, this, ENABLE_NETWORKING_PROPERTY, Boolean.FALSE);
        fireBaseURL = new Property<>(String.class, this, FIREBASE_URL, "https://glowing-heat-2189.firebaseio.com");
    }

    @ReflectiveField
    public Property<String> nameProperty() {
        return name;
    }

    @ReflectiveField
    public Property<String> defaultSceneProperty() {
        return defaultScene;
    }

    @ReflectiveField
    public Property<Boolean> enableWebGLProperty() {
        return enableWebGL;
    }

    @ReflectiveField
    public Property<Boolean> enableDebugProperty() {
        return enableDebug;
    }

    @ReflectiveField
    public Property<Boolean> enableNetworkingProperty() {
        return enableNetworking;
    }

    @ReflectiveField
    public Property<String> fireBaseURLProperty() {
        return fireBaseURL;
    }

    public void removeScene(String aSceneName) {
        if (aSceneName.equals(defaultScene.get())) {
            defaultScene.set(null);
        }
    }

    @ReflectiveField
    public Property<CustomProperties> customPropertiesProperty() {
        return customProperties;
    }

    @Override
    public GameClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(NAME_PROPERTY, name.get());
        theResult.put("defaultscene", defaultScene.get());
        theResult.put("enablewebgl", Boolean.toString(enableWebGL.get()));
        theResult.put("customProperties", customProperties.get().serialize());
        theResult.put("enableDebug", Boolean.toString(enableDebug.get()));
        theResult.put("enableNetworking", Boolean.toString(enableNetworking.get()));
        theResult.put("firebaseURL", fireBaseURL.get());
        return theResult;
    }

    public static Game deserialize(Map<String, Object> aSerializedData) {
        Game theResult = new Game();
        theResult.name.setQuietly((String) aSerializedData.get(NAME_PROPERTY));
        theResult.defaultScene.setQuietly((String) aSerializedData.get("defaultscene"));

        String theWebGl = (String) aSerializedData.get("enablewebgl");
        if (theWebGl != null) {
            theResult.enableWebGL.setQuietly(Boolean.valueOf(theWebGl));
        }
        String theDebug = (String) aSerializedData.get("enableDebug");
        if (theDebug != null) {
            theResult.enableDebug.setQuietly(Boolean.valueOf(theDebug));
        }
        Map<String, Object> theCustomProperties = (Map<String, Object>) aSerializedData.get("customProperties");
        if (theCustomProperties != null) {
            theResult.customProperties.setQuietly(CustomProperties.deserialize(theCustomProperties));
        }

        String theNetworkingEnabled = (String) aSerializedData.get("enableNetworking");
        if (theNetworkingEnabled != null) {
            theResult.enableNetworking.setQuietly(Boolean.parseBoolean(theNetworkingEnabled));
        }
        String theFirebaseURL = (String) aSerializedData.get("firebaseURL");
        if (theFirebaseURL != null) {
            theResult.fireBaseURL.setQuietly(theFirebaseURL);
        }

        return theResult;
    }
}