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
package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.type.GameKeyCode;

class DragomeKeyCodeTranslator {

    private DragomeKeyCodeTranslator() {
    }

    public static GameKeyCode translate(String aKeyIdentifier) {
        switch (aKeyIdentifier) {
        // Cursor keys
        case "37":
            return GameKeyCode.LEFT;
        case "39":
            return GameKeyCode.RIGHT;
        case "38":
            return GameKeyCode.UP;
        case "40":
            return GameKeyCode.DOWN;
        // Digits 0 .. 9
        case "48":
            return GameKeyCode.DIGIT0;
        case "49":
            return GameKeyCode.DIGIT1;
        case "50":
            return GameKeyCode.DIGIT2;
        case "51":
            return GameKeyCode.DIGIT3;
        case "52":
            return GameKeyCode.DIGIT4;
        case "53":
            return GameKeyCode.DIGIT5;
        case "54":
            return GameKeyCode.DIGIT6;
        case "55":
            return GameKeyCode.DIGIT7;
        case "56":
            return GameKeyCode.DIGIT8;
        case "59":
            return GameKeyCode.DIGIT9;
        // Characters A..Z
        case "65":
            return GameKeyCode.A;
        case "66":
            return GameKeyCode.B;
        case "67":
            return GameKeyCode.C;
        case "68":
            return GameKeyCode.D;
        case "69":
            return GameKeyCode.E;
        case "70":
            return GameKeyCode.F;
        case "71":
            return GameKeyCode.G;
        case "72":
            return GameKeyCode.H;
        case "73":
            return GameKeyCode.I;
        case "74":
            return GameKeyCode.J;
        case "75":
            return GameKeyCode.K;
        case "76":
            return GameKeyCode.L;
        case "77":
            return GameKeyCode.M;
        case "78":
            return GameKeyCode.N;
        case "79":
            return GameKeyCode.O;
        case "80":
            return GameKeyCode.P;
        case "81":
            return GameKeyCode.Q;
        case "82":
            return GameKeyCode.R;
        case "83":
            return GameKeyCode.S;
        case "84":
            return GameKeyCode.T;
        case "85":
            return GameKeyCode.U;
        case "86":
            return GameKeyCode.V;
        case "87":
            return GameKeyCode.W;
        case "88":
            return GameKeyCode.X;
        case "89":
            return GameKeyCode.Y;
        case "90":
            return GameKeyCode.Z;
        }
        // This is the default...
        return GameKeyCode.SPACE;
    }
}
