package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class TextClassInformation extends ClassInformation {

  public final Method<de.mirkosertic.gameengine.text.Text> DELETE = new Method<de.mirkosertic.gameengine.text.Text>("delete", new Class[] {}) {
    @Override
    public Object invoke(Text aObject, Object[] aArguments) {
      aObject.delete();
      return null;
    }
  };

  public final Field<Text, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Font>> FONTPROPERTY = new Field<Text, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Font>>("fontProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Font> getValue(Text aObject) {
      return aObject.fontProperty();
    }
  };

  public final Field<Text, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.TextExpression>> TEXTEXPRESSIONPROPERTY = new Field<Text, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.TextExpression>>("textExpressionProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.TextExpression> getValue(Text aObject) {
      return aObject.textExpressionProperty();
    }
  };

  public final Field<Text, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Color>> COLORPROPERTY = new Field<Text, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Color>>("colorProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Color> getValue(Text aObject) {
      return aObject.colorProperty();
    }
  };

  public final Field<Text, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>> ISSCRIPTPROPERTY = new Field<Text, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>>("isScriptProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Boolean> getValue(Text aObject) {
      return aObject.isScriptProperty();
    }
  };

  public TextClassInformation() {
    register(DELETE);
    register(FONTPROPERTY);
    register(TEXTEXPRESSIONPROPERTY);
    register(COLORPROPERTY);
    register(ISSCRIPTPROPERTY);
  }
}
