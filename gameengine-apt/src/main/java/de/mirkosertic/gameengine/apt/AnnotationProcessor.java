/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.apt;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

@SupportedAnnotationTypes(
        {"de.mirkosertic.gameengine.annotations.ReflectiveField",
         "de.mirkosertic.gameengine.annotations.ReflectiveMethod"
        }
)
public class AnnotationProcessor extends AbstractProcessor {

    private String toTypeName(String aTypeName) {
        if ("float".equals(aTypeName)) {
            return "Float";
        }
        if ("int".equals(aTypeName)) {
            return "Integer";
        }
        if ("long".equals(aTypeName)) {
            return "Long";
        }
        if ("double".equals(aTypeName)) {
            return "Double";
        }
        if (aTypeName.startsWith("java.lang.")) {
            return aTypeName.substring(10);
        }
        return aTypeName;
    }

    private String stripGenerics(String aTypeName) {
        int p = aTypeName.indexOf("<");
        if (p>0) {
            return aTypeName.substring(0, p);
        }
        return aTypeName;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> aAnnotations, RoundEnvironment aEnvironment) {

        Map<TypeElement, List<Element>> theAnnotatedClassNames = new HashMap<>();

        for (TypeElement theAnnotation : aAnnotations) {

                for (Element theElement : aEnvironment.getElementsAnnotatedWith(theAnnotation)) {
                    Element theA = theElement.getEnclosingElement();
                    TypeElement theType = (TypeElement) theA;
                    List<Element> theAnnotations = theAnnotatedClassNames.get(theType);
                    if (theAnnotations == null) {
                        theAnnotations = new ArrayList<>();
                        theAnnotatedClassNames.put(theType, theAnnotations);
                    }
                    theAnnotations.add(theElement);
                }
        }

        Filer theFiler = processingEnv.getFiler();
        for (Map.Entry<TypeElement, List<Element>> theEntry : theAnnotatedClassNames.entrySet()) {

            TypeElement theClass = theEntry.getKey();

            String theSuperClass = "ClassInformation";
            if (theClass.getAnnotation(InheritedClassInformation.class) != null) {
                theSuperClass = theClass.getSuperclass().toString()+"ClassInformation";
            }

            String theClassInfoFQName = theClass.getQualifiedName().toString()+"ClassInformation";
            String theClassInfoName = theClass.getSimpleName().toString()+"ClassInformation";
            String thePackageName = ((PackageElement) theClass.getEnclosingElement()).getQualifiedName().toString();

            try {
                PrintWriter theClassWriter = new PrintWriter(theFiler.createSourceFile(theClassInfoFQName, theClass).openWriter());
                theClassWriter.println("package " + thePackageName + ";");
                theClassWriter.println();

                theClassWriter.println("import de.mirkosertic.gameengine.type.ClassInformation;");
                theClassWriter.println("import de.mirkosertic.gameengine.type.Field;");
                theClassWriter.println("import de.mirkosertic.gameengine.type.Method;");
                theClassWriter.println();

                theClassWriter.print("public class ");
                theClassWriter.print(theClassInfoName);
                theClassWriter.print(" extends ");
                theClassWriter.print(theSuperClass);
                theClassWriter.print(" {");
                theClassWriter.println();
                for (Element theElement : theEntry.getValue()) {
                    ReflectiveField theField = theElement.getAnnotation(ReflectiveField.class);
                    if (theField != null) {
                        // Field annotation
                        String theTypename;
                        boolean isMethod = false;
                        if (theElement.getKind() == ElementKind.FIELD) {
                            TypeMirror theType = theElement.asType();
                            theTypename = toTypeName(theType.toString());
                        } else {
                            // Method
                            TypeMirror theType = ((ExecutableType) theElement.asType()).getReturnType();
                            theTypename = toTypeName(theType.toString());
                            isMethod = true;
                        }

                        String theFieldName = theElement.getSimpleName().toString().toUpperCase();
                        theClassWriter.println();
                        theClassWriter.print("  public final Field<");
                        theClassWriter.print(theClass.getSimpleName().toString());
                        theClassWriter.print(", ");
                        theClassWriter.print(theTypename);
                        theClassWriter.print("> ");
                        theClassWriter.print(theFieldName);
                        theClassWriter.print(" = new Field<");
                        theClassWriter.print(theClass.getSimpleName().toString());
                        theClassWriter.print(", ");
                        theClassWriter.print(theTypename);
                        theClassWriter.print(">(\"");
                        theClassWriter.print(theElement.getSimpleName().toString());
                        theClassWriter.print("\", ");
                        theClassWriter.print(stripGenerics(theTypename));
                        theClassWriter.println(".class) {");
                        theClassWriter.println("    @Override");
                        theClassWriter.print("    public ");
                        theClassWriter.print(theTypename);
                        theClassWriter.print(" getValue(");
                        theClassWriter.print(theClass.getSimpleName().toString());
                        theClassWriter.println(" aObject) {");
                        theClassWriter.print("      return aObject.");
                        theClassWriter.print(theElement.getSimpleName().toString());
                        if (isMethod) {
                            theClassWriter.print("()");
                        }
                        theClassWriter.println(";");
                        theClassWriter.println("    }");
                        theClassWriter.println("  };");
                    }
                    ReflectiveMethod theMethod = theElement.getAnnotation(ReflectiveMethod.class);
                    if (theMethod != null) {
                        ExecutableType theElementType = (ExecutableType) theElement.asType();
                        // Field annotation
                        TypeMirror theType = theElementType.getReturnType();
                        String theTypename = toTypeName(theType.toString());

                        String theFieldName = theElement.getSimpleName().toString().toUpperCase();
                        theClassWriter.println();
                        theClassWriter.print("  public final Method<");
                        theClassWriter.print(theClass.getQualifiedName().toString());
                        theClassWriter.print("> ");
                        theClassWriter.print(theFieldName);
                        theClassWriter.print(" = new Method<");
                        theClassWriter.print(theClass.getQualifiedName().toString());
                        theClassWriter.print(">(\"");
                        theClassWriter.print(theElement.getSimpleName().toString());
                        theClassWriter.print("\", new Class[] {");

                        List<? extends TypeMirror> theTypes = theElementType.getParameterTypes();
                        for (int i=0;i<theTypes.size();i++) {
                            if (i>0) {
                                theClassWriter.print(", ");
                            }
                            theClassWriter.print(stripGenerics(toTypeName(theTypes.get(i).toString())));
                            theClassWriter.print(".class");
                        }

                        theClassWriter.println("}) {");
                        theClassWriter.println("    @Override");
                        theClassWriter.print("    public Object invoke(");
                        theClassWriter.print(theClass.getSimpleName().toString());
                        theClassWriter.println(" aObject, Object[] aArguments) {");
                        if (!"void".equals(theTypename)) {
                            theClassWriter.print("      return aObject.");
                            theClassWriter.print(theElement.getSimpleName().toString());
                            theClassWriter.print("(");
                            for (int i = 0; i < theTypes.size(); i++) {
                                if (i > 0) {
                                    theClassWriter.print(", ");
                                }
                                theClassWriter.print("(");
                                theClassWriter.print(toTypeName(theTypes.get(i).toString()));
                                theClassWriter.print(") aArguments[");
                                theClassWriter.print(i);
                                theClassWriter.print("]");
                            }

                            theClassWriter.println(");");
                        } else {
                            theClassWriter.print("      aObject.");
                            theClassWriter.print(theElement.getSimpleName().toString());
                            theClassWriter.print("(");
                            for (int i = 0; i < theTypes.size(); i++) {
                                if (i > 0) {
                                    theClassWriter.print(", ");
                                }
                                theClassWriter.print("(");
                                theClassWriter.print(toTypeName(theTypes.get(i).toString()));
                                theClassWriter.print(") aArguments[");
                                theClassWriter.print(i);
                                theClassWriter.print("]");
                            }

                            theClassWriter.println(");");
                            theClassWriter.println("      return null;");
                        }
                        theClassWriter.println("    }");
                        theClassWriter.println("  };");
                    }
                }
                theClassWriter.println();
                theClassWriter.print("  public ");
                theClassWriter.print(theClassInfoName);
                theClassWriter.println("() {");

                for (Element theElement : theEntry.getValue()) {
                    theClassWriter.print("    register(");
                    theClassWriter.print(theElement.getSimpleName().toString().toUpperCase());
                    theClassWriter.println(");");
                }

                theClassWriter.println("  }");
                theClassWriter.println("}");
                theClassWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}

