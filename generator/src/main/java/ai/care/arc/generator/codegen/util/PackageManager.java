package ai.care.arc.generator.codegen.util;

import ai.care.arc.generator.conf.CodeGenType;
import graphql.language.*;
import graphql.schema.idl.TypeDefinitionRegistry;

/**
 * 生成java文件包路径管理
 *
 * @author yuheng.wang
 */
public class PackageManager {

    private static final String PACKAGE_SEPARATOR = ".";

    private String basePackage;
    private TypeDefinitionRegistry typeDefinitionRegistry;

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
        return this.basePackage + PACKAGE_SEPARATOR + CodeGenType.API.getDirName();
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
        if (typeDefinitionRegistry.getType(graphqlType, EnumTypeDefinition.class).isPresent()) {
            return this.getEnumPackage();
        } else if (typeDefinitionRegistry.getType(graphqlType, InputObjectTypeDefinition.class).isPresent()) {
            return this.getInputPackage();
        } else if (typeDefinitionRegistry.getType(graphqlType, ObjectTypeDefinition.class).isPresent()
                || typeDefinitionRegistry.getType(graphqlType, UnionTypeDefinition.class).isPresent()
                || typeDefinitionRegistry.getType(graphqlType, InterfaceTypeDefinition.class).isPresent()) {
            return this.getTypePackage();
        } else {
            throw new IllegalArgumentException("not support or type not existed graphqlType: " + graphqlType);
        }
    }
}
