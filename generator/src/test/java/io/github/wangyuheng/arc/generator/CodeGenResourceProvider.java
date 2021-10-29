package io.github.wangyuheng.arc.generator;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class CodeGenResourceProvider implements ArgumentsProvider, AnnotationConsumer<CodeGenResources> {

    private List<CodeGenResourceArgument> arguments = new ArrayList<>();

    @Override
    public void accept(CodeGenResources codeGenResources) {
        Arrays.stream(codeGenResources.value())
                .map(it -> new CodeGenResourceArgument(it.basePackage(),
                        it.generatorClazz(),
                        it.sourceGraphqlSchemaPath(),
                        it.generatedJavaCodePaths()))
                .forEach(it -> arguments.add(it));
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return arguments.stream().map(Arguments::of);
    }
}
