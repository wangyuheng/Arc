package com.github.yituhealthcare.arc.graphql.interceptor;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ConditionalInterceptorWrapper}.
 *
 * @author yuheng.wang
 */
public class ConditionalInterceptorWrapperTest {

    @Test
    public void regex_test() {
        String s = "createProjects";
        assertTrue(s.matches("(.*)(?i)project(.*)"));
        assertTrue(s.matches("(.*)(?i)Project(.*)"));
    }

    @Test
    public void should_match_if_pattern_is_empty() {
        ConditionalInterceptorWrapper wrapper = new ConditionalInterceptorWrapper(null);
        String name = "project/list";
        assertTrue(wrapper.isMatch(name));
    }

    @Test
    public void should_match_if_add_correct_pattern() {
        ConditionalInterceptorWrapper wrapper = new ConditionalInterceptorWrapper(null);
        String name = "project/list";
        wrapper.addPatterns("error(.*)");
        assertFalse(wrapper.isMatch(name));
        wrapper.addPatterns("project(.*)");
        assertTrue(wrapper.isMatch(name));
    }

    @Test
    public void should_match_if_not_in_exclude_pattern() {
        ConditionalInterceptorWrapper wrapper = new ConditionalInterceptorWrapper(null);
        String name = "project/list";
        wrapper.excludePatterns("error(.*)");
        assertTrue(wrapper.isMatch(name));
    }

    @Test
    public void should_not_match_if_in_exclude_pattern() {
        ConditionalInterceptorWrapper wrapper = new ConditionalInterceptorWrapper(null);
        String name = "project/list";
        wrapper.excludePatterns("project(.*)");
        assertFalse(wrapper.isMatch(name));
    }

    @Test
    public void should_not_match_if_in_exclude_pattern_although_in_pattern() {
        ConditionalInterceptorWrapper wrapper = new ConditionalInterceptorWrapper(null);
        String name = "project/list";
        wrapper.excludePatterns("project(.*)");
        wrapper.addPatterns("project(.*)");
        assertFalse(wrapper.isMatch(name));
    }

}