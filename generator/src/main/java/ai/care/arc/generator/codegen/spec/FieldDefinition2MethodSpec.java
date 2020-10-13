package ai.care.arc.generator.codegen.spec;

import ai.care.arc.generator.convert.GraphqlType2JavapoetTypeName;
import ai.care.arc.generator.dictionary.GeneratorGlobalConst;
import com.squareup.javapoet.MethodSpec;
import graphql.language.FieldDefinition;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.function.Function;

/**
 * 根据 Graphql {@link FieldDefinition} DataFetcher方法 {@link MethodSpec}
 *
 * @author yuheng.wang
 */
public class FieldDefinition2MethodSpec implements Function<FieldDefinition, MethodSpec> {

    private GraphqlType2JavapoetTypeName graphqlType2JavapoetTypeName;

    public FieldDefinition2MethodSpec(GraphqlType2JavapoetTypeName graphqlType2JavapoetTypeName) {
        this.graphqlType2JavapoetTypeName = graphqlType2JavapoetTypeName;
    }

    @Override
    public MethodSpec apply(FieldDefinition fieldDefinition) {
        return MethodSpec.methodBuilder(GeneratorGlobalConst.HANDLE_METHOD_SUFFIX + StringUtils.capitalize(fieldDefinition.getName()))
                .addParameter(DataFetchingEnvironment.class, StringUtils.uncapitalize(DataFetchingEnvironment.class.getSimpleName()))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(graphqlType2JavapoetTypeName.apply(fieldDefinition.getType()))
                .build();
    }

}