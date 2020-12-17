package com.github.yituhealthcare.arc.generator.codegen.util;

import com.github.yituhealthcare.arc.generator.conf.CodeGenType;
import com.github.yituhealthcare.arc.graphql.util.GraphqlTypeUtils;
import graphql.language.*;
import graphql.schema.idl.TypeDefinitionRegistry;

/**
 * 生成java文件包路径管理
 *
 * @author yuheng.wang
 */
public class PackageManager {

    private static final String PACKAGE_SEPARATOR = ".";

    private final String basePackage;
    private final TypeDefinitionRegistry typeDefinitionRegistry;

    public PackageManager(String basePackage, TypeDefinitionRegistry typeDefinitionRegistry) {
        this.basePackage = basePackage;
        this.typeDefinitionRegistry = typeDefinitionRegistry;
    }

    public String getTypePackage() {
        return this.basePackage + PACKAGE_SEPARATOR + CodeGenType.TYPE.getDirName();
    }

    public String getRepoPackage() {
        return this.basePackage + PACKAGE_SEPARATOR + CodeGenType.REPO.getDirName();
    }

    public String getInterfacePackage() {
        return this.basePackage + PACKAGE_SEPARATOR + CodeGenType.DATA_FETCHER.getDirName();
    }

    public String getInputPackage() {
        return this.basePackage + PACKAGE_SEPARATOR + CodeGenType.INPUT.getDirName();
    }

    public String getEnumPackage() {
        return this.basePackage + PACKAGE_SEPARATOR + CodeGenType.DICTIONARY.getDirName();
    }

    /**
     * 根据graphqlType返回对应的包路径
     */
    public String getPackageByGraphqlType(Type<?> graphqlType) {
        Type<?> unWrapperType = GraphqlTypeUtils.getUnWrapperType(graphqlType);
        if (typeDefinitionRegistry.getType(unWrapperType, EnumTypeDefinition.class).isPresent()) {
            return this.getEnumPackage();
        } else if (typeDefinitionRegistry.getType(unWrapperType, InputObjectTypeDefinition.class).isPresent()) {
            return this.getInputPackage();
        } else if (typeDefinitionRegistry.getType(unWrapperType, ObjectTypeDefinition.class).isPresent()
                || typeDefinitionRegistry.getType(unWrapperType, UnionTypeDefinition.class).isPresent()
                || typeDefinitionRegistry.getType(unWrapperType, InterfaceTypeDefinition.class).isPresent()) {
            return this.getTypePackage();
        } else {
            throw new IllegalArgumentException("not support or type not existed graphqlType: " + unWrapperType);
        }
    }
}
