package io.github.wangyuheng.arc.dgraph.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link RDFUtil}.
 *
 * @author yuheng.wang
 */
public class RDFUtilTest {

    @Test
    public void should_wrapper_arr_by_rdf_around() {
        String r = RDFUtil.wrapperAndJoin("a", "b", "c", "d", "e");
        assertEquals("<a> <b> <c> <d> <e> . ", r);
    }

    @Test
    public void should_wrapper_by_rdf_around() {
        assertEquals("<a>", RDFUtil.wrapper("a"));
    }

}