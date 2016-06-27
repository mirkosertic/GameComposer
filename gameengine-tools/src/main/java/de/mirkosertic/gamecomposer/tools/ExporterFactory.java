package de.mirkosertic.gamecomposer.tools;

public class ExporterFactory {

    public static enum ExportFormat {
        GWT, TEAVM, APK, DRAGOME
    }

    public ExporterFactory() {
    }

    public Exporter create(ExportFormat aFormat) {
        switch (aFormat) {
            case GWT:
                return new HTML5Exporter("/gameengine-gwtrenderer.war");
            case TEAVM:
                return new HTML5Exporter("/gameengine-teavmrenderer.war");
            case DRAGOME:
                return new HTML5Exporter("/gameengine-dragomerenderer.war");
            case APK:
                return new APKExporter();
            default:
                throw new IllegalArgumentException("Unknown export format : " + aFormat);
        }
    }
}