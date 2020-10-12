package ai.care.arc.generator;

import ai.care.arc.generator.codegen.*;
import ai.care.arc.generator.conf.CodeGenConfig;
import ai.care.arc.generator.conf.CodeGenOperation;
import ai.care.arc.generator.conf.CodeGenType;
import ai.care.arc.generator.convert.IsContainsGraphqlMethodField;
import ai.care.arc.generator.convert.IsOperator;
import ai.care.arc.generator.io.CodeWriter;
import ai.care.arc.graphql.util.GraphqlTypeUtils;
import com.squareup.javapoet.JavaFile;
import graphql.language.EnumTypeDefinition;
import graphql.language.InputObjectTypeDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.UnionTypeDefinition;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.InputStream;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 根据GraphqlSchema生成JavaCode，包括
 * <p>
 * 1. enum {@link EnumGenerator}
 * 2. input type {@link InputGenerator}
 * 3. type {@link TypeForObjectGenerator}
 * 4. repository {@link RepositoryGenerator}
 * 5. interface {@link InterfaceGenerator} 此interface为java接口类, graphql interface归类为type
 * <p>
 * 开发者只需编写interface的实现类
 *
 * @author yuheng.wang
 */
public class JavaCodeGenerator {

    private CodeWriter codeWriter;
    private CodeGenConfig config;

    public JavaCodeGenerator(CodeWriter codeWriter, CodeGenConfig config) {
        this.codeWriter = codeWriter;
        this.config = config;
    }

    public JavaCodeGenerator(CodeWriter codeWriter) {
        this.codeWriter = codeWriter;
        this.config = new CodeGenConfig(Collections.emptyList());
    }

    private final Predicate<ObjectTypeDefinition> isContainGraphqlMethodField = new IsContainsGraphqlMethodField();

    private final Predicate<JavaFile> canExec = javaFile -> {
        if (config.getIgnoreJavaFileNames().contains(javaFile.packageName)) {
            return false;
        } else {
            CodeGenOperation operation = config.getOperationByType(CodeGenType.parse(javaFile.packageName));
            switch (operation) {
                case OVERRIDE:
                    return true;
                case SKIP:
                    return false;
                case SKIP_IF_EXISTED:
                    return !codeWriter.exist(javaFile);
                default:
                    throw new IllegalArgumentException("operation illegal");
            }
        }
    };

    public void generate(InputStream inputStream, String basePackage) {
        this.parseJavaFileStream(inputStream, basePackage)
                .filter(canExec)
                .forEach(codeWriter::write);
    }

    private Stream<JavaFile> parseJavaFileStream(InputStream schema, String basePackage) {
        final TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(schema);
        final PackageManager packageManager = new PackageManager(basePackage, typeDefinitionRegistry);

        final Predicate<ObjectTypeDefinition> isOperator = new IsOperator(GraphqlTypeUtils.getOperationTypeNames(typeDefinitionRegistry));

        final Function<TypeDefinitionRegistry, Stream<JavaFile>> genEnums = t -> t.getTypes(EnumTypeDefinition.class).stream()
                .map(new EnumGenerator(packageManager));
        final Function<TypeDefinitionRegistry, Stream<JavaFile>> genInputs = t -> t.getTypes(InputObjectTypeDefinition.class).stream()
                .map(new InputGenerator(packageManager));
        final Function<TypeDefinitionRegistry, Stream<JavaFile>> genTypes = t -> t.getTypes(ObjectTypeDefinition.class).stream()
                .map(new TypeForObjectGenerator(isOperator, packageManager));
        final Function<TypeDefinitionRegistry, Stream<JavaFile>> genTypesForUnion = t -> t.getTypes(UnionTypeDefinition.class).stream()
                .map(new TypeForUnionGenerator(packageManager));
        final Function<TypeDefinitionRegistry, Stream<JavaFile>> genRepos = t -> t.getTypes(ObjectTypeDefinition.class).stream()
                .filter(isOperator.negate())
                .map(new RepositoryGenerator(packageManager));
        final Function<TypeDefinitionRegistry, Stream<JavaFile>> genTypeInterfaces = t -> t.getTypes(ObjectTypeDefinition.class).stream()
                .filter(isOperator.negate().and(isContainGraphqlMethodField))
                .distinct()
                .map(new InterfaceGenerator(packageManager));
        final Function<TypeDefinitionRegistry, Stream<JavaFile>> genTypeInterfacesByOperator = t -> t.getTypes(ObjectTypeDefinition.class).stream()
                .filter(isOperator)
                .distinct()
                .map(new OperatorInterfaceGenerator(packageManager));

        return Stream.of(genEnums, genInputs, genTypes, genTypesForUnion, genRepos, genTypeInterfaces, genTypeInterfacesByOperator)
                .flatMap(it -> it.apply(typeDefinitionRegistry));
    }
}
