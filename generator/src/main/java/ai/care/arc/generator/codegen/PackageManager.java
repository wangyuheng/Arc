package ai.care.arc.generator.codegen;

import graphql.language.EnumTypeDefinition;
import graphql.language.InputObjectTypeDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.Type;
import graphql.schema.idl.TypeDefinitionRegistry;

/**
 * 生成java文件包路径管理
 *
 * @author yuheng.wang
 */
public class PackageManager {

    private String basePackage;
    private TypeDefinitionRegistry typeDefinitionRegistry;

    public PackageManager(String basePackage, TypeDefinitionRegistry typeDefinitionRegistry) {
        this.basePackage = basePackage;
        this.typeDefinitionRegistry = typeDefinitionRegistry;
    }

    public String getTypePackage() {
        return this.basePackage + ".type";
    }

    public String getRepoPackage() {
        return this.basePackage + ".repo";
    }

    public String getInterfacePackage() {
        return this.basePackage + ".api";
    }

    public String getInputPackage() {
        return this.basePackage + ".input";
    }

    public String getEnumPackage() {
        return this.basePackage + ".dictionary";
    }

    /**
     * 根据graphqlType返回对应的包路径
     */
    public String getPackageByGraphqlType(Type<?> graphqlType) {
        if (typeDefinitionRegistry.getType(graphqlType, EnumTypeDefinition.class).isPresent()) {
            return this.getEnumPackage();
        } else if (typeDefinitionRegistry.getType(graphqlType, InputObjectTypeDefinition.class).isPresent()) {
            return this.getInputPackage();
        } else if (typeDefinitionRegistry.getType(graphqlType, ObjectTypeDefinition.class).isPresent()) {
            return this.getTypePackage();
        } else {
            throw new IllegalArgumentException("not support or type not existed graphqlType: " + graphqlType);
        }
    }
}
