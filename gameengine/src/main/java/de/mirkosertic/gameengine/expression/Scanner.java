package de.mirkosertic.gameengine.expression;

import java.util.ArrayList;
import java.util.List;

class Scanner {

    public Scanner() {
    }

    private void token(List<Token> aTokens, TokenType aTokenType) {
        aTokens.add(new Token(aTokenType));
    }

    private void value(List<Token> aTokens, StringBuilder aValue) {
        aTokens.add(new Token(TokenType.VALUE, aValue.toString()));
    }

    private void symbol(List<Token> aTokens, StringBuilder aSymbol) {
        aTokens.add(new Token(TokenType.FUNCTION, aSymbol.toString()));
    }

    public Token[] scan(String expression) {

        List<Token> theTokens = new ArrayList<Token>();

        boolean escapeNext = false;
        boolean inString = false;
        boolean inNumeric = false;
        boolean inSymbol = false;
        StringBuilder theCurrentToken = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char theCharacter = expression.charAt(i);
            if (escapeNext) {
                theCurrentToken.append(theCharacter);
                escapeNext = false;
            } else if (inString && !('"' == theCharacter)) {
                theCurrentToken.append(theCharacter);
            } else {
                switch (expression.charAt(i)) {
                    case '"':
                        if (!inString) {
                            inString = true;
                            theCurrentToken = new StringBuilder();
                        } else {
                            inString = false;
                            value(theTokens, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                            inNumeric = false;
                            inSymbol = false;
                        }
                        break;
                    case ' ':
                        if (theCurrentToken.length() > 0) {
                            value(theTokens, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        inNumeric = false;
                        inSymbol = false;
                        break;
                    case '\\':
                        escapeNext = true;
                        break;
                    case '+':
                        if (theCurrentToken.length() > 0) {
                            value(theTokens, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(theTokens, TokenType.ADD);
                        inNumeric = false;
                        inSymbol = false;
                        break;
                    case '|':
                        if (theCurrentToken.length() > 0) {
                            value(theTokens, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(theTokens, TokenType.STRINGCONCATENATE);
                        inNumeric = false;
                        inSymbol = false;
                        break;
                    case '-':
                        if (theCurrentToken.length() > 0) {
                            value(theTokens, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(theTokens, TokenType.SUBSTRACT);
                        inNumeric = false;
                        inSymbol = false;
                        break;
                    case '/':
                        if (theCurrentToken.length() > 0) {
                            value(theTokens, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(theTokens, TokenType.DIVIDE);
                        inNumeric = false;
                        inSymbol = false;
                        break;
                    case '*':
                        if (theCurrentToken.length() > 0) {
                            value(theTokens, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(theTokens, TokenType.MULTIPLY);
                        inNumeric = false;
                        inSymbol = false;
                        break;
                    case '^':
                        if (theCurrentToken.length() > 0) {
                            value(theTokens, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(theTokens, TokenType.EXP);
                        inNumeric = false;
                        inSymbol = false;
                        break;
                    case '(':
                        if (theCurrentToken.length() > 0) {
                            if (inSymbol) {
                                symbol(theTokens, theCurrentToken);
                            } else {
                                value(theTokens, theCurrentToken);
                            }
                            theCurrentToken = new StringBuilder();
                        }
                        token(theTokens, TokenType.LEFTPARENTHESIS);
                        inNumeric = false;
                        inSymbol = false;
                        break;
                    case ')':
                        if (theCurrentToken.length() > 0) {
                            value(theTokens, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(theTokens, TokenType.RIGHTPARENTHESIS);
                        inNumeric = false;
                        inSymbol = false;
                        break;
                    case ',':
                        if (theCurrentToken.length() > 0) {
                            value(theTokens, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(theTokens, TokenType.COMMA);
                        inNumeric = false;
                        inSymbol = false;
                        break;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    case '.':
                        if (inSymbol) {
                            // A complex variable in dot notation
                            theCurrentToken.append(theCharacter);
                        } else {
                            // Numerical symbols
                            theCurrentToken.append(theCharacter);
                            inNumeric = true;
                        }
                        break;
                    default:
                        if (inNumeric) {
                            throw new IllegalArgumentException("Unexpected character at position " + i);
                        }
                        inSymbol = true;
                        theCurrentToken.append(theCharacter);
                        break;
                }
            }
        }
        if (theCurrentToken.length() > 0) {
            value(theTokens, theCurrentToken);
        }

        return theTokens.toArray(new Token[theTokens.size()]);
    }
}
