/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <https://techshoom.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.techshroom.emergencylanding.generations;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.lang.model.element.Modifier;

import com.flowpowered.math.vector.Vectord;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.techshroom.emergencylanding.library.util.Maths;
import com.techshroom.emergencylanding.library.util.Strings;

public class PointClassGenerator {

    private static final String TYPE_NAME_POINT_SET = "PointSet";
    private static final String TYPE_NAME_HAS_X_POINT = "HasXPoint";
    private static final String METHOD_NAME_GET_POINT_COUNT = "getPointCount";

    private static final class NameData {

        private static String makeClassName(int num) {
            String english = Maths.convertNumberToEnglish(num);
            String className = "";
            for (String s : Splitter.on(' ').split(english)) {
                className += Strings.uppercaseFirstCodePoint(s);
            }
            return className;
        }

        private final int number;
        private final String className;
        private final String pointNumberName;
        private transient TypeSpec HasXSpec;

        private String getXPointName() {
            return "get" + this.pointNumberName + "Point";
        }

        private NameData(int number) {
            this.number = number;
            this.className = makeClassName(number);
            this.pointNumberName = Strings.uppercaseFirstCodePoint(Maths.toOrdinalNumber(number));
        }

    }

    private static final String FOLDER = "src/generated/java";
    private static final String PACKAGE = "com.techshroom.emergencylanding.library.shapeup.generated";
    private static final String INDENTATION = Strings.repeat(' ', 4);
    private static final TypeVariableName VECTOR_D = TypeVariableName.get("V", ClassName.get(Vectord.class));

    private static final List<NameData> NAMES;
    static {
        Stream.Builder<NameData> names = Stream.builder();
        int nameCount = 10;
        for (int i = 1; i <= nameCount; i++) {
            names.add(new NameData(i));
        }
        NAMES = names.build().collect(Collectors.toList());
    }

    public static void main(String[] args) {
        TypeSpec.Builder PointSet =
                TypeSpec.interfaceBuilder(TYPE_NAME_POINT_SET).addModifiers(Modifier.PUBLIC).addTypeVariable(VECTOR_D);
        TypeSpec.Builder HasXPoint = TypeSpec.classBuilder(TYPE_NAME_HAS_X_POINT).addModifiers();
        String generationTime = DateTimeFormatter.RFC_1123_DATE_TIME.format(LocalDateTime.now().atZone(ZoneOffset.UTC));
        HasXPoint.addJavadoc(
                "Package-private interfaces to allow sharing of methods in PointSet without allowing PointSet.X as an instanceof PointSet.Y\n"
                        + "Automatically generated by PCG on $L. All edits will be discarded when regenerated.\n",
                generationTime);
        PointSet.addJavadoc(
                "A PointSet contains a certain amount of points, which can be accessed with {@link #getPointCount}.\n"
                        + "There are $L pre-generated interfaces that are for common points, and will return the correct number from {@code getPointCount}.\n"
                        + "Automatically generated by PCG on $L. All edits will be discarded when regenerated.\n"
                        + "\n@param <V> - The type of the vector used as the point\n",
                NAMES.size(), generationTime);
        HasXPoint.addTypes(generateHasXPointClassExtensions());
        PointSet.addTypes(generatePointSetClassExtensions());
        PointSet.addMethod(MethodSpec.methodBuilder("getPoints").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(ParameterizedTypeName.get(ClassName.get(Collection.class), VECTOR_D)).build());
        PointSet.addMethod(MethodSpec.methodBuilder(METHOD_NAME_GET_POINT_COUNT)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(TypeName.INT).build());
        renderToFile(HasXPoint.build(), FOLDER, PACKAGE);
        renderToFile(PointSet.build(), FOLDER, PACKAGE);
    }

    private static List<TypeSpec> generateHasXPointClassExtensions() {
        ImmutableList.Builder<TypeSpec> specs = ImmutableList.builder();
        TypeSpec lastX = null;
        for (NameData name : NAMES) {
            specs.add(lastX = generateHasXPointClassExtension(name, lastX));
            name.HasXSpec = lastX;
        }
        return specs.build();
    }

    private static TypeSpec generateHasXPointClassExtension(NameData info, TypeSpec lastX) {
        TypeSpec.Builder x = TypeSpec.interfaceBuilder(info.className).addTypeVariable(VECTOR_D);
        if (lastX != null) {
            x.addSuperinterface(
                    ParameterizedTypeName.get(ClassName.get(PACKAGE, TYPE_NAME_HAS_X_POINT, lastX.name), VECTOR_D));
        }
        x.addMethod(MethodSpec.methodBuilder(info.getXPointName()).addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(VECTOR_D).build());
        return x.build();
    }

