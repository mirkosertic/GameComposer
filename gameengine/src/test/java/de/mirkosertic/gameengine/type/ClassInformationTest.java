package de.mirkosertic.gameengine.type;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ClassInformationTest {

    private ClassInformation information;

    @Before
    public void setUp() {
        information = new ClassInformation() {
        };
    }

    @Test
    public void testRegisterField() throws Exception {
        Field theField = mock(Field.class);
        when(theField.getName()).thenReturn("Field1");
        information.register(theField);
        assertNull(information.getFieldByName("lala"));
        assertSame(theField, information.getFieldByName("Field1"));
    }

    @Test
    public void testRegisterMethod() throws Exception {
        Method theMethod = mock(Method.class);
        when(theMethod.getName()).thenReturn("Method1");
        information.register(theMethod);
        assertNull(information.getMethodByName("lala"));
        assertSame(theMethod, information.getMethodByName("Method1"));
    }

    @Test
    public void testGetFields() throws Exception {
        Field theField = mock(Field.class);
        when(theField.getName()).thenReturn("Field1");
        information.register(theField);
        Field[] theFields = information.getFields();
        assertEquals(1, theFields.length);
        assertSame(theField, theFields[0]);
    }
}