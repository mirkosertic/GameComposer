package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.ValueProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeExpressionParserTest {

    private Scanner scanner;
    private Parser parser;

    @Before
    public void setup() {
        scanner = new Scanner();
        parser = new Parser();
    }

    private GeExpressionParser parse(String aExpression) {
        return new GeExpressionParser(parser.parse(scanner.scan(aExpression)), new TestBuildInFunctions());
    }

    @Test
    public void testString() {
        GeExpressionParser theParser = parse("hallo");
        theParser.registerVariable("hallo", "v1");
        Assert.assertEquals("v1", theParser.evaluateToString());
    }

    @Test
    public void testPureString() {
        GeExpressionParser theParser = parse("\"hallo\"");
        Assert.assertEquals("hallo", theParser.evaluateToString());
    }

    @Test
    public void testNoting() {
        GeExpressionParser theParser = parse("hallo.x");
        Assert.assertNull(theParser.evaluateToObject());
    }

    @Test
    public void testValueProvider() {
        GeExpressionParser theParser = parse("hallo");
        theParser.registerVariable("hallo", new ValueProvider<Object>() {
            @Override
            public Object get() {
                return "v1";
            }
        });
        Assert.assertEquals("v1", theParser.evaluateToString());
    }

    @Test
    public void testReflectable() {
        GeExpressionParser theParser = parse("s.width");
        theParser.registerVariable("s", new Size(80, 90));
        Assert.assertEquals(Integer.valueOf(80), theParser.evaluateToObject());
    }

}
