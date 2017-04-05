/*
 * Copyright 2017 Mirko Sertic
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
package de.mirkosertic.gameengine.teavmwasm;

import java.util.HashMap;
import java.util.Map;

public class JSONParser {

    private class TokenHandler {

        private Map<String, Object> data;
        private StringBuilder currentToken;
        private String lastToken;
        private boolean inString;
        private boolean inValue;
        private final TokenHandler parent;

        public TokenHandler() {
            this(null, null);
        }

        private TokenHandler(TokenHandler aParent) {
            this(aParent, new HashMap<>());
        }

        private TokenHandler(TokenHandler aParent, Map<String, Object> aData) {
            data = aData;
            currentToken = new StringBuilder();
            inString = false;
            inValue = false;
            parent = aParent;
        }

        public TokenHandler handle(char aChar) {
            if (inString) {
                switch (aChar) {
                    case '\"':
                        inString = false;
                        if (inValue) {
                            data.put(lastToken, currentToken.toString());
                            inValue = false;
                        } else {
                            lastToken = currentToken.toString();
                        }
                        return this;
                    default:
                        currentToken.append(aChar);
                }
                return this;
            }
            switch (aChar) {
                case '\"':
                    inString = true;
                    currentToken = new StringBuilder();
                    return this;
                case '{':
                    if (data == null) {
                        data = new HashMap<>();
                    } else {
                        return new TokenHandler(this);
                    }
                    break;
                case ':':
                    inValue = true;
                    break;
                case ',':
                    inValue = false;
                    inString = false;
                    currentToken = new StringBuilder();
                case '\n':
                    break;
                case '\r':
                    break;
                case '\t':
                    break;
                case ' ':
                    break;
                case '}':
                    if (parent != null) {
                        return parent.resolveChild(this);
                    }
                    break;
                default:
                    currentToken.append(aChar);
            }
            return this;
        }

        public Map<String, Object> toMap() {
            return data;
        }

        private TokenHandler resolveChild(TokenHandler aChild) {
            data.put(currentToken.toString(), aChild.toMap());
            return this;
        }
    }

    public Map<String, Object> fromJSON(String aValue) {
        TokenHandler theHandler = new TokenHandler();
        for (int i=0;i<aValue.length();i++) {
            theHandler = theHandler.handle(aValue.charAt(i));
        }
        return theHandler.toMap();
    }
}