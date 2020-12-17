package com.github.yituhealthcare.arc.generator.codegen.util;

import com.github.yituhealthcare.arc.generator.dictionary.GeneratorGlobalConst;
import graphql.language.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JavadocUtils}.
 *
 * @author yuheng.wang
 */
public class JavadocUtilsTest {

    @Test
    public void all_type_should_get_description() {
        final String classInstanceName = "random";
        Stream<TypeDefinition<?>> items = Stream.of(ObjectTypeDefinition.newObjectTypeDefinition().name(classInstanceName).build(),
                InterfaceTypeDefinition.newInterfaceTypeDefinition().name(classInstanceName).build(),
                UnionTypeDefinition.newUnionTypeDefinition().name(classInstanceName).build(),
                ScalarTypeDefinition.newScalarTypeDefinition().name(classInstanceName).build(),
                EnumTypeDefinition.newEnumTypeDefinition().name(classInstanceName).build(),
                InputObjectTypeDefinition.newInputObjectDefinition().name(classInstanceName).build());
        // fix 无法使用以下不同的参数继承graphql.language.Node: <? extends graphql.lactNode<?>&graphql.language.TypeDefinition<?>> 和 <?>
        items.forEach(it -> {
            if (JavadocUtils.getDocForType(it).isEmpty()) {
                Assert.fail(it + "could not get description!");
            }
        });
    }

    @Test
    public void should_get_description() {
        TypeDefinition<?> typeDefinition = ObjectTypeDefinition.newObjectTypeDefinition()
                .name("o1")
                .description(new Description("This is A Object Type", null, false))
                .build();

        assertEquals("This is A Object Type" + System.lineSeparator() + GeneratorGlobalConst.GENERAL_CODE_BLOCK,
                JavadocUtils.getDocForType(typeDefinition).toString());
    }

    @Test
    public void should_get_type_name_if_description_content_is_null() {
        TypeDefinition<?> typeDefinition = ObjectTypeDefinition.newObjectTypeDefinition()
                .name("o1")
                .description(new Description(null, null, false))
                .build();

        assertEquals("o1" + System.lineSeparator() + GeneratorGlobalConst.GENERAL_CODE_BLOCK,
                JavadocUtils.getDocForType(typeDefinition).toString());
    }

    @Test
    public void should_get_default_value_if_description_content_is_null() {
        Description description = new Description(null, null, false);

        assertEquals("d1" + System.lineSeparator() + GeneratorGlobalConst.GENERAL_CODE_BLOCK,
                JavadocUtils.getClassDocByDescription(description, "d1").toString());
    }

    @Test
    public void should_get_description_without_general_by_field() {
        Description description = new Description("Field1", null, false);

        assertEquals("Field1", JavadocUtils.getFieldDocByDescription(description, "Field1").toString());
    }

}