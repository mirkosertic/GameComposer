package de.mirkosertic.gameengine.expression;

public enum TokenType {
    EXP {
        @Override
        public boolean isLeftAssociative() {
            return false;
        }

        @Override
        public boolean isOperator() {
            return true;
        }

        @Override
        public int getPrecedence() {
            return 4;
        }
    },
    STRINGCONCATENATE {
        @Override
        public boolean isLeftAssociative() {
            return true;
        }

        @Override
        public boolean isOperator() {
            return true;
        }

        @Override
        public int getPrecedence() {
            return 2;
        }
    },
    ADD {
        @Override
        public boolean isLeftAssociative() {
            return true;
        }

        @Override
        public boolean isOperator() {
            return true;
        }

        @Override
        public int getPrecedence() {
            return 2;
        }
    },
    SUBSTRACT {
        @Override
        public boolean isLeftAssociative() {
            return true;
        }

        @Override
        public boolean isOperator() {
            return true;
        }

        @Override
        public int getPrecedence() {
            return 2;
        }
    },
    DIVIDE {
        @Override
        public boolean isLeftAssociative() {
            return true;
        }

        @Override
        public boolean isOperator() {
            return true;
        }

        @Override
        public int getPrecedence() {
            return 3;
        }
    },
    MULTIPLY {
        @Override
        public boolean isLeftAssociative() {
            return true;
        }

        @Override
        public boolean isOperator() {
            return true;
        }

        @Override
        public int getPrecedence() {
            return 3;
        }
    },
    FUNCTION {
        @Override
        public boolean isLeftAssociative() {
            return true;
        }

        @Override
        public boolean isOperator() {
            return true;
        }

        @Override
        public int getPrecedence() {
            return 5;
        }
    },
    VALUE {
        @Override
        public boolean isLeftAssociative() {
            throw new IllegalArgumentException("Unknown associativity for " + this);
        }

        @Override
        public boolean isOperator() {
            return false;
        }

        @Override
        public int getPrecedence() {
            throw new IllegalArgumentException("Unknown precedence for " + this);
        }
    },
    STRING {
        @Override
        public boolean isLeftAssociative() {
            throw new IllegalArgumentException("Unknown associativity for " + this);
        }

        @Override
        public boolean isOperator() {
            return false;
        }

        @Override
        public int getPrecedence() {
            throw new IllegalArgumentException("Unknown precedence for " + this);
        }
    },
    LEFTPARENTHESIS {
        @Override
        public boolean isLeftAssociative() {
            throw new IllegalArgumentException("Unknown associativity for " + this);
        }

        @Override
        public boolean isOperator() {
            return false;
        }

        @Override
        public int getPrecedence() {
            throw new IllegalArgumentException("Unknown precedence for " + this);
        }
    },
    RIGHTPARENTHESIS {
        @Override
        public boolean isLeftAssociative() {
            throw new IllegalArgumentException("Unknown associativity for " + this);
        }

        @Override
        public boolean isOperator() {
            return false;
        }

        @Override
        public int getPrecedence() {
            throw new IllegalArgumentException("Unknown precedence for " + this);
        }
    },
    COMMA {
        @Override
        public boolean isLeftAssociative() {
            throw new IllegalArgumentException("Unknown associativity for " + this);
        }

        @Override
        public boolean isOperator() {
            return false;
        }

        @Override
        public int getPrecedence() {
            throw new IllegalArgumentException("Unknown precedence for " + this);
        }
    };

    public abstract boolean isLeftAssociative();

    public abstract boolean isOperator();

    public abstract int getPrecedence();
}
