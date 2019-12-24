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
package de.mirkosertic.gameengine.web;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.teavm.tooling.TeaVMTool;
import org.teavm.tooling.TeaVMToolException;
import org.teavm.tooling.TeaVMToolLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping(path = "/teavm")
public class EditorBackendTeaVMEndpoint {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(EditorBackendTeaVMEndpoint.class);

    @RequestMapping(path = "/classes.js" ,method = RequestMethod.GET)
    @ResponseBody
    public byte[] downloadByteCode() throws TeaVMToolException, IOException {

        File theParent = FileUtils.getTempDirectory();

        File theTempDir = new File(theParent, "teavmtemp");
        theTempDir.mkdirs();

        LOG.info("Using temp directory {} for JavaScript code generation", theTempDir);

        TeaVMTool theTool = new TeaVMTool();
        theTool.setClassLoader(getClass().getClassLoader());
        theTool.setMinifying(false);
        theTool.setMainClass(WebUI.class.getName());
        theTool.setSourceFilesCopied(false);
        theTool.setSourceMapsFileGenerated(false);
        theTool.setIncremental(false);
        theTool.setTargetDirectory(theTempDir);
        theTool.setLog(new TeaVMToolLog() {
            @Override
            public void info(String text) {
                LOG.info(text);
            }

            @Override
            public void debug(String text) {
                LOG.debug(text);
            }

            @Override
            public void warning(String text) {
                LOG.warn(text);
            }

            @Override
            public void error(String text) {
                LOG.error(text);
            }

            @Override
            public void info(String text, Throwable e) {
                LOG.info(text, e);
            }

            @Override
            public void debug(String text, Throwable e) {
                LOG.debug(text, e);
            }

            @Override
            public void warning(String text, Throwable e) {
                LOG.warn(text, e);
            }

            @Override
            public void error(String text, Throwable e) {
                LOG.error(text, e);
            }
        });
        theTool.generate();

        return IOUtils.toByteArray(new FileInputStream(new File(theTempDir, "classes.js")));
    }
}
