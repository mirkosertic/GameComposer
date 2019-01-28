package de.mirkosertic.gameengine.bytecoder;

import static org.junit.Assert.*;

import de.mirkosertic.bytecoder.api.web.Console;
import de.mirkosertic.bytecoder.unittest.BytecoderUnitTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

@RunWith(BytecoderUnitTestRunner.class)
public class JSONParserTest {

    @Test
    public void testParseTabs() {
        final String jsonData = "{\"key\": \"\thello\tworld!\n\"}";
        JSONParser theParser = new JSONParser();
        Map<String, Object> theResult = theParser.fromJSON(jsonData);
        String theKey = (String) theResult.get("key");
        Assert.assertEquals("\thello\tworld!\n", theKey);
    }

}