package io.github.wangyuheng.arc.generator.codegen.spec;

import com.squareup.javapoet.TypeSpec;
import org.junit.Test;

import javax.lang.model.element.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for {@link TypeSpecGenBuilder}.
 *
 * @author yuheng.wang
 */
public class TypeSpecGenBuilderTest {

    @Test
    public void should_ignore_if_fields_is_empty() {
        TypeSpec source = TypeSpec.classBuilder("TypeWithoutFields")
                .addModifiers(Modifier.PUBLIC)
                .build();
        assertEquals(source, new TypeSpecGenBuilder().apply(source));
        TypeSpec sourceWithField = source.toBuilder().addField(String.class, "id").build();
        assertNotEquals(source, new TypeSpecGenBuilder().apply(sourceWithField));
    }

}