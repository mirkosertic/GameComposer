package de.mirkosertic.gameengine.expression.ge;

public class Token {

    final TokenType type;
    final String value;

    Token(TokenType aType, String aValue) {
        type = aType;
        value = aValue;
    }

    Token(TokenType aType) {
        this(aType, null);
    }
}
