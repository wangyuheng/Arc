package ai.care.arc.generator.convert;

import graphql.language.ObjectTypeDefinition;

import java.util.function.Predicate;

/**
 * 判断是否包含graphql method
 *
 * @author yuheng.wang
 */
public class IsContainsGraphqlMethodField implements Predicate<ObjectTypeDefinition> {

    private final IsGraphqlMethodField isGraphqlMethodField = new IsGraphqlMethodField();

    @Override
    public boolean test(ObjectTypeDefinition typeDefinition) {
        return typeDefinition.getFieldDefinitions().stream().anyMatch(isGraphqlMethodField);
    }

}
