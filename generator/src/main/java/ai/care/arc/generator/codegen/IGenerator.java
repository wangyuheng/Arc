package ai.care.arc.generator.codegen;

import com.squareup.javapoet.JavaFile;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.util.function.Function;
import java.util.stream.Stream;

public interface IGenerator extends Function<TypeDefinitionRegistry, Stream<JavaFile>> {
}
