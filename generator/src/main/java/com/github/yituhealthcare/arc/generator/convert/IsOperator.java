package com.github.yituhealthcare.arc.generator.convert;

import graphql.language.ObjectTypeDefinition;

import java.util.List;
import java.util.function.Predicate;

/**
 * 判断 {@link ObjectTypeDefinition} 是否为 {@link graphql.language.OperationTypeDefinition}
 *
 * @author yuheng.wang
 */
public class IsOperator implements Predicate<ObjectTypeDefinition> {

    private final List<String> operationTypeNames;

    public IsOperator(List<String> operationTypeNames) {
        this.operationTypeNames = operationTypeNames;
    }

    @Override
    public boolean test(ObjectTypeDefinition typeDefinition) {
        return operationTypeNames.contains(typeDefinition.getName());
    }
}
