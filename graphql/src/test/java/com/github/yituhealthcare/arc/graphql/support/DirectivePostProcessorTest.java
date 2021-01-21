package com.github.yituhealthcare.arc.graphql.support;

import com.github.yituhealthcare.arc.graphql.annotation.Directive;
import graphql.schema.idl.SchemaDirectiveWiring;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests for {@link DirectivePostProcessor}.
 *
 * @author yuheng.wang
 */
public class DirectivePostProcessorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final DirectivePostProcessor directivePostProcessor = new DirectivePostProcessor();

    @Test
    public void should_throw_exception_if_directive_class_not_a_wiring() {
        thrown.expect(BeanInitializationException.class);
        thrown.expectMessage("directive bean must implements SchemaDirectiveWiring! bean:a");
        directivePostProcessor.postProcessBeforeInitialization(new IllegalDirective(), "a");
    }

    @Test
    public void should_throw_exception_if_directive_class_not_a_wiring1() {
        directivePostProcessor.postProcessBeforeInitialization(new CorrectDirective(), "a");
        Map<String, SchemaDirectiveWiring> map = (Map<String, SchemaDirectiveWiring>) ReflectionTestUtils.getField(RuntimeWiringRegistry.class, "NAME_AND_DIRECTIVE_WIRING");
        assertNotNull(map);
        assertEquals(1, map.size());
        assertTrue(map.containsKey("b"));
    }

    @Directive
    static class IllegalDirective {

    }

    @Directive("b")
    static class CorrectDirective implements SchemaDirectiveWiring {

    }

}
