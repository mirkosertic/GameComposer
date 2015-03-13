package de.mirkosertic.gamecomposer.tools;

public class ExporterFactory {

    public static enum ExportFormat {
        GWT, DRAGOME, TEAVM, APK
    }

    public ExporterFactory() {
    }

    public Exporter create(ExportFormat aFormat) {
        switch (aFormat) {
            case GWT:
                return new HTML5Exporter("/gameengine-gwtrenderer.war");
            case DRAGOME:
                return new HTML5Exporter("/gameengine-dragomerenderer.war");
            case TEAVM:
                return new HTML5Exporter("/gameengine-teavmrenderer.war");
            case APK:
                return new APKExporter();
            default:
                throw new IllegalArgumentException("Unknown export format : " + aFormat);
        }
    }
}