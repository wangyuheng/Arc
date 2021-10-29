package io.github.wangyuheng.arc.generator.convert;

import io.github.wangyuheng.arc.core.common.GraphqlFieldType2JavaTypeConverter;
import io.github.wangyuheng.arc.core.dictionary.GraphqlFieldTypeEnum;
import io.github.wangyuheng.arc.generator.codegen.util.PackageManager;
import io.github.wangyuheng.arc.generator.dictionary.GeneratorGlobalConst;
import io.github.wangyuheng.arc.graphql.util.GraphqlTypeUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import graphql.language.Type;
import graphql.schema.idl.TypeInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * graphql与javapoet类型转换
 *
 * @author yuheng.wang
 */
public class GraphqlType2JavapoetTypeName implements Function<Type<?>, TypeName> {

    private final GraphqlFieldType2JavaTypeConverter graphqlFieldType2JavaTypeConverter = new GraphqlFieldType2JavaTypeConverter();

    private final PackageManager packageManager;

    public GraphqlType2JavapoetTypeName(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public TypeName apply(Type type) {
        final String graphqlTypeName = TypeInfo.typeInfo(type).getName();
        final Optional<Class<?>> typeOptional = GraphqlFieldTypeEnum.parse(graphqlTypeName).map(graphqlFieldType2JavaTypeConverter::convert);

        if (GraphqlTypeUtils.isListType(type)) {
            return typeOptional
                    .map(it -> {
                        if (Map.class.equals(it)) {
                            return ParameterizedTypeName.get(GeneratorGlobalConst.DEFAULT_LIST_CLASS_NAME, GeneratorGlobalConst.DEFAULT_MAP_TYPE);
                        } else {
                            return ParameterizedTypeName.get(List.class, it);
                        }
                    })
                    .orElseGet(() -> ParameterizedTypeName.get(GeneratorGlobalConst.DEFAULT_LIST_CLASS_NAME, ClassName.get(packageManager.getPackageByGraphqlType(type), graphqlTypeName)));
        } else {
            return typeOptional
                    .map(it -> {
                        if (Map.class.equals(it)) {
                            return GeneratorGlobalConst.DEFAULT_MAP_TYPE;
                        } else {
                            return TypeName.get(it);
                        }
                    })
                    .orElseGet(() -> ClassName.get(packageManager.getPackageByGraphqlType(type), graphqlTypeName));
        }
    }

}