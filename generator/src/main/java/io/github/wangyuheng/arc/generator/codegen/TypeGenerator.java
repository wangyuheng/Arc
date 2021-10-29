package io.github.wangyuheng.arc.generator.codegen;

import io.github.wangyuheng.arc.dgraph.annotation.DgraphType;
import io.github.wangyuheng.arc.dgraph.annotation.UidField;
import io.github.wangyuheng.arc.dgraph.dictionary.IDomainClass;
import io.github.wangyuheng.arc.generator.codegen.spec.FieldSpecGenAbstractGetter;
import io.github.wangyuheng.arc.generator.codegen.spec.FieldSpecGenGetter;
import io.github.wangyuheng.arc.generator.codegen.spec.FieldSpecGenSetter;
import io.github.wangyuheng.arc.generator.codegen.spec.TypeSpecGenBuilder;
import io.github.wangyuheng.arc.generator.codegen.util.JavadocUtils;
import io.github.wangyuheng.arc.generator.codegen.util.PackageManager;
import io.github.wangyuheng.arc.generator.codegen.util.SpecNameManager;
import io.github.wangyuheng.arc.generator.convert.GraphqlType2JavapoetTypeName;
import io.github.wangyuheng.arc.generator.convert.IsContainsGraphqlMethodField;
import io.github.wangyuheng.arc.generator.convert.IsGraphqlMethodField;
import io.github.wangyuheng.arc.generator.convert.IsOperator;
import io.github.wangyuheng.arc.graphql.annotation.Graphql;
import io.github.wangyuheng.arc.graphql.annotation.GraphqlMethod;
import io.github.wangyuheng.arc.graphql.util.GraphqlTypeUtils;
import com.squareup.javapoet.*;
import graphql.language.*;
import graphql.schema.DataFetcher;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 根据 {@link ObjectTypeDefinition} 生成 {@link io.github.wangyuheng.arc.generator.conf.CodeGenType#TYPE} 代码
 *
 * @author yuheng.wang
 */
public class TypeGenerator implements IGenerator {

    private static final Class<?>[] SUPPORT_TYPES = new Class[]{ObjectTypeDefinition.class, OperationTypeDefinition.class, UnionTypeDefinition.class, InterfaceTypeDefinition.class};

    private static final String DEFAULT_UID_FIELD_NAME = "id";

    private final PackageManager packageManager;

    public TypeGenerator(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public Stream<JavaFile> apply(TypeDefinitionRegistry typeDefinitionRegistry) {
        final Predicate<ObjectTypeDefinition> isOperator = new IsOperator(GraphqlTypeUtils.getOperationTypeNames(typeDefinitionRegistry));
        final GraphqlType2JavapoetTypeName toJavapoetTypeName = new GraphqlType2JavapoetTypeName(packageManager);
        final MethodSpecBuilder toMethodSpec = new MethodSpecBuilder(toJavapoetTypeName);
        final IsGraphqlMethodField isGraphqlMethodField = new IsGraphqlMethodField(typeDefinitionRegistry);
        final DgraphTypeFiller dgraphTypeFiller = new DgraphTypeFiller(toJavapoetTypeName);
        final IsContainsGraphqlMethodField isContainGraphqlMethodField = new IsContainsGraphqlMethodField(isGraphqlMethodField);
        final AutowiredFieldFiller autowiredFieldFiller = new AutowiredFieldFiller(isContainGraphqlMethodField, packageManager);
        final TypeSpecConvert typeSpecConvert = new TypeSpecConvert(toJavapoetTypeName);
        final TypeSpecGenBuilder typeSpecGenBuilder = new TypeSpecGenBuilder();

        return typeDefinitionRegistry.types().values().stream()
                .filter(it -> Arrays.stream(SUPPORT_TYPES).anyMatch(t -> t.isInstance(it)))
                .map(typeDefinition -> {
                    switch (TypeKind.getTypeKind(typeDefinition)) {
                        case Operation:
                            return new OperatorGenerator(typeSpecConvert, autowiredFieldFiller, toMethodSpec).apply((ObjectTypeDefinition) typeDefinition);
                        case Object:
                            if (isOperator.test((ObjectTypeDefinition) typeDefinition)) {
                                return new OperatorGenerator(typeSpecConvert, autowiredFieldFiller, toMethodSpec).apply((ObjectTypeDefinition) typeDefinition);
                            } else {
                                TypeSpec.Builder typeSpecBuilder = new ObjectGenerator(typeSpecConvert, dgraphTypeFiller, autowiredFieldFiller, toMethodSpec, isGraphqlMethodField).apply((ObjectTypeDefinition) typeDefinition);
                                return typeSpecGenBuilder.apply(typeSpecBuilder.build()).toBuilder();
                            }
                        case Interface:
                            return new InterfaceGenerator(toJavapoetTypeName).apply((InterfaceTypeDefinition) typeDefinition);
                        case Union:
                            return new UnionGenerator().apply((UnionTypeDefinition) typeDefinition);
                        case Enum:
                        case Scalar:
                        case InputObject:
                        default:
                            throw new IllegalArgumentException("type generator not support this graphql type!");
                    }
                })
                .map(it -> JavaFile.builder(packageManager.getTypePackage(), it.build()).build());
    }

    static class ObjectGenerator implements Function<ObjectTypeDefinition, TypeSpec.Builder> {

        private final TypeSpecConvert typeSpecConvert;
        private final DgraphTypeFiller dgraphTypeFiller;
        private final AutowiredFieldFiller autowiredFieldFiller;
        private final MethodSpecBuilder toMethodSpec;
        private final IsGraphqlMethodField isGraphqlMethodField;

        public ObjectGenerator(TypeSpecConvert typeSpecConvert,
                               DgraphTypeFiller dgraphTypeFiller,
                               AutowiredFieldFiller autowiredFieldFiller,
                               MethodSpecBuilder toMethodSpec, IsGraphqlMethodField isGraphqlMethodField) {
            this.typeSpecConvert = typeSpecConvert;
            this.dgraphTypeFiller = dgraphTypeFiller;
            this.autowiredFieldFiller = autowiredFieldFiller;
            this.toMethodSpec = toMethodSpec;
            this.isGraphqlMethodField = isGraphqlMethodField;
        }

        @Override
        public TypeSpec.Builder apply(ObjectTypeDefinition objectTypeDefinition) {
            return new MethodsFiller(toMethodSpec, isGraphqlMethodField)
                    .andThen(dgraphTypeFiller)
                    .andThen(autowiredFieldFiller)
                    .apply(typeSpecConvert.apply(objectTypeDefinition), objectTypeDefinition);
        }

    }

    static class OperatorGenerator implements Function<ObjectTypeDefinition, TypeSpec.Builder> {

        private final TypeSpecConvert typeSpecConvert;
        private final AutowiredFieldFiller autowiredFieldFiller;
        private final MethodSpecBuilder toMethodSpec;

        public OperatorGenerator(TypeSpecConvert typeSpecConvert, AutowiredFieldFiller autowiredFieldFiller, MethodSpecBuilder toMethodSpec) {
            this.typeSpecConvert = typeSpecConvert;
            this.autowiredFieldFiller = autowiredFieldFiller;
            this.toMethodSpec = toMethodSpec;
        }

        @Override
        public TypeSpec.Builder apply(ObjectTypeDefinition objectTypeDefinition) {
            return new MethodsFiller(toMethodSpec, p -> true)
                    .andThen(autowiredFieldFiller)
                    .apply(typeSpecConvert.apply(objectTypeDefinition), objectTypeDefinition);
        }
    }

    /**
     * Union 需要融合多个类的内容提供转换能力
     * 目前只生成class未处理
     */
    static class UnionGenerator implements Function<UnionTypeDefinition, TypeSpec.Builder> {

        @Override
        public TypeSpec.Builder apply(UnionTypeDefinition unionTypeDefinition) {
            return TypeSpec.classBuilder(unionTypeDefinition.getName())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addJavadoc(JavadocUtils.getDocForType(unionTypeDefinition));
        }
    }

    /**
     * 创建 interface 类并生成 abstract getter 方法
     */
    static class InterfaceGenerator implements Function<InterfaceTypeDefinition, TypeSpec.Builder> {

        private final FieldSpecGenAbstractGetter fieldSpecGenAbstractGetter = new FieldSpecGenAbstractGetter();
        private final GraphqlType2JavapoetTypeName toJavapoetTypeName;

        public InterfaceGenerator(GraphqlType2JavapoetTypeName toJavapoetTypeName) {
            this.toJavapoetTypeName = toJavapoetTypeName;
        }

        @Override
        public TypeSpec.Builder apply(InterfaceTypeDefinition interfaceTypeDefinition) {
            return TypeSpec.interfaceBuilder(StringUtils.capitalize(interfaceTypeDefinition.getName()))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethods(
                            interfaceTypeDefinition.getFieldDefinitions().stream()
                                    .map(fieldDefinition ->
                                            fieldSpecGenAbstractGetter.apply(FieldSpec.builder(toJavapoetTypeName.apply(fieldDefinition.getType()), fieldDefinition.getName()).build())
                                    ).collect(Collectors.toList())
                    )
                    .addJavadoc(JavadocUtils.getDocForType(interfaceTypeDefinition));
        }
    }

    static class MethodSpecBuilder implements BiFunction<ObjectTypeDefinition, FieldDefinition, MethodSpec> {

        private final GraphqlType2JavapoetTypeName toJavapoetTypeName;

        public MethodSpecBuilder(GraphqlType2JavapoetTypeName toJavapoetTypeName) {
            this.toJavapoetTypeName = toJavapoetTypeName;
        }

        @Override
        public MethodSpec apply(ObjectTypeDefinition objectTypeDefinition, FieldDefinition fieldDefinition) {
            return MethodSpec.methodBuilder(fieldDefinition.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(AnnotationSpec.builder(GraphqlMethod.class).addMember("type", "$S", objectTypeDefinition.getName()).build())
                    .addStatement("return dataFetchingEnvironment -> $L.handle$L(dataFetchingEnvironment)", SpecNameManager.getUnCapitalizeApiName(objectTypeDefinition), StringUtils.capitalize(fieldDefinition.getName()))
                    .returns(ParameterizedTypeName.get(ClassName.get(DataFetcher.class), toJavapoetTypeName.apply(fieldDefinition.getType())))
                    .build();
        }
    }

    static class TypeSpecConvert implements Function<ObjectTypeDefinition, TypeSpec.Builder> {
        private final GraphqlType2JavapoetTypeName toJavapoetTypeName;

        public TypeSpecConvert(GraphqlType2JavapoetTypeName toJavapoetTypeName) {
            this.toJavapoetTypeName = toJavapoetTypeName;
        }

        @Override
        public TypeSpec.Builder apply(ObjectTypeDefinition objectTypeDefinition) {
            return TypeSpec.classBuilder(StringUtils.capitalize(objectTypeDefinition.getName()))
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Graphql.class)
                    .addSuperinterface(IDomainClass.class)
                    .addSuperinterfaces(objectTypeDefinition.getImplements().stream()
                            .map(it -> toJavapoetTypeName.apply(GraphqlTypeUtils.getUnWrapperType(it)))
                            .collect(Collectors.toList()))
                    .addJavadoc(JavadocUtils.getDocForType(objectTypeDefinition));
        }
    }

    static class DgraphTypeFiller implements Filler<TypeSpec.Builder, ObjectTypeDefinition> {

        private final GraphqlType2JavapoetTypeName toJavapoetTypeName;
        private final FieldSpecGenGetter fieldSpecGenGetter = new FieldSpecGenGetter();
        private final FieldSpecGenSetter fieldSpecGenSetter = new FieldSpecGenSetter();

        public DgraphTypeFiller(GraphqlType2JavapoetTypeName toJavapoetTypeName) {
            this.toJavapoetTypeName = toJavapoetTypeName;
        }

        @Override
        public TypeSpec.Builder apply(TypeSpec.Builder typeSpecBuilder, ObjectTypeDefinition objectTypeDefinition) {
            typeSpecBuilder.addAnnotation(AnnotationSpec.builder(DgraphType.class)
                    .addMember("value", "$S", objectTypeDefinition.getName().toUpperCase()).build());
            List<FieldSpec> fieldSpecs = objectTypeDefinition.getFieldDefinitions().stream()
                    .map(fieldDefinition -> {
                        FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(toJavapoetTypeName.apply(fieldDefinition.getType()), fieldDefinition.getName(), Modifier.PRIVATE)
                                .addJavadoc(JavadocUtils.getFieldDocByDescription(fieldDefinition.getDescription(), fieldDefinition.getName()));
                        if (DEFAULT_UID_FIELD_NAME.equals(fieldDefinition.getName())) {
                            fieldSpecBuilder.addAnnotation(UidField.class);
                        }
                        return fieldSpecBuilder.build();
                    })
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(fieldSpecs)) {
                fieldSpecs.forEach(fieldSpec -> {
                    typeSpecBuilder.addMethod(fieldSpecGenSetter.apply(fieldSpec));
                    typeSpecBuilder.addMethod(fieldSpecGenGetter.apply(fieldSpec));
                });
                typeSpecBuilder.addFields(fieldSpecs);
            }
            return typeSpecBuilder;
        }
    }

    static class AutowiredFieldFiller implements Filler<TypeSpec.Builder, ObjectTypeDefinition> {

        private final Predicate<ObjectTypeDefinition> isContainGraphqlMethodField;
        private final PackageManager packageManager;

        public AutowiredFieldFiller(Predicate<ObjectTypeDefinition> isContainGraphqlMethodField, PackageManager packageManager) {
            this.isContainGraphqlMethodField = isContainGraphqlMethodField;
            this.packageManager = packageManager;
        }

        @Override
        public TypeSpec.Builder apply(TypeSpec.Builder typeSpecBuilder, ObjectTypeDefinition objectTypeDefinition) {
            if (isContainGraphqlMethodField.test(objectTypeDefinition)) {
                typeSpecBuilder.addField(
                        FieldSpec.builder(ClassName.get(packageManager.getInterfacePackage(), SpecNameManager.getApiName(objectTypeDefinition)), SpecNameManager.getUnCapitalizeApiName(objectTypeDefinition))
                                .addModifiers(Modifier.PRIVATE)
                                .addAnnotation(Autowired.class)
                                .build()
                );
            }
            return typeSpecBuilder;
        }
    }

    static class MethodsFiller implements Filler<TypeSpec.Builder, ObjectTypeDefinition> {

        private final MethodSpecBuilder methodSpecBuilder;
        private final Predicate<FieldDefinition> filter;

        public MethodsFiller(MethodSpecBuilder methodSpecBuilder, Predicate<FieldDefinition> filter) {
            this.methodSpecBuilder = methodSpecBuilder;
            this.filter = filter;
        }

        @Override
        public TypeSpec.Builder apply(TypeSpec.Builder typeSpecBuilder, ObjectTypeDefinition objectTypeDefinition) {
            objectTypeDefinition.getFieldDefinitions().stream()
                    .filter(filter)
                    .map(fieldDefinition -> methodSpecBuilder.apply(objectTypeDefinition, fieldDefinition))
                    .forEach(typeSpecBuilder::addMethod);
            return typeSpecBuilder;
        }
    }
}