    private static List<TypeSpec> generatePointSetClassExtensions() {
        ImmutableList.Builder<TypeSpec> specs = ImmutableList.builder();
        for (NameData name : NAMES) {
            specs.add(generatePointSetClassExtension(name));
        }
        return specs.build();
    }

    private static TypeSpec generatePointSetClassExtension(NameData info) {
        TypeSpec.Builder x = TypeSpec.interfaceBuilder(info.className).addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(VECTOR_D);
        x.addSuperinterface(ParameterizedTypeName.get(ClassName.get(PACKAGE, TYPE_NAME_POINT_SET), VECTOR_D));
        if (info.HasXSpec != null) {
            x.addSuperinterface(ParameterizedTypeName
                    .get(ClassName.get(PACKAGE, TYPE_NAME_HAS_X_POINT, info.HasXSpec.name), VECTOR_D));
        }
        x.addMethod(generateImplementation(info));
        x.addMethod(MethodSpec.methodBuilder(METHOD_NAME_GET_POINT_COUNT).addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT).returns(TypeName.INT)
                .addCode(CodeBlock.builder().addStatement("return $L", info.number).build()).build());
        return x.build();
    }

    private static MethodSpec generateImplementation(NameData info) {
        TypeName returnType =
                ParameterizedTypeName.get(ClassName.get(PACKAGE, TYPE_NAME_POINT_SET, info.className), VECTOR_D);
        MethodSpec.Builder method = MethodSpec.methodBuilder("create").addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(VECTOR_D).returns(returnType);
        String[] params = IntStream.rangeClosed(1, info.number).mapToObj(x -> "point" + x).toArray(String[]::new);
        for (int i = 1; i <= info.number; i++) {
            method.addParameter(VECTOR_D, params[i - 1]);
        }
        TypeSpec.Builder inner = TypeSpec.anonymousClassBuilder("");
        inner.addSuperinterface(returnType);
        for (int i = 0; i < info.number; i++) {
            NameData cInfo = NAMES.get(i);
            inner.addMethod(MethodSpec.methodBuilder(cInfo.getXPointName()).addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC).returns(VECTOR_D)
                    .addCode(CodeBlock.builder().addStatement("return $N", params[i]).build()).build());
        }
        final int builderThreshold = 5;
        final String points = "points";
        final CodeBlock.Builder pointCreator = CodeBlock.builder();
        ClassName ImmutableListClass = ClassName.get(ImmutableList.class);
        TypeName ImmutableListClassV = ParameterizedTypeName.get(ImmutableListClass, VECTOR_D);
        inner.addMethod(MethodSpec.methodBuilder("getPoints").addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC).returns(ImmutableListClassV)
                .addCode(CodeBlock.builder().addStatement("return $N", points).build()).build());
        if (info.number > builderThreshold) {
            final String pointsBuilder = "pointsBuilder";
            ClassName ImmutableListBuilderClass = ClassName.get(ImmutableList.Builder.class);
            TypeName ImmutableListBuilderClassV = ParameterizedTypeName.get(ImmutableListBuilderClass, VECTOR_D);
            pointCreator.addStatement("$T $N = $T.$N()", ImmutableListBuilderClassV, pointsBuilder, ImmutableListClass,
                    "builder");
            for (int i = 0; i < info.number; i++) {
                pointCreator.addStatement("$N.$N($N)", pointsBuilder, "add", params[i]);
            }
            pointCreator.addStatement("$T $N = $N.$N()", ImmutableListClassV, points, pointsBuilder, "build");
        } else {
            String args = IntStream.range(0, info.number).mapToObj(x -> "$N").collect(Collectors.joining(", "));
            pointCreator.add("$[$T $N = $T.$N(", ImmutableListClassV, points, ImmutableListClass, "of")
                    .add(args, (Object[]) params).add(");\n$]");
        }
        method.addCode(CodeBlock.builder().add(pointCreator.build()).addStatement("return $L", inner.build()).build());
        return method.build();
    }

    private static void renderToFile(TypeSpec spec, String folder, String bowiesPackage /*
                                                                                         * (
                                                                                         * ͡°
                                                                                         * ͜ʖ
                                                                                         * ͡
                                                                                         * °
                                                                                         * )
                                                                                         */) {
        try {
            JavaFile.builder(bowiesPackage, spec).indent(INDENTATION).skipJavaLangImports(true).build()
                    .writeTo(Paths.get(folder));
        } catch (IOException e) {
            System.err.println("Couldn't render " + bowiesPackage + "." + spec.name);
        }
    }

}
