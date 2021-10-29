package io.github.wangyuheng.arc.generator.codegen;

import io.github.wangyuheng.arc.generator.CodeGenResource;
import io.github.wangyuheng.arc.generator.CodeGenResourceArgument;
import io.github.wangyuheng.arc.generator.CodeGenResources;
import io.github.wangyuheng.arc.generator.codegen.util.PackageManager;
import com.squareup.javapoet.JavaFile;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

/**
 * 代码生成器全局测试
 * ? implements {@link IGenerator}
 *
 * @author yuheng.wang
 */
public class GeneratorIntegralTest {

//    @ParameterizedTest
//    @CodeGenResources({
//            @CodeGenResource(generatorClazz = DataFetcherGenerator.class, sourceGraphqlSchemaPath = "data_fetcher_test.graphqls", generatedJavaCodePaths = {"data_fetcher_test_ProjectDataFetcher.java", "data_fetcher_test_MutationDataFetcher.java", "data_fetcher_test_QueryDataFetcher.java"}),
//            @CodeGenResource(generatorClazz = DictionaryGenerator.class, sourceGraphqlSchemaPath = "dictionary_test.graphqls", generatedJavaCodePaths = {"dictionary_test_E1.java"}),
//            @CodeGenResource(generatorClazz = InputGenerator.class, sourceGraphqlSchemaPath = "input_test.graphqls", generatedJavaCodePaths = {"input_test_I1.java"}),
//            @CodeGenResource(generatorClazz = RepositoryGenerator.class, sourceGraphqlSchemaPath = "repository_test.graphqls", generatedJavaCodePaths = {"repository_test_ProjectRepository.java"}),
//            @CodeGenResource(generatorClazz = TypeGenerator.class, sourceGraphqlSchemaPath = "type_test.graphqls", generatedJavaCodePaths = {"type_test_Milestone.java", "type_test_Mutation.java", "type_test_Project.java", "type_test_Query.java", "type_test_User.java"}),
//            @CodeGenResource(generatorClazz = TypeGenerator.class, sourceGraphqlSchemaPath = "type_union_test.graphqls", generatedJavaCodePaths = {"type_union_test_Milestone.java", "type_union_test_Entity.java", "type_union_test_Project.java"}),
//            @CodeGenResource(generatorClazz = TypeGenerator.class, sourceGraphqlSchemaPath = "type_interface_test.graphqls", generatedJavaCodePaths = {"type_interface_test_Milestone.java", "type_interface_test_Entity.java", "type_interface_test_Project.java"})
//    })
    public void gen_e2e_test(CodeGenResourceArgument argument) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        final TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(new ClassPathResource(argument.getSourceGraphqlSchemaPath()).getInputStream());
        final PackageManager packageManager = new PackageManager(argument.getBasePackage(), typeDefinitionRegistry);
        final IGenerator generator = argument.getGeneratorClazz().getConstructor(PackageManager.class).newInstance(packageManager);

        final List<String> list = new ArrayList<>();
        final String[] javaPaths = argument.getGeneratedJavaCodePaths();
        Arrays.sort(javaPaths);
        for (String javaPath : javaPaths) {
            list.add(String.join("", Files.readAllLines(new ClassPathResource(javaPath).getFile().toPath())));
        }
        assertArrayEquals(generator.apply(typeDefinitionRegistry)
                        .sorted(Comparator.comparing(o -> o.typeSpec.name))
                        .map(JavaFile::toString)
                        .map(it -> it.replaceAll("\n", ""))
                        .map(it -> it.replaceAll("\\s+", ""))
                        .toArray()
                , list.stream()
                        .map(it -> it.replaceAll("\n", ""))
                        .map(it -> it.replaceAll("\\s+", "")).toArray());
    }

}
