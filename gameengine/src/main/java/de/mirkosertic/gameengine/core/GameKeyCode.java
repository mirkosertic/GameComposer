package de.mirkosertic.gameengine.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum GameKeyCode {
    ENTER, BACK_SPACE, TAB, CANCEL, CLEAR, SHIFT, CONTROL, ALT, PAUSE, CAPS, ESCAPE, SPACE, PAGE_UP, PAGE_DOWN, END, HOME, LEFT, UP, RIGHT, DOWN, COMMA, MINUS, PERIOD, SLASH, DIGIT0, DIGIT1, DIGIT2, DIGIT3, DIGIT4, DIGIT5, DIGIT6, DIGIT7, DIGIT8, DIGIT9, SEMICOLON, EQUALS, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, OPEN_BRACKET, BACK_SLASH, CLOSE_BRACKET, NUMPAD0, NUMPAD1, NUMPAD2, NUMPAD3, NUMPAD4, NUMPAD5, NUMPAD6, NUMPAD7, NUMPAD8, NUMPAD9, MULTIPLY, ADD, SEPARATOR, SUBTRACT, DECIMAL, DIVIDE, DELETE, NUM_LOCK, SCROLL_LOCK, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, PRINTSCREEN, INSERT, HELP, META, BACK_QUOTE, QUOTE, KP_UP, KP_DOWN, KP_LEFT, KP_RIGHT, DEAD_GRAVE, DEAD_ACUTE, DEAD_CIRCUMFLEX, DEAD_TILDE, DEAD_MACRON, DEAD_BREVE, DEAD_ABOVEDOT, DEAD_DIAERESIS, DEAD_ABOVERING, DEAD_DOUBLEACUTE, DEAD_CARON, DEAD_CEDILLA, DEAD_OGONEK, DEAD_IOTA, DEAD_VOICED_SOUND, DEAD_SEMIVOICED_SOUND, AMPERSAND, ASTERISK, QUOTEDBL, LESS, GREATER, BRACELEFT, BRACERIGHT, AT, COLON, CIRCUMFLEX, DOLLAR, EURO_SIGN, EXCLAMATION_MARK, INVERTED_EXCLAMATION_MARK, LEFT_PARENTHESIS, NUMBER_SIGN, PLUS, RIGHT_PARENTHESIS, UNDERSCORE, WINDOWS, CONTEXT_MENU, FINAL, CONVERT, NONCONVERT, ACCEPT, MODECHANGE, KANA, KANJI, ALPHANUMERIC, KATAKANA, HIRAGANA, FULL_WIDTH, HALF_WIDTH, ROMAN_CHARACTERS, ALL_CANDIDATES, PREVIOUS_CANDIDATE, CODE_INPUT, JAPANESE_KATAKANA, JAPANESE_HIRAGANA, JAPANESE_ROMAN, KANA_LOCK, INPUT_METHOD_ON_OFF, CUT, COPY, PASTE, UNDO, AGAIN, FIND, PROPS, STOP, COMPOSE, ALT_GRAPH, BEGIN, UNDEFINED, SOFTKEY_0, SOFTKEY_1, SOFTKEY_2, SOFTKEY_3, SOFTKEY_4, SOFTKEY_5, SOFTKEY_6, SOFTKEY_7, SOFTKEY_8, SOFTKEY_9, GAME_A, GAME_B, GAME_C, GAME_D, STAR, POUND, POWER, INFO, COLORED_KEY_0, COLORED_KEY_1, COLORED_KEY_2, COLORED_KEY_3, EJECT_TOGGLE, PLAY, RECORD, FAST_FWD, REWIND, TRACK_PREV, TRACK_NEXT, CHANNEL_UP, CHANNEL_DOWN, VOLUME_UP, VOLUME_DOWN, MUTE, COMMAND, SHORTCUT;

    public static GameKeyCode fromChar(char aCharacter) {
        switch(Character.toUpperCase(aCharacter)) {
            case 13:
                return ENTER;
            case 8:
                return BACK_SPACE;
            case 9:
                return TAB;
            case 27:
                return ESCAPE;
            case ' ':
                return SPACE;
            case ',':
                return COMMA;
            case '-':
                return MINUS;
            case '.':
                return PERIOD;
            case '/':
                return SLASH;
            case '0':
                return DIGIT0;
            case '1':
                return DIGIT1;
            case '2':
                return DIGIT2;
            case '3':
                return DIGIT3;
            case '4':
                return DIGIT4;
            case '5':
                return DIGIT5;
            case '6':
                return DIGIT6;
            case '7':
                return DIGIT7;
            case '8':
                return DIGIT8;
            case '9':
                return DIGIT9;
            case ';':
                return SEMICOLON;
            case '=':
                return EQUALS;
            case 'A':
                return A;
            case 'B':
                return B;
            case 'C':
                return C;
            case 'D':
                return D;
            case 'E':
                return E;
            case 'F':
                return F;
            case 'G':
                return G;
            case 'H':
                return H;
            case 'I':
                return I;
            case 'J':
                return J;
            case 'K':
                return K;
            case 'L':
                return L;
            case 'M':
                return M;
            case 'N':
                return N;
            case 'O':
                return O;
            case 'P':
                return P;
            case 'Q':
                return Q;
            case 'R':
                return R;
            case 'S':
                return S;
            case 'T':
                return T;
            case 'U':
                return U;
            case 'V':
                return V;
            case 'W':
                return W;
            case 'X':
                return X;
            case 'Y':
                return Y;
            case 'Z':
                return Z;
            case '[':
                return OPEN_BRACKET;
            case '\\':
                return BACK_SLASH;
            case ']':
                return CLOSE_BRACKET;
            case '&':
                return AMPERSAND;
            case '*':
                return ASTERISK;
            case '"':
                return QUOTEDBL;
            case '<':
                return LESS;
            case '>':
                return GREATER;
            case '(':
                return BRACELEFT;
            case ')':
                return BRACERIGHT;
            case '@':
                return AT;
            case ':':
                return COLON;
            case '^':
                return CIRCUMFLEX;
            case '$':
                return DOLLAR;
            case '€':
                return EURO_SIGN;
            case '!':
                return EXCLAMATION_MARK;
            case '+':
                return PLUS;
            case '{':
                return LEFT_PARENTHESIS;
            case '}':
                return RIGHT_PARENTHESIS;
            case '_':
                return UNDERSCORE;
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
