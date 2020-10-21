package ai.care.arc.generator;

import ai.care.arc.generator.codegen.IGenerator;
import ai.care.arc.generator.codegen.util.PackageManager;
import com.squareup.javapoet.JavaFile;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;

/**
 * Gen e2e 测试扩展
 * 比较根据schema生成的代码与预期的java文件
 *
 * @author yuheng.wang
 * @see GenE2ECase
 */
public class GenE2EExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        final Optional<GenE2ECase> ep = extensionContext.getElement().map(it -> it.getDeclaredAnnotation(GenE2ECase.class));
        if (ep.isPresent()) {
            final GenE2ECase e = ep.get();
            final TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(new ClassPathResource(e.schemaPath()).getInputStream());
            final PackageManager packageManager = new PackageManager(e.basePackage(), typeDefinitionRegistry);
            final IGenerator generator = e.clazz().getConstructor(PackageManager.class).newInstance(packageManager);

            final List<String> list = new ArrayList<>();
            final String[] javaPaths = e.javaPaths();
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

}
