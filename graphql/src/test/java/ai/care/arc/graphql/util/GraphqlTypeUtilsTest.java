package ai.care.arc.graphql.util;

import graphql.language.*;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Tests for {@link GraphqlTypeUtils}.
 *
 * @author yuheng.wang
 */
public class GraphqlTypeUtilsTest {

    private final Type<?> COMMON_TYPE = TypeName.newTypeName("T").build();

    @Test
    public void should_return_true_if_type_is_enum_definition() {
        TypeDefinitionRegistry typeDefinitionRegistry = new TypeDefinitionRegistry();

        typeDefinitionRegistry.add(EnumTypeDefinition.newEnumTypeDefinition().name("e1").build());
        typeDefinitionRegistry.add(EnumTypeDefinition.newEnumTypeDefinition().name("e2").build());

        assertTrue(GraphqlTypeUtils.isEnumType(typeDefinitionRegistry, TypeName.newTypeName("e1").build()));
        assertTrue(GraphqlTypeUtils.isEnumType(typeDefinitionRegistry, TypeName.newTypeName("e1").build()));
        assertFalse(GraphqlTypeUtils.isEnumType(typeDefinitionRegistry, TypeName.newTypeName("e3").build()));
    }

    @Test
    public void should_return_true_if_type_is_enum_definitiosn() throws IllegalAccessException {
        TypeDefinitionRegistry typeDefinitionRegistry = PowerMockito.spy(new TypeDefinitionRegistry());
        PowerMockito.field(TypeDefinitionRegistry.class, "schema").set(typeDefinitionRegistry, SchemaDefinition.newSchemaDefinition()
                .operationTypeDefinition(OperationTypeDefinition.newOperationTypeDefinition().name("q").typeName(TypeName.newTypeName("Q").build()).build())
                .operationTypeDefinition(OperationTypeDefinition.newOperationTypeDefinition().name("m").typeName(TypeName.newTypeName("M").build()).build())
                .build());

        typeDefinitionRegistry.add(EnumTypeDefinition.newEnumTypeDefinition().name("e1").build());
        typeDefinitionRegistry.add(EnumTypeDefinition.newEnumTypeDefinition().name("e2").build());

        assertEquals(Arrays.asList("Q", "M"), GraphqlTypeUtils.getOperationTypeNames(typeDefinitionRegistry));
    }

    @Test
    public void is_list_type_when_nonnull_wrapper_list() {
        assertTrue(GraphqlTypeUtils.isListType(NonNullType.newNonNullType()
                .type(ListType.newListType().type(COMMON_TYPE).build())
                .build()));
    }

    @Test
    public void is_not_list_type_when_nonnull_wrapper_type() {
        assertFalse(GraphqlTypeUtils.isListType(NonNullType.newNonNullType()
                .type(COMMON_TYPE)
                .build()));
    }

}