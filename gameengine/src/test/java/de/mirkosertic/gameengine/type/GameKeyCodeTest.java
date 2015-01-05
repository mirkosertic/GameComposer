package de.mirkosertic.gameengine.type;

import java.util.List;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameKeyCodeTest {

    @Test
    public void testFromChar() throws Exception {
        assertEquals(GameKeyCode.ENTER, GameKeyCode.fromChar((char) 13));
        assertEquals(GameKeyCode.BACK_SPACE, GameKeyCode.fromChar((char) 8));
        assertEquals(GameKeyCode.TAB, GameKeyCode.fromChar((char) 9));
        assertEquals(GameKeyCode.ESCAPE, GameKeyCode.fromChar((char) 27));
        assertEquals(GameKeyCode.SPACE, GameKeyCode.fromChar(' '));
        assertEquals(GameKeyCode.COMMA, GameKeyCode.fromChar(','));
        assertEquals(GameKeyCode.MINUS, GameKeyCode.fromChar('-'));
        assertEquals(GameKeyCode.PERIOD, GameKeyCode.fromChar('.'));
        assertEquals(GameKeyCode.SLASH, GameKeyCode.fromChar('/'));
        assertEquals(GameKeyCode.DIGIT0, GameKeyCode.fromChar('0'));
        assertEquals(GameKeyCode.DIGIT1, GameKeyCode.fromChar('1'));
        assertEquals(GameKeyCode.DIGIT2, GameKeyCode.fromChar('2'));
        assertEquals(GameKeyCode.DIGIT3, GameKeyCode.fromChar('3'));
        assertEquals(GameKeyCode.DIGIT4, GameKeyCode.fromChar('4'));
        assertEquals(GameKeyCode.DIGIT5, GameKeyCode.fromChar('5'));
        assertEquals(GameKeyCode.DIGIT6, GameKeyCode.fromChar('6'));
        assertEquals(GameKeyCode.DIGIT7, GameKeyCode.fromChar('7'));
        assertEquals(GameKeyCode.DIGIT8, GameKeyCode.fromChar('8'));
        assertEquals(GameKeyCode.DIGIT9, GameKeyCode.fromChar('9'));
        assertEquals(GameKeyCode.SEMICOLON, GameKeyCode.fromChar(';'));
        assertEquals(GameKeyCode.EQUALS, GameKeyCode.fromChar('='));
        assertEquals(GameKeyCode.A, GameKeyCode.fromChar('A'));
        assertEquals(GameKeyCode.B, GameKeyCode.fromChar('B'));
        assertEquals(GameKeyCode.C, GameKeyCode.fromChar('C'));
        assertEquals(GameKeyCode.D, GameKeyCode.fromChar('D'));
        assertEquals(GameKeyCode.E, GameKeyCode.fromChar('E'));
        assertEquals(GameKeyCode.F, GameKeyCode.fromChar('F'));
        assertEquals(GameKeyCode.G, GameKeyCode.fromChar('G'));
        assertEquals(GameKeyCode.H, GameKeyCode.fromChar('H'));
        assertEquals(GameKeyCode.I, GameKeyCode.fromChar('I'));
        assertEquals(GameKeyCode.J, GameKeyCode.fromChar('J'));
        assertEquals(GameKeyCode.K, GameKeyCode.fromChar('K'));
        assertEquals(GameKeyCode.L, GameKeyCode.fromChar('L'));
        assertEquals(GameKeyCode.M, GameKeyCode.fromChar('M'));
        assertEquals(GameKeyCode.N, GameKeyCode.fromChar('N'));
        assertEquals(GameKeyCode.O, GameKeyCode.fromChar('O'));
        assertEquals(GameKeyCode.P, GameKeyCode.fromChar('P'));
        assertEquals(GameKeyCode.Q, GameKeyCode.fromChar('Q'));
        assertEquals(GameKeyCode.R, GameKeyCode.fromChar('R'));
        assertEquals(GameKeyCode.S, GameKeyCode.fromChar('S'));
        assertEquals(GameKeyCode.U, GameKeyCode.fromChar('U'));
        assertEquals(GameKeyCode.V, GameKeyCode.fromChar('V'));
        assertEquals(GameKeyCode.W, GameKeyCode.fromChar('W'));
        assertEquals(GameKeyCode.X, GameKeyCode.fromChar('X'));
        assertEquals(GameKeyCode.Y, GameKeyCode.fromChar('Y'));
        assertEquals(GameKeyCode.Z, GameKeyCode.fromChar('Z'));
        assertEquals(GameKeyCode.OPEN_BRACKET, GameKeyCode.fromChar('['));
        assertEquals(GameKeyCode.BACK_SLASH, GameKeyCode.fromChar('\\'));
        assertEquals(GameKeyCode.CLOSE_BRACKET, GameKeyCode.fromChar(']'));
        assertEquals(GameKeyCode.AMPERSAND, GameKeyCode.fromChar('&'));
        assertEquals(GameKeyCode.ASTERISK, GameKeyCode.fromChar('*'));
        assertEquals(GameKeyCode.QUOTEDBL, GameKeyCode.fromChar('"'));
        assertEquals(GameKeyCode.LESS, GameKeyCode.fromChar('<'));
        assertEquals(GameKeyCode.GREATER, GameKeyCode.fromChar('>'));
        assertEquals(GameKeyCode.BRACELEFT, GameKeyCode.fromChar('('));
        assertEquals(GameKeyCode.BRACERIGHT, GameKeyCode.fromChar(')'));
        assertEquals(GameKeyCode.AT, GameKeyCode.fromChar('@'));
        assertEquals(GameKeyCode.COLON, GameKeyCode.fromChar(':'));
        assertEquals(GameKeyCode.CIRCUMFLEX, GameKeyCode.fromChar('^'));
        assertEquals(GameKeyCode.DOLLAR, GameKeyCode.fromChar('$'));
        assertEquals(GameKeyCode.EXCLAMATION_MARK, GameKeyCode.fromChar('!'));
        assertEquals(GameKeyCode.PLUS, GameKeyCode.fromChar('+'));
        assertEquals(GameKeyCode.LEFT_PARENTHESIS, GameKeyCode.fromChar('{'));
        assertEquals(GameKeyCode.RIGHT_PARENTHESIS, GameKeyCode.fromChar('}'));
        assertEquals(GameKeyCode.UNDERSCORE, GameKeyCode.fromChar('_'));
        assertNull(GameKeyCode.fromChar('?'));
    }

    @Test
    public void testAllKeysAsSortedList() throws Exception {
        List<GameKeyCode> theList = GameKeyCode.allKeysAsSortedList();
        assertEquals(225, theList.size());
        assertEquals(GameKeyCode.A, theList.get(0));
        assertEquals(GameKeyCode.Z, theList.get(theList.size() - 1));
    }
}