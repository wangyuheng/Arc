package io.github.wangyuheng.arc.graphql.util;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GraphqlPayloadUtilTest {

    @Test
    public void should_return_null_if_args_is_null() {
        assertNull(GraphqlPayloadUtil.resolveArguments(null, Mock.class));
    }

    @Test
    public void should_return_null_if_args_is_empty() {
        Mock mock = GraphqlPayloadUtil.resolveArguments(new HashMap<>(), Mock.class);
        assertNull(mock);
    }

    @Test
    public void should_resolve_args() {
        String json = "{" +
                "    \"payload\": {" +
                "        \"id\": \"abc\"" +
                "    }" +
                "}";
        Map<String, Object> args = JSON.parseObject(json);
        Mock mock = GraphqlPayloadUtil.resolveArguments(args, Mock.class);
        assertEquals("abc", mock.getId());
    }

    private static class Mock {
        private String id;

        public Mock() {
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String toString() {
            return "GraphqlPayloadUtilTest.Mock(id=" + this.getId() + ")";
        }
    }
}