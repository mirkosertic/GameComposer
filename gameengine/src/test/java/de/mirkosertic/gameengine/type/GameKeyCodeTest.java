package de.mirkosertic.gameengine.type;

import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GameKeyCodeTest {

    @Test
    public void testFromChar() throws Exception {
        assertEquals(GameKeyCode.ENTER, GameKeyCodeUtils.fromChar((char) 13));
        assertEquals(GameKeyCode.BACK_SPACE, GameKeyCodeUtils.fromChar((char) 8));
        assertEquals(GameKeyCode.TAB, GameKeyCodeUtils.fromChar((char) 9));
        assertEquals(GameKeyCode.ESCAPE, GameKeyCodeUtils.fromChar((char) 27));
        assertEquals(GameKeyCode.SPACE, GameKeyCodeUtils.fromChar(' '));
        assertEquals(GameKeyCode.COMMA, GameKeyCodeUtils.fromChar(','));
        assertEquals(GameKeyCode.MINUS, GameKeyCodeUtils.fromChar('-'));
        assertEquals(GameKeyCode.PERIOD, GameKeyCodeUtils.fromChar('.'));
        assertEquals(GameKeyCode.SLASH, GameKeyCodeUtils.fromChar('/'));
        assertEquals(GameKeyCode.DIGIT0, GameKeyCodeUtils.fromChar('0'));
        assertEquals(GameKeyCode.DIGIT1, GameKeyCodeUtils.fromChar('1'));
        assertEquals(GameKeyCode.DIGIT2, GameKeyCodeUtils.fromChar('2'));
        assertEquals(GameKeyCode.DIGIT3, GameKeyCodeUtils.fromChar('3'));
        assertEquals(GameKeyCode.DIGIT4, GameKeyCodeUtils.fromChar('4'));
        assertEquals(GameKeyCode.DIGIT5, GameKeyCodeUtils.fromChar('5'));
        assertEquals(GameKeyCode.DIGIT6, GameKeyCodeUtils.fromChar('6'));
        assertEquals(GameKeyCode.DIGIT7, GameKeyCodeUtils.fromChar('7'));
        assertEquals(GameKeyCode.DIGIT8, GameKeyCodeUtils.fromChar('8'));
        assertEquals(GameKeyCode.DIGIT9, GameKeyCodeUtils.fromChar('9'));
        assertEquals(GameKeyCode.SEMICOLON, GameKeyCodeUtils.fromChar(';'));
        assertEquals(GameKeyCode.EQUALS, GameKeyCodeUtils.fromChar('='));
        assertEquals(GameKeyCode.A, GameKeyCodeUtils.fromChar('A'));
        assertEquals(GameKeyCode.B, GameKeyCodeUtils.fromChar('B'));
        assertEquals(GameKeyCode.C, GameKeyCodeUtils.fromChar('C'));
        assertEquals(GameKeyCode.D, GameKeyCodeUtils.fromChar('D'));
        assertEquals(GameKeyCode.E, GameKeyCodeUtils.fromChar('E'));
        assertEquals(GameKeyCode.F, GameKeyCodeUtils.fromChar('F'));
        assertEquals(GameKeyCode.G, GameKeyCodeUtils.fromChar('G'));
        assertEquals(GameKeyCode.H, GameKeyCodeUtils.fromChar('H'));
        assertEquals(GameKeyCode.I, GameKeyCodeUtils.fromChar('I'));
        assertEquals(GameKeyCode.J, GameKeyCodeUtils.fromChar('J'));
        assertEquals(GameKeyCode.K, GameKeyCodeUtils.fromChar('K'));
        assertEquals(GameKeyCode.L, GameKeyCodeUtils.fromChar('L'));
        assertEquals(GameKeyCode.M, GameKeyCodeUtils.fromChar('M'));
        assertEquals(GameKeyCode.N, GameKeyCodeUtils.fromChar('N'));
        assertEquals(GameKeyCode.O, GameKeyCodeUtils.fromChar('O'));
        assertEquals(GameKeyCode.P, GameKeyCodeUtils.fromChar('P'));
        assertEquals(GameKeyCode.Q, GameKeyCodeUtils.fromChar('Q'));
        assertEquals(GameKeyCode.R, GameKeyCodeUtils.fromChar('R'));
        assertEquals(GameKeyCode.S, GameKeyCodeUtils.fromChar('S'));
        assertEquals(GameKeyCode.U, GameKeyCodeUtils.fromChar('U'));
        assertEquals(GameKeyCode.V, GameKeyCodeUtils.fromChar('V'));
        assertEquals(GameKeyCode.W, GameKeyCodeUtils.fromChar('W'));
        assertEquals(GameKeyCode.X, GameKeyCodeUtils.fromChar('X'));
        assertEquals(GameKeyCode.Y, GameKeyCodeUtils.fromChar('Y'));
        assertEquals(GameKeyCode.Z, GameKeyCodeUtils.fromChar('Z'));
        assertEquals(GameKeyCode.OPEN_BRACKET, GameKeyCodeUtils.fromChar('['));
        assertEquals(GameKeyCode.BACK_SLASH, GameKeyCodeUtils.fromChar('\\'));
        assertEquals(GameKeyCode.CLOSE_BRACKET, GameKeyCodeUtils.fromChar(']'));
        assertEquals(GameKeyCode.AMPERSAND, GameKeyCodeUtils.fromChar('&'));
        assertEquals(GameKeyCode.ASTERISK, GameKeyCodeUtils.fromChar('*'));
        assertEquals(GameKeyCode.QUOTEDBL, GameKeyCodeUtils.fromChar('"'));
        assertEquals(GameKeyCode.LESS, GameKeyCodeUtils.fromChar('<'));
        assertEquals(GameKeyCode.GREATER, GameKeyCodeUtils.fromChar('>'));
        assertEquals(GameKeyCode.BRACELEFT, GameKeyCodeUtils.fromChar('('));
        assertEquals(GameKeyCode.BRACERIGHT, GameKeyCodeUtils.fromChar(')'));
        assertEquals(GameKeyCode.AT, GameKeyCodeUtils.fromChar('@'));
        assertEquals(GameKeyCode.COLON, GameKeyCodeUtils.fromChar(':'));
        assertEquals(GameKeyCode.CIRCUMFLEX, GameKeyCodeUtils.fromChar('^'));
        assertEquals(GameKeyCode.DOLLAR, GameKeyCodeUtils.fromChar('$'));
        assertEquals(GameKeyCode.EXCLAMATION_MARK, GameKeyCodeUtils.fromChar('!'));
        assertEquals(GameKeyCode.PLUS, GameKeyCodeUtils.fromChar('+'));
        assertEquals(GameKeyCode.LEFT_PARENTHESIS, GameKeyCodeUtils.fromChar('{'));
        assertEquals(GameKeyCode.RIGHT_PARENTHESIS, GameKeyCodeUtils.fromChar('}'));
        assertEquals(GameKeyCode.UNDERSCORE, GameKeyCodeUtils.fromChar('_'));
        assertNull(GameKeyCodeUtils.fromChar('?'));
    }

    @Test
    public void testAllKeysAsSortedList() throws Exception {
        List<GameKeyCode> theList = GameKeyCodeUtils.allKeysAsSortedList();
        assertEquals(225, theList.size());
        assertEquals(GameKeyCode.A, theList.get(0));
        assertEquals(GameKeyCode.Z, theList.get(theList.size() - 1));
    }
}