package de.mirkosertic.gameengine.expression;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Shunting-yard algorithm.
 *
 * Converts infix expressions to RPN expressions.
 *
 * http://en.wikipedia.org/wiki/Shunting-yard_algorithm.
 * http://en.wikipedia.org/wiki/Reverse_Polish_notation.
 */
class Parser {

    public Parser() {
    }

    public Token[] parse(Token[] aTokens) {
        List<Token> theOutputQueue = new ArrayList<>();
        SimpleStack<Token> theOutputStack = new SimpleStack<>();

        for (int i = 0; i < aTokens.length; i++) {
            Token theToken = aTokens[i];

            // If the token is a number, then add it to the output queue.
            if (theToken.type == TokenType.VALUE || theToken.type == TokenType.STRING) {
                theOutputQueue.add(theToken);
                continue;
            }
            // If the token is a function token, then push it onto the stack.
            if (theToken.type == TokenType.FUNCTION) {
                theOutputStack.add(theToken);
                continue;
            }
            // If the token is a function argument separator (e.g., a bracket):
            //  Until the token at the top of the stack is a left parenthesis,
            //  pop operators off the stack onto the output queue.
            // If no left parentheses are encountered, either the separator was
            // misplaced or parentheses were mismatched.
            if (theToken.type == TokenType.LEFTPARENTHESIS && i > 0 && aTokens[i - 1].type == TokenType.FUNCTION) {
                while (!theOutputStack.isEmpty() && theOutputStack.peek().type != TokenType.RIGHTPARENTHESIS && theOutputStack.peek().type != TokenType.FUNCTION) {
                    theOutputQueue.add(theOutputStack.pop());
                }
                theOutputStack.add(theToken);
                continue;
            }

            // If the token is an operator, o1, then:
            // while there is an operator token, o2, at the top of the stack, and
            // either o1 is left-associative and its precedence is equal to that of o2,
            // or o1 has precedence less than that of o2,
            // pop o2 off the stack, onto the output queue;
            // push o1 onto the stack.
            if (theToken.type.isOperator()) {
                boolean isLeftAssociative = theToken.type.isLeftAssociative();
                int thePrecedence = theToken.type.getPrecedence();

                while (!(theOutputStack.isEmpty()) && theOutputStack.peek().type.isOperator()) {
                    int theStackPredecende = theOutputStack.peek().type.getPrecedence();
                    if ((isLeftAssociative && thePrecedence == theStackPredecende) || (thePrecedence < theStackPredecende)) {
                        theOutputQueue.add(theOutputStack.pop());
                    } else {
                        break;
                    }
                }
                theOutputStack.push(theToken);
                continue;
            }
            // If the token is a left parenthesis, then push it onto the stack.
            if (theToken.type == TokenType.LEFTPARENTHESIS) {
                theOutputStack.push(theToken);
                continue;
            }
            // If the token is a right parenthesis:
            // Until the token at the top of the stack is a left parenthesis, pop operators off the stack onto the output queue.
            // Pop the left parenthesis from the stack, but not onto the output queue.
            // If the token at the top of the stack is a function token, pop it onto the output queue.
            // If the stack runs out without finding a left parenthesis, then there are mismatched parentheses.
            if (theToken.type == TokenType.RIGHTPARENTHESIS) {
                while (!(theOutputStack.peek().type == TokenType.LEFTPARENTHESIS)) {
                    theOutputQueue.add(theOutputStack.pop());
                }
                theOutputStack.pop();
                if (!theOutputStack.isEmpty() && theOutputStack.peek().type == TokenType.FUNCTION) {
                    theOutputQueue.add(theOutputStack.pop());
                }
                continue;
            }
        }


        // When there are no more tokens to read:
        // While there are still operator tokens in the stack:
        // If the operator token on the top of the stack is a parenthesis, then there are mismatched parentheses.
        // Pop the operator onto the output queue.
        while (!theOutputStack.isEmpty()) {
            theOutputQueue.add(theOutputStack.pop());
        }

        if (theOutputQueue.size() > 1) {
            if (!theOutputQueue.get(theOutputQueue.size() -1).type.isOperator()) {
                throw new IllegalArgumentException("Missing operator");
            }
        }

        return theOutputQueue.toArray(new Token[theOutputQueue.size()]);
    }
}