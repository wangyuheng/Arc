package com.github.yituhealthcare.arc.generator.codegen.util;

import graphql.language.ObjectTypeDefinition;
import org.springframework.util.StringUtils;

import static com.github.yituhealthcare.arc.generator.dictionary.GeneratorGlobalConst.INTERFACE_SUFFIX;

public class SpecNameManager {


    private SpecNameManager() {
    }

    public static String getApiName(ObjectTypeDefinition objectTypeDefinition) {
        return objectTypeDefinition.getName() + INTERFACE_SUFFIX;
    }

    public static String getUnCapitalizeApiName(ObjectTypeDefinition objectTypeDefinition) {
        return StringUtils.uncapitalize(getApiName(objectTypeDefinition));
    }


}
