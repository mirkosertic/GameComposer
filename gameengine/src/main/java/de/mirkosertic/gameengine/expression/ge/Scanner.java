package de.mirkosertic.gameengine.expression.ge;

public class Scanner {

    public enum TokenType {
        VALUE, PLUS, MINUS, DIVIDE, MULTIPLY, OPENBRACKED, CLOSEBRACKED, COMMA;
    }

    private final String expression;

    public Scanner(String aExpression) {
        expression = aExpression;
    }

    public void token(TokenType aTokenType) {
        System.out.println("Token " + aTokenType);
    }

    public void token(TokenType aTokenType, StringBuilder aPrefix) {
        System.out.println("Token " + aTokenType + " -> " + aPrefix);
    }

    public void scan() {
        boolean escapeNext = false;
        boolean inString = false;
        StringBuilder theCurrentToken = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char theCharacter = expression.charAt(i);
            if (escapeNext) {
                theCurrentToken.append(theCharacter);
                escapeNext = false;
            } else
            if (inString && !('"' == theCharacter)) {
                theCurrentToken.append(theCharacter);
            } else {
                switch (expression.charAt(i)) {
                    case '"':
                        if (!inString) {
                            inString = true;
                            theCurrentToken = new StringBuilder();
                        } else {
                            inString = false;
                            token(TokenType.VALUE, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        break;
                    case ' ':
                        //Can be ignored
                        break;
                    case '\\':
                        escapeNext = true;
                        break;
                    case '+':
                        if (theCurrentToken.length() > 0) {
                            token(TokenType.VALUE, theCurrentToken);
                            theCurrentToken = new StringBuilder();

                            token(TokenType.PLUS);
                        } else {
                            theCurrentToken.append(theCharacter);
                        }
                        break;
                    case '-':
                        if (theCurrentToken.length() > 0) {
                            token(TokenType.VALUE, theCurrentToken);
                            theCurrentToken = new StringBuilder();

                            token(TokenType.MINUS);
                        } else {
                            theCurrentToken.append(theCharacter);
                        }

                        break;
                    case '/':
                        if (theCurrentToken.length() > 0) {
                            token(TokenType.VALUE, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(TokenType.DIVIDE);
                        break;
                    case '*':
                        if (theCurrentToken.length() > 0) {
                            token(TokenType.VALUE, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(TokenType.MULTIPLY);
                        break;
                    case '(':
                        if (theCurrentToken.length() > 0) {
                            token(TokenType.VALUE, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(TokenType.OPENBRACKED);
                        break;
                    case ')':
                        if (theCurrentToken.length() > 0) {
                            token(TokenType.VALUE, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(TokenType.CLOSEBRACKED);
                        break;
                    case ',':
                        if (theCurrentToken.length() > 0) {
                            token(TokenType.VALUE, theCurrentToken);
                            theCurrentToken = new StringBuilder();
                        }
                        token(TokenType.COMMA);
                        break;
                    default:
                        theCurrentToken.append(theCharacter);
                        break;
                }
            }
        }
        if (theCurrentToken.length() > 0) {
            token(TokenType.VALUE, theCurrentToken);
        }
    }

    public static void main(String[] args) {
        Scanner theScanner = new Scanner("-1+2*-(6*-7)");
        theScanner.scan();
    }
}
