package ai.care.arc.generator.convert;

import graphql.language.ObjectTypeDefinition;
import graphql.language.OperationTypeDefinition;
import graphql.language.SchemaDefinition;
import graphql.language.TypeName;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link IsOperator}.
 *
 * @author yuheng.wang
 */
public class IsOperatorTest {

    @Test
    public void should_return_true_if_type_is_operation() throws IllegalAccessException {
        TypeDefinitionRegistry typeDefinitionRegistry = PowerMockito.spy(new TypeDefinitionRegistry());
        PowerMockito.field(TypeDefinitionRegistry.class, "schema").set(typeDefinitionRegistry, SchemaDefinition.newSchemaDefinition()
                .operationTypeDefinition(OperationTypeDefinition.newOperationTypeDefinition().name("q").typeName(TypeName.newTypeName("Q").build()).build())
                .build());
        typeDefinitionRegistry.add(ObjectTypeDefinition.newObjectTypeDefinition().name("m").build());
        IsOperator isOperator = new IsOperator(typeDefinitionRegistry);

        assertTrue(isOperator.test(ObjectTypeDefinition.newObjectTypeDefinition().name("Q").build()));
        assertFalse(isOperator.test(ObjectTypeDefinition.newObjectTypeDefinition().name("q").build()));
        assertFalse(isOperator.test(ObjectTypeDefinition.newObjectTypeDefinition().name("m").build()));
    }

}