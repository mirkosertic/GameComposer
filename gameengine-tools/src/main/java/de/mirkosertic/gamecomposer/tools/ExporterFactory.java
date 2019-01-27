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
package de.mirkosertic.gamecomposer.tools;

public class ExporterFactory {

    public enum ExportFormat {
        GWT, TEAVM, WASM, APK, DRAGOME
    }

    public ExporterFactory() {
    }

    public Exporter create(final ExportFormat aFormat) {
        switch (aFormat) {
            case GWT:
                return new HTML5Exporter("/gameengine-gwtrenderer.war");
            case TEAVM:
                return new HTML5Exporter("/gameengine-teavmrenderer.war");
            case WASM:
                return new HTML5Exporter("/gameengine-bytecoderrenderer.war");
            case DRAGOME:
                return new HTML5Exporter("/gameengine-dragomerenderer.war");
            case APK:
                return new APKExporter();
            default:
                throw new IllegalArgumentException("Unknown export format : " + aFormat);
        }
    }
}