package com.github.yituhealthcare.arc.generator.convert;

import com.github.yituhealthcare.arc.core.common.GraphqlFieldType2JavaTypeConverter;
import com.github.yituhealthcare.arc.core.dictionary.GraphqlFieldTypeEnum;
import com.github.yituhealthcare.arc.generator.codegen.util.PackageManager;
import com.github.yituhealthcare.arc.generator.dictionary.GeneratorGlobalConst;
import com.github.yituhealthcare.arc.graphql.util.GraphqlTypeUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import graphql.language.Type;
import graphql.schema.idl.TypeInfo;

import java.util.List;
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
                    .map(value -> ParameterizedTypeName.get(List.class, value))
                    .orElseGet(() -> ParameterizedTypeName.get(GeneratorGlobalConst.DEFAULT_LIST_CLASS_NAME, ClassName.get(packageManager.getPackageByGraphqlType(type), graphqlTypeName)));
        } else {
            return typeOptional
                    .map(TypeName::get)
                    .orElseGet(() -> ClassName.get(packageManager.getPackageByGraphqlType(type), graphqlTypeName));
        }
    }

}
