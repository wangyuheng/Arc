package com.github.yituhealthcare.arc.generator.codegen.spec;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link FieldSpecGenSetter}.
 *
 * @author yuheng.wang
 */
public class FieldSpecGenSetterTest {

    private final FieldSpecGenSetter genSetter = new FieldSpecGenSetter();

    @Test
    public void generate_setter() {
        final String expected = "public void setAbc(java.lang.String abc) {\n" +
                "  this.abc = abc;\n" +
                "}\n";

        FieldSpec fieldSpec = FieldSpec.builder(String.class, "abc").build();
        MethodSpec methodSpec = genSetter.apply(fieldSpec);
        assertEquals(expected, methodSpec.toString());
    }

}