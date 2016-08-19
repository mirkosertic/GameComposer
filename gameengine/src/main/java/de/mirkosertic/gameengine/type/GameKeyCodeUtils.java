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
package de.mirkosertic.gameengine.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameKeyCodeUtils {
    public static GameKeyCode fromChar(char aCharacter) {
        switch(Character.toUpperCase(aCharacter)) {
            case 13:
                return GameKeyCode.ENTER;
            case 8:
                return GameKeyCode.BACK_SPACE;
            case 9:
                return GameKeyCode.TAB;
            case 27:
                return GameKeyCode.ESCAPE;
            case ' ':
                return GameKeyCode.SPACE;
            case ',':
                return GameKeyCode.COMMA;
            case '-':
                return GameKeyCode.MINUS;
            case '.':
                return GameKeyCode.PERIOD;
            case '/':
                return GameKeyCode.SLASH;
            case '0':
                return GameKeyCode.DIGIT0;
            case '1':
                return GameKeyCode.DIGIT1;
            case '2':
                return GameKeyCode.DIGIT2;
            case '3':
                return GameKeyCode.DIGIT3;
            case '4':
                return GameKeyCode.DIGIT4;
            case '5':
                return GameKeyCode.DIGIT5;
            case '6':
                return GameKeyCode.DIGIT6;
            case '7':
                return GameKeyCode.DIGIT7;
            case '8':
                return GameKeyCode.DIGIT8;
            case '9':
                return GameKeyCode.DIGIT9;
            case ';':
                return GameKeyCode.SEMICOLON;
            case '=':
                return GameKeyCode.EQUALS;
            case 'A':
                return GameKeyCode.A;
            case 'B':
                return GameKeyCode.B;
            case 'C':
                return GameKeyCode.C;
            case 'D':
                return GameKeyCode.D;
            case 'E':
                return GameKeyCode.E;
            case 'F':
                return GameKeyCode.F;
            case 'G':
                return GameKeyCode.G;
            case 'H':
                return GameKeyCode.H;
            case 'I':
                return GameKeyCode.I;
            case 'J':
                return GameKeyCode.J;
            case 'K':
                return GameKeyCode.K;
            case 'L':
                return GameKeyCode.L;
            case 'M':
                return GameKeyCode.M;
            case 'N':
                return GameKeyCode.N;
            case 'O':
                return GameKeyCode.O;
            case 'P':
                return GameKeyCode.P;
            case 'Q':
                return GameKeyCode.Q;
            case 'R':
                return GameKeyCode.R;
            case 'S':
                return GameKeyCode.S;
            case 'T':
                return GameKeyCode.T;
            case 'U':
                return GameKeyCode.U;
            case 'V':
                return GameKeyCode.V;
            case 'W':
                return GameKeyCode.W;
            case 'X':
                return GameKeyCode.X;
            case 'Y':
                return GameKeyCode.Y;
            case 'Z':
                return GameKeyCode.Z;
            case '[':
                return GameKeyCode.OPEN_BRACKET;
            case '\\':
                return GameKeyCode.BACK_SLASH;
            case ']':
                return GameKeyCode.CLOSE_BRACKET;
            case '&':
                return GameKeyCode.AMPERSAND;
            case '*':
                return GameKeyCode.ASTERISK;
            case '"':
                return GameKeyCode.QUOTEDBL;
            case '<':
                return GameKeyCode.LESS;
            case '>':
                return GameKeyCode.GREATER;
            case '(':
                return GameKeyCode.BRACELEFT;
            case ')':
                return GameKeyCode.BRACERIGHT;
            case '@':
                return GameKeyCode.AT;
            case ':':
                return GameKeyCode.COLON;
            case '^':
                return GameKeyCode.CIRCUMFLEX;
            case '$':
                return GameKeyCode.DOLLAR;
            case '!':
                return GameKeyCode.EXCLAMATION_MARK;
            case '+':
                return GameKeyCode.PLUS;
            case '{':
                return GameKeyCode.LEFT_PARENTHESIS;
            case '}':
                return GameKeyCode.RIGHT_PARENTHESIS;
            case '_':
                return GameKeyCode.UNDERSCORE;
        }
        return null;
    }

    public static List<GameKeyCode> allKeysAsSortedList() {
        List<GameKeyCode> theResult = Arrays.asList(GameKeyCode.values());
        Collections.sort(theResult, new Comparator<GameKeyCode>() {
            @Override
            public int compare(GameKeyCode o1, GameKeyCode o2) {
                return o1.name().compareTo(o2.name());
            }
        });
        return theResult;
    }
}
