package ai.care.arc.generator.codegen;

import graphql.language.*;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link PackageManager}.
 *
 * @author yuheng.wang
 */
public class PackageManagerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private PackageManager packageManager;

    @Before
    public void setUp() throws Exception {
        TypeDefinitionRegistry typeDefinitionRegistry = PowerMockito.spy(new TypeDefinitionRegistry());
        typeDefinitionRegistry.add(EnumTypeDefinition.newEnumTypeDefinition().name("e1").build());
        typeDefinitionRegistry.add(ObjectTypeDefinition.newObjectTypeDefinition().name("t1").build());
        typeDefinitionRegistry.add(InputObjectTypeDefinition.newInputObjectDefinition().name("i1").build());
        typeDefinitionRegistry.add(UnionTypeDefinition.newUnionTypeDefinition().name("u1").build());
        packageManager = new PackageManager("a.b.c", typeDefinitionRegistry);
    }

    @Test
    public void should_get_package_by_type() {
        assertEquals(packageManager.getPackageByGraphqlType(TypeName.newTypeName("e1").build()), packageManager.getEnumPackage());
        assertEquals(packageManager.getPackageByGraphqlType(TypeName.newTypeName("t1").build()), packageManager.getTypePackage());
        assertEquals(packageManager.getPackageByGraphqlType(TypeName.newTypeName("i1").build()), packageManager.getInputPackage());
    }

    /**
     * 业务场景中，type通过schema解析，一定存在
     */
    @Test
    public void should_throw_if_type_not_existed() {
        thrown.expect(IllegalArgumentException.class);
        packageManager.getPackageByGraphqlType(TypeName.newTypeName("aaa").build());
    }

    @Test
    public void should_return_type_if_union_type() {
        assertEquals(packageManager.getTypePackage(), packageManager.getPackageByGraphqlType(TypeName.newTypeName("u1").build()));
    }

}