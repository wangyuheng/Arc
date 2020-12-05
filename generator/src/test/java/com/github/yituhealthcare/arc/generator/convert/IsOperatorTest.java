package com.github.yituhealthcare.arc.generator.convert;

import com.github.yituhealthcare.arc.graphql.util.GraphqlTypeUtils;
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
        final String operationName = "q";
        final String operationTypeName = "Q";
        final String notOperationName = "m";
        TypeDefinitionRegistry typeDefinitionRegistry = PowerMockito.spy(new TypeDefinitionRegistry());
        PowerMockito.field(TypeDefinitionRegistry.class, "schema").set(typeDefinitionRegistry, SchemaDefinition.newSchemaDefinition()
                .operationTypeDefinition(OperationTypeDefinition.newOperationTypeDefinition().name(operationName).typeName(TypeName.newTypeName(operationTypeName).build()).build())
                .build());
        typeDefinitionRegistry.add(this.buildObjectTypeDefinitionWithName(notOperationName));
        IsOperator isOperator = new IsOperator(GraphqlTypeUtils.getOperationTypeNames(typeDefinitionRegistry));

        assertTrue(isOperator.test(this.buildObjectTypeDefinitionWithName(operationTypeName)));
        assertFalse(isOperator.test(this.buildObjectTypeDefinitionWithName(operationName)));
        assertFalse(isOperator.test(this.buildObjectTypeDefinitionWithName(notOperationName)));
    }

    private ObjectTypeDefinition buildObjectTypeDefinitionWithName(String name) {
        return ObjectTypeDefinition.newObjectTypeDefinition().name(name).build();
    }
